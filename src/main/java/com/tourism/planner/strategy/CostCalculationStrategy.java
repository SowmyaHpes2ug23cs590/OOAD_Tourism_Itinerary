package com.tourism.planner.strategy;

import com.tourism.planner.entity.Activity;

/**
 * Open/Closed Principle (OCP):
 * This interface defines the contract for calculating activity costs.
 * It makes the system OPEN for extension (we can add PremiumCostStrategy, 
 * DiscountedCostStrategy, etc.) but CLOSED for modification (we don't need to 
 * change existing classes to introduce new pricing rules).
 */
public interface CostCalculationStrategy {
    Double calculate(Activity activity);
}
