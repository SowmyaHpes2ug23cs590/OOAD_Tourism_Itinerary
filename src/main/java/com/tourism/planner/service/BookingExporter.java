package com.tourism.planner.service;

/**
 * Interface Segregation Principle (ISP)
 * Defines isolated operations intended solely for export logic.
 */
public interface BookingExporter {
    String generateItinerarySummary(Long itineraryId);
}
