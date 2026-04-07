package com.tourism.planner.service;

import com.tourism.planner.entity.HotelBooking;

/**
 * Interface Segregation Principle (ISP)
 * Defines exclusively mutating write operations for bookings.
 */
public interface BookingWriter {
    HotelBooking addBooking(HotelBooking booking);
    void cancelBooking(Long bookingId);
}
