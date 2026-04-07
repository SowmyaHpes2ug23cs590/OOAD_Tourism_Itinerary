package com.tourism.planner.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Structural Design Pattern: Adapter Pattern
 *
 * This adapter class implements our system's DestinationInfoProvider interface
 * but internally calls the LegacyDestinationSystem. It bridges the gap between
 * our new interface and the old system component without modifying either.
 */
@Component
public class ExternalApiAdapter implements DestinationInfoProvider {

    private final LegacyDestinationSystem legacySystem;

    @Autowired
    public ExternalApiAdapter(LegacyDestinationSystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    @Override
    public String getInfo(String cityName) {
        // Adapt the specific call to the legacy system method
        return legacySystem.fetchLegacyData(cityName);
    }
}
