package com.tourism.planner.controller;

import com.tourism.planner.entity.Itinerary;
import com.tourism.planner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping
    public String listItineraries(Model model, Authentication authentication) {
        model.addAttribute("itineraries", itineraryService.getUserItineraries(authentication.getName()));
        return "itinerary-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("itinerary", new Itinerary());
        return "itinerary-form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("itinerary", itineraryService.getItineraryById(id));
        return "itinerary-form";
    }

    @PostMapping("/new")
    public String createItinerary(@RequestParam String title,
                                  @RequestParam(required = false, defaultValue = "") String destinationSummary,
                                  @RequestParam LocalDate startDate,
                                  @RequestParam LocalDate endDate,
                                  @RequestParam Double totalBudget,
                                  Authentication authentication) {
        try {
            itineraryService.createItinerary(authentication.getName(), title, destinationSummary, startDate, endDate, totalBudget);
        } catch (IllegalArgumentException e) {
            // Very simple error handling for demo
            return "redirect:/itineraries/new?error=" + e.getMessage();
        }
        return "redirect:/itineraries";
    }

    @GetMapping("/{id}")
    public String viewItinerary(@PathVariable Long id, Model model) {
        Itinerary itinerary = itineraryService.getItineraryById(id);
        model.addAttribute("itinerary", itinerary);
        return "itinerary-detail";
    }

    @PostMapping("/{id}/edit")
    public String updateItinerary(@PathVariable Long id,
                                  @RequestParam String title, 
                                  @RequestParam String destinationSummary,
                                  @RequestParam LocalDate startDate, 
                                  @RequestParam LocalDate endDate,
                                  @RequestParam Double totalBudget) {
        itineraryService.updateItinerary(id, title, destinationSummary, startDate, endDate, totalBudget);
        return "redirect:/itineraries/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return "redirect:/itineraries";
    }

    @PostMapping("/{id}/days")
    public String addDayPlan(@PathVariable Long id, @RequestParam Integer dayNumber) {
        itineraryService.addDayPlan(id, dayNumber);
        return "redirect:/itineraries/" + id;
    }
}
