package com.tourism.planner.controller;

import com.tourism.planner.entity.HotelBooking;
import com.tourism.planner.entity.BookingStatus;
import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.service.BookingReader;
import com.tourism.planner.service.BookingWriter;
import com.tourism.planner.service.BookingExporter;
import com.tourism.planner.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    // Demonstrating ISP (Interface Segregation Principle):
    // The controller isolates the read/write/export interfaces separately 
    // ensuring loose coupling tailored narrowly to discrete operational domains.
    private final BookingReader bookingReader;
    private final BookingWriter bookingWriter;
    private final BookingExporter bookingExporter;
    private final ItineraryRepository itineraryRepository;

    @Autowired
    public BookingController(BookingReader bookingReader, BookingWriter bookingWriter, 
                             BookingExporter bookingExporter, ItineraryRepository itineraryRepository) {
        this.bookingReader = bookingReader;
        this.bookingWriter = bookingWriter;
        this.bookingExporter = bookingExporter;
        this.itineraryRepository = itineraryRepository;
    }

    @GetMapping("/{itineraryId}")
    public String viewBookings(@PathVariable Long itineraryId, Model model) {
        model.addAttribute("bookings", bookingReader.getBookingsByItinerary(itineraryId));
        model.addAttribute("itineraryId", itineraryId);
        model.addAttribute("itinerary", itineraryRepository.findById(itineraryId).orElseThrow());
        return "booking-list";
    }

    @GetMapping("/add")
    public String showAddBookingForm(@RequestParam Long itineraryId, Model model) {
        model.addAttribute("itineraryId", itineraryId);
        return "booking-form";
    }
    
    @PostMapping("/add")
    public String addBooking(@RequestParam Long itineraryId,
                             @RequestParam String hotelName,
                             @RequestParam String city,
                             @RequestParam LocalDate checkIn,
                             @RequestParam LocalDate checkOut,
                             @RequestParam Double pricePerNight,
                             @RequestParam String bookingRef) {
                             
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow();
        HotelBooking booking = new HotelBooking();
        booking.setItinerary(itinerary);
        booking.setHotelName(hotelName);
        booking.setCity(city);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setPricePerNight(pricePerNight);
        booking.setBookingRef(bookingRef);
        booking.setStatus(BookingStatus.CONFIRMED);
        
        bookingWriter.addBooking(booking);
        return "redirect:/bookings/" + itineraryId;
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, @RequestParam Long itineraryId) {
        bookingWriter.cancelBooking(id);
        return "redirect:/bookings/" + itineraryId;
    }
    
    @GetMapping("/export/{itineraryId}")
    public ResponseEntity<byte[]> exportSummary(@PathVariable Long itineraryId) {
        String summary = bookingExporter.generateItinerarySummary(itineraryId);
        byte[] output = summary.getBytes();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "itinerary_" + itineraryId + "_summary.txt");
        headers.setContentLength(output.length);
        
        return ResponseEntity.ok().headers(headers).body(output);
    }
}
