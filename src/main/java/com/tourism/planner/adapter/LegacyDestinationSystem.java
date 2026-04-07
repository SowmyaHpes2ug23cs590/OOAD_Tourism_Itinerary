package com.tourism.planner.adapter;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;

/**
 * Simulates a legacy third-party system or static source.
 */
@Component
public class LegacyDestinationSystem {
    private final Map<String, String> legacyData = new HashMap<>();

    public LegacyDestinationSystem() {
        legacyData.put("Paris", "City of Light. Known for the Eiffel Tower and Louvre.");
        legacyData.put("Kyoto", "Heart of traditional Japan with thousands of classical Buddhist temples.");
        legacyData.put("Maui", "Famous Hawaiian island known for its beautiful beaches and Haleakalā.");
    }

    public String fetchLegacyData(String queryCode) {
        return legacyData.getOrDefault(queryCode, "No legacy data available for this location.");
    }
}
