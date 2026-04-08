package com.tourism.planner.controller;

import com.tourism.planner.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // Creating activity inline via the itinerary-detail page
    @PostMapping("/new")
    public String addActivity(@RequestParam Long itineraryId,
                              @RequestParam Long dayPlanId, 
                              @RequestParam String name,
                              @RequestParam LocalTime startTime,
                              @RequestParam Integer durationMins,
                              @RequestParam Double estimatedCost,
                              @RequestParam(required = false, defaultValue = "") String notes) {
        activityService.addActivity(dayPlanId, name, startTime, durationMins, estimatedCost, notes);
        return "redirect:/itineraries/" + itineraryId;
    }

    @PostMapping("/{id}/delete")
    public String deleteActivity(@PathVariable Long id, @RequestParam Long itineraryId) {
        activityService.deleteActivity(id);
        return "redirect:/itineraries/" + itineraryId;
    }
}
