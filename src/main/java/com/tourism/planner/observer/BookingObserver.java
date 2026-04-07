package com.tourism.planner.observer;

import com.tourism.planner.entity.HotelBooking;

/**
 * Behavioral Design Pattern: Observer Pattern
 *
 * This interface is implemented by any component that fundamentally
 * needs to react to changes in HotelBooking statuses across the application,
 * entirely decoupled from the explicit BookingService logic.
 */
public interface BookingObserver {
    void onBookingStatusChanged(HotelBooking booking);
}
