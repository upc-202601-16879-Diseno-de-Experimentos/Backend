package com.upc.matchpoint.coaches.domain.model.commands;

public record UpdateCoachCommand(Long coachId, String name, String expertise, String phone, String availability,
                                  String email, String sportType, Double pricePerHour, String location,
                                  String description, String imageUrl, Boolean isAvailable, Integer experienceYears) {
}
