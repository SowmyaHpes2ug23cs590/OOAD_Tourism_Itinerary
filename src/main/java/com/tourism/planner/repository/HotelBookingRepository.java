package com.tourism.planner.repository;

import com.tourism.planner.entity.HotelBooking;
import com.tourism.planner.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByItinerary(Itinerary itinerary);
}
