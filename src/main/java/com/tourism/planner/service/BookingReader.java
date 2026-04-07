package com.tourism.planner.service;

import com.tourism.planner.entity.HotelBooking;
import java.util.List;

/**
 * Interface Segregation Principle (ISP)
 * Defines strictly read-only operations for hotel bookings.
 */
public interface BookingReader {
    List<HotelBooking> getBookingsByItinerary(Long itineraryId);
}
