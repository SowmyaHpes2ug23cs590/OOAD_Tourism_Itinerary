package com.tourism.planner.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("URBAN")
public class UrbanDestination extends Destination {

    @Override
    public String fetchLocationGuidelines() {
        return "Urban Guidelines: Use public transport, beware of pickpockets, and check local city passes.";
    }
}
