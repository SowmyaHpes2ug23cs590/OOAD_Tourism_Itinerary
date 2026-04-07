package com.tourism.planner.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "hotel_bookings")
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @Column(nullable = false)
    private String hotelName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Double pricePerNight;

    @Column(nullable = false)
    private Double totalCost;

    private String bookingRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public HotelBooking() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Itinerary getItinerary() { return itinerary; }
    public void setItinerary(Itinerary itinerary) { this.itinerary = itinerary; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public String getBookingRef() { return bookingRef; }
    public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public void calculateTotalCost() {
        if (checkIn != null && checkOut != null && pricePerNight != null) {
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalCost = (nights > 0 ? nights : 1) * pricePerNight;
        }
    }
}
