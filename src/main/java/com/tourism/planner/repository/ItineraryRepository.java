package com.tourism.planner.repository;

import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUser(User user);
}
