package com.tourism.planner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itineraries")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    private String destinationSummary;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Positive(message = "Budget must be positive")
    @Column(nullable = false)
    private Double totalBudget;

    @Column(nullable = false)
    private String status = "Draft"; // Lifecycle: Draft, Active, Completed, Deleted

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayPlan> dayPlans = new ArrayList<>();

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelBooking> hotelBookings = new ArrayList<>();

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Destination> destinations = new ArrayList<>();

    public Itinerary() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDestinationSummary() { return destinationSummary; }
    public void setDestinationSummary(String destinationSummary) { this.destinationSummary = destinationSummary; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<DayPlan> getDayPlans() { return dayPlans; }
    public void setDayPlans(List<DayPlan> dayPlans) { this.dayPlans = dayPlans; }

    @Transient
    public long getTotalDays() {
        if (startDate != null && endDate != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return 0;
    }

    @Transient
    public Double getTotalSpent() {
        double activityCost = 0.0;
        if (dayPlans != null) {
            activityCost = dayPlans.stream()
                .filter(dp -> dp.getActivities() != null)
                .flatMap(dp -> dp.getActivities().stream())
                .filter(act -> act.getEstimatedCost() != null)
                .mapToDouble(Activity::getEstimatedCost)
                .sum();
        }
        
        double hotelCost = 0.0;
        if (hotelBookings != null) {
            hotelCost = hotelBookings.stream()
                .filter(hb -> hb.getStatus() == BookingStatus.CONFIRMED && hb.getTotalCost() != null)
                .mapToDouble(HotelBooking::getTotalCost)
                .sum();
        }
        
        return activityCost + hotelCost;
    }
}
