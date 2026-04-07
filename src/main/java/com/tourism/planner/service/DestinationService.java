package com.tourism.planner.service;

import com.tourism.planner.adapter.DestinationInfoProvider;
import com.tourism.planner.entity.*;
import com.tourism.planner.repository.DestinationRepository;
import com.tourism.planner.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final ItineraryRepository itineraryRepository;
    private final DestinationInfoProvider infoProvider;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository,
                              ItineraryRepository itineraryRepository,
                              DestinationInfoProvider infoProvider) {
        this.destinationRepository = destinationRepository;
        this.itineraryRepository = itineraryRepository;
        this.infoProvider = infoProvider;
    }

    public List<Destination> getDestinationsByItinerary(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid itinerary ID"));

        List<Destination> destinations = destinationRepository.findByItinerary(itinerary);

        // Liskov Substitution Principle (LSP) demonstration:
        // We call fetchLocationGuidelines() on the Destination base type — no instanceof checks.
        // UrbanDestination and NaturalDestination each supply their own correct implementation.
        for (Destination dest : destinations) {
            // Log guidelines cleanly — each subtype responds correctly without type casting
            System.out.println("LSP: Guidelines for [" + dest.getName() + "]: " + dest.fetchLocationGuidelines());
        }

        return destinations;
    }

    public String fetchExternalInfo(String cityName) {
        return infoProvider.getInfo(cityName);
    }

    @Transactional
    public Destination addDestination(Long itineraryId, String name, String country, String city,
                                      DestinationType type, String bestSeason, String notes) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid itinerary ID"));

        Destination destination;
        if (type == DestinationType.CITY || type == DestinationType.HERITAGE) {
            destination = new UrbanDestination();
        } else {
            destination = new NaturalDestination();
        }

        destination.setItinerary(itinerary);
        destination.setName(name);
        destination.setCountry(country);
        destination.setCity(city);
        destination.setType(type);
        destination.setBestSeason(bestSeason);

        String externalInfo = infoProvider.getInfo(city);
        if (externalInfo != null && !externalInfo.contains("No legacy data")) {
            destination.setNotes(notes + " [Info: " + externalInfo + "]");
        } else {
            destination.setNotes(notes);
        }

        return destinationRepository.save(destination);
    }

    @Transactional
    public void removeDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
