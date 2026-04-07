package com.tourism.planner.service;

import com.tourism.planner.entity.Activity;
import com.tourism.planner.entity.DayPlan;
import com.tourism.planner.repository.ActivityRepository;
import com.tourism.planner.repository.DayPlanRepository;
import com.tourism.planner.strategy.CostCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final DayPlanRepository dayPlanRepository;
    private final CostCalculationStrategy costStrategy;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, 
                           DayPlanRepository dayPlanRepository,
                           CostCalculationStrategy costStrategy) {
        this.activityRepository = activityRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.costStrategy = costStrategy;
    }

    @Transactional
    public Activity addActivity(Long dayPlanId, String name, LocalTime startTime, 
                                Integer durationMins, Double estimatedCost, String notes) {
        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId).orElseThrow();
        Activity activity = new Activity();
        activity.setDayPlan(dayPlan);
        activity.setName(name);
        activity.setStartTime(startTime);
        activity.setDurationMins(durationMins);
        
        if (estimatedCost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        activity.setEstimatedCost(estimatedCost);
        activity.setNotes(notes);
        
        // Strategy Pattern for OCP:
        // Calculates final cost without modifying this service if cost logic changes later.
        Double finalCost = costStrategy.calculate(activity);
        activity.setEstimatedCost(finalCost);

        return activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
