package com.tourism.planner.service;

import com.tourism.planner.builder.ItineraryBuilder;
import com.tourism.planner.entity.DayPlan;
import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.entity.User;
import com.tourism.planner.repository.DayPlanRepository;
import com.tourism.planner.repository.ItineraryRepository;
import com.tourism.planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final DayPlanRepository dayPlanRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItineraryService(ItineraryRepository itineraryRepository, 
                            DayPlanRepository dayPlanRepository,
                            UserRepository userRepository) {
        this.itineraryRepository = itineraryRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.userRepository = userRepository;
    }

    public List<Itinerary> getUserItineraries(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return itineraryRepository.findByUser(user);
    }

    public Itinerary getItineraryById(Long id) {
        return itineraryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Itinerary ID"));
    }

    @Transactional
    public Itinerary createItinerary(String email, String title, String summary, 
                                     LocalDate startDate, LocalDate endDate, Double budget) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // Using Builder Pattern cleanly maps configuration of an object
        // and enforces required conditions in one fluid statement.
        Itinerary itinerary = new ItineraryBuilder()
                .setUser(user)
                .setTitle(title)
                .setDestinationSummary(summary)
                .setDates(startDate, endDate)
                .setBudget(budget)
                .build();

        return itineraryRepository.save(itinerary);
    }

    @Transactional
    public void updateItinerary(Long id, String title, String summary, LocalDate startDate, LocalDate endDate, Double budget) {
        Itinerary itinerary = getItineraryById(id);
        
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        if (budget != null && budget <= 0) {
            throw new IllegalArgumentException("Budget must be positive.");
        }

        itinerary.setTitle(title);
        itinerary.setDestinationSummary(summary);
        itinerary.setStartDate(startDate);
        itinerary.setEndDate(endDate);
        itinerary.setTotalBudget(budget);
        
        itineraryRepository.save(itinerary);
    }

    @Transactional
    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }

    @Transactional
    public DayPlan addDayPlan(Long itineraryId, Integer dayNumber) {
        Itinerary itinerary = getItineraryById(itineraryId);
        DayPlan dayPlan = new DayPlan();
        dayPlan.setItinerary(itinerary);
        dayPlan.setDayNumber(dayNumber);
        return dayPlanRepository.save(dayPlan);
    }
}
