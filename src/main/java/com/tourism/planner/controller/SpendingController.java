package com.tourism.planner.controller;

import com.tourism.planner.service.SpendingService;
import com.tourism.planner.service.SpendingService.SpendingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SpendingController {

    private final SpendingService spendingService;

    @Autowired
    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @GetMapping("/spending")
    public String viewSpendingReport(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<SpendingItem> spendingItems = spendingService.getSpendingReportForUser(email);
        Double totalSpend = spendingService.getTotalEstimatedSpend(spendingItems);

        model.addAttribute("username", email);
        model.addAttribute("spendingItems", spendingItems);
        model.addAttribute("totalSpend", totalSpend);

        return "spending-report";
    }
}
