package com.tourism.planner.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NATURAL")
public class NaturalDestination extends Destination {

    @Override
    public String fetchLocationGuidelines() {
        return "Natural Guidelines: Carry insect repellent, stay on marked trails, and strictly respect wildlife.";
    }
}
