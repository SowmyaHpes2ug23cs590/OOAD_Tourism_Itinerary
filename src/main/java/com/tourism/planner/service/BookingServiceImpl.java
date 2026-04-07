package com.tourism.planner.service;

import com.tourism.planner.entity.HotelBooking;
import com.tourism.planner.entity.BookingStatus;
import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.repository.HotelBookingRepository;
import com.tourism.planner.repository.ItineraryRepository;
import com.tourism.planner.observer.BookingObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingReader, BookingWriter, BookingExporter {

    private final HotelBookingRepository bookingRepository;
    private final ItineraryRepository itineraryRepository;
    private final List<BookingObserver> observers;

    @Autowired
    public BookingServiceImpl(HotelBookingRepository bookingRepository, 
                              ItineraryRepository itineraryRepository,
                              List<BookingObserver> observers) {
        this.bookingRepository = bookingRepository;
        this.itineraryRepository = itineraryRepository;
        // Observer Pattern: Service dynamically injects all matching observers via Spring context
        this.observers = observers;
    }

    @Override
    public List<HotelBooking> getBookingsByItinerary(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow();
        return bookingRepository.findByItinerary(itinerary);
    }

    @Override
    @Transactional
    public HotelBooking addBooking(HotelBooking booking) {
        if (booking.getCheckIn().isAfter(booking.getCheckOut()) || booking.getCheckIn().isEqual(booking.getCheckOut())) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }
        booking.calculateTotalCost();
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.CONFIRMED);
        }
        HotelBooking saved = bookingRepository.save(booking);
        notifyObservers(saved);
        return saved;
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        HotelBooking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            // Notify observers upon state mutation
            notifyObservers(booking);
        }
    }

    private void notifyObservers(HotelBooking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingStatusChanged(booking);
        }
    }

    @Override
    public String generateItinerarySummary(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow();
        StringBuilder sb = new StringBuilder();
        sb.append("ITINERARY SUMMARY\n");
        sb.append("=========================\n");
        sb.append("Title: ").append(itinerary.getTitle()).append("\n");
        sb.append("Date: ").append(itinerary.getStartDate()).append(" to ").append(itinerary.getEndDate()).append("\n");
        sb.append("Estimated Budget: $").append(String.format("%.2f", itinerary.getTotalBudget())).append("\n\n");
        
        sb.append("--- HOTEL BOOKINGS ---\n");
        List<HotelBooking> bookings = bookingRepository.findByItinerary(itinerary);
        double totalHotelCost = 0;
        if (bookings.isEmpty()) {
            sb.append("No hotel bookings registered.\n");
        } else {
            for (HotelBooking b : bookings) {
                sb.append("- ").append(b.getHotelName()).append(" in ").append(b.getCity())
                  .append("\n  Dates: ").append(b.getCheckIn()).append(" to ").append(b.getCheckOut())
                  .append("\n  [Status: ").append(b.getStatus()).append("] Cost: $").append(String.format("%.2f", b.getTotalCost())).append("\n\n");
                
                if (b.getStatus() != BookingStatus.CANCELLED) {
                    totalHotelCost += b.getTotalCost();
                }
            }
        }
        sb.append("Confirmed Hotels Total Cost: $").append(String.format("%.2f", totalHotelCost)).append("\n");
        return sb.toString();
    }
}
