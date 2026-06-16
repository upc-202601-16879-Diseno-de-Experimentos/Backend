package com.upc.matchpoint.bookings.interfaces.rest.resources;

import java.time.LocalDateTime;

public record BookingResource(Long id, LocalDateTime startTime, LocalDateTime endTime, UserSummaryResource user, CourtSummaryResource court, CoachServiceSummaryResource coachService, Double amount, String status) {
    public record UserSummaryResource(Long id, String name) {}
    public record CourtSummaryResource(Long id, String name) {}
    public record CoachServiceSummaryResource(Long id, String name, Double price, CoachSummaryResource coach) {}
    public record CoachSummaryResource(Long id, String name) {}
}

