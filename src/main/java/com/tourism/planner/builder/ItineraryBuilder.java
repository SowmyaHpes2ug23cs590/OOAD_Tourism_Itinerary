package com.tourism.planner.builder;

import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.entity.User;
import java.time.LocalDate;

/**
 * Creational Design Pattern: Builder Pattern
 * 
 * This class implements the Builder pattern to construct an Itinerary object step by step.
 * It is especially useful because an Itinerary has many parameters, some of which require
 * validation (like ensuring startDate is before endDate). Instead of a constructor with
 * a long list of parameters, the builder allows readable component-wise construction.
 */
public class ItineraryBuilder {
    private final Itinerary itinerary;

    public ItineraryBuilder() {
        this.itinerary = new Itinerary();
    }

    public ItineraryBuilder setUser(User user) {
        this.itinerary.setUser(user);
        return this;
    }

    public ItineraryBuilder setTitle(String title) {
        this.itinerary.setTitle(title);
        return this;
    }

    public ItineraryBuilder setDestinationSummary(String summary) {
        this.itinerary.setDestinationSummary(summary);
        return this;
    }

    public ItineraryBuilder setDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        this.itinerary.setStartDate(startDate);
        this.itinerary.setEndDate(endDate);
        return this;
    }

    public ItineraryBuilder setBudget(Double totalBudget) {
        if (totalBudget != null && totalBudget <= 0) {
            throw new IllegalArgumentException("Budget must be positive.");
        }
        this.itinerary.setTotalBudget(totalBudget);
        return this;
    }

    public ItineraryBuilder setStatus(String status) {
        this.itinerary.setStatus(status);
        return this;
    }

    public Itinerary build() {
        if (itinerary.getUser() == null || itinerary.getTitle() == null || 
            itinerary.getStartDate() == null || itinerary.getEndDate() == null) {
            throw new IllegalStateException("Missing required fields for Itinerary.");
        }
        if (itinerary.getStatus() == null) {
            itinerary.setStatus("Draft");
        }
        return this.itinerary;
    }
}
