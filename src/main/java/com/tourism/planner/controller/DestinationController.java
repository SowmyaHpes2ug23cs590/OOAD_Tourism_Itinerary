package com.tourism.planner.controller;

import com.tourism.planner.entity.DestinationType;
import com.tourism.planner.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @Autowired
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/{itineraryId}")
    public String viewDestinations(@PathVariable Long itineraryId, Model model) {
        model.addAttribute("destinations", destinationService.getDestinationsByItinerary(itineraryId));
        model.addAttribute("itineraryId", itineraryId);
        return "destination-list";
    }

    @GetMapping("/add")
    public String showAddDestinationForm(@RequestParam Long itineraryId, Model model) {
        model.addAttribute("itineraryId", itineraryId);
        model.addAttribute("types", DestinationType.values());
        return "destination-form";
    }

    @PostMapping("/add")
    public String addDestination(@RequestParam Long itineraryId,
                                 @RequestParam String name,
                                 @RequestParam String country,
                                 @RequestParam String city,
                                 @RequestParam DestinationType type,
                                 @RequestParam String bestSeason,
                                 @RequestParam String notes) {
        destinationService.addDestination(itineraryId, name, country, city, type, bestSeason, notes);
        return "redirect:/destinations/" + itineraryId;
    }

    @PostMapping("/remove/{id}")
    public String removeDestination(@PathVariable Long id, @RequestParam Long itineraryId) {
        destinationService.removeDestination(id);
        return "redirect:/destinations/" + itineraryId;
    }
}
