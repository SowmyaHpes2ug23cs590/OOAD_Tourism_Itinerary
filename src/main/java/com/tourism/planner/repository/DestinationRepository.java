package com.tourism.planner.repository;

import com.tourism.planner.entity.Destination;
import com.tourism.planner.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByItinerary(Itinerary itinerary);
}
