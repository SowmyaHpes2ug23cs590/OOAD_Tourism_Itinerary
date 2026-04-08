package com.tourism.planner.observer;

import com.tourism.planner.entity.HotelBooking;
import org.springframework.stereotype.Component;

@Component
public class SpendingUpdater implements BookingObserver {

    @Override
    public void onBookingStatusChanged(HotelBooking booking) {
        // Observers passively react to isolated changes in domain state. 
        // Example: if a booking changes from CONFIRMED to CANCELLED, 
        // we might instruct the analytic cache to deduct its totalCost.
        System.out.println("SpendingUpdater OBSERVER triggered: Booking ID " + 
                           booking.getId() + " changed status to " + booking.getStatus() + 
                           ". Recalculating cached spending totals natively...");
    }
}
