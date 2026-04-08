package com.tourism.planner.strategy;

import com.tourism.planner.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class StandardCostStrategy implements CostCalculationStrategy {

    @Override
    public Double calculate(Activity activity) {
        if (activity == null || activity.getEstimatedCost() == null) {
            return 0.0;
        }
        // Base behavior: return the explicitly set estimated cost of the activity.
        return activity.getEstimatedCost();
    }
}
