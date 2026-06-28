package com.upc.matchpoint.coaches.interfaces.rest.resources;

public record CreateCoachResource(String name, String expertise, String phone, String availability,
                                   String email, String sportType, Double pricePerHour, String location,
                                   String description, String imageUrl, Boolean isAvailable, Integer experienceYears) {
}
