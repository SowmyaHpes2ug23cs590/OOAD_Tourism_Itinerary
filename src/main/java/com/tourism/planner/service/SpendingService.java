package com.tourism.planner.service;

import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.entity.User;
import com.tourism.planner.repository.ItineraryRepository;
import com.tourism.planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpendingService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;

    @Autowired
    public SpendingService(ItineraryRepository itineraryRepository, UserRepository userRepository) {
        this.itineraryRepository = itineraryRepository;
        this.userRepository = userRepository;
    }

    public static class SpendingItem {
        private final String itineraryName;
        private final Double estimatedCost;
        private final Long itineraryId;

        public SpendingItem(Long itineraryId, String itineraryName, Double estimatedCost) {
            this.itineraryId = itineraryId;
            this.itineraryName = itineraryName;
            this.estimatedCost = estimatedCost;
        }

        public Long getItineraryId() { return itineraryId; }
        public String getItineraryName() { return itineraryName; }
        public Double getEstimatedCost() { return estimatedCost; }
    }

    public List<SpendingItem> getSpendingReportForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Itinerary> itineraries = itineraryRepository.findByUser(user);

        return itineraries.stream()
                .filter(it -> it.getTotalBudget() != null)
                .map(it -> new SpendingItem(it.getId(), it.getTitle(), it.getTotalBudget()))
                .sorted(Comparator.comparingDouble(SpendingItem::getEstimatedCost).reversed())
                .collect(Collectors.toList());
    }

    public Double getTotalEstimatedSpend(List<SpendingItem> items) {
        return items.stream()
                .mapToDouble(SpendingItem::getEstimatedCost)
                .sum();
    }
}
