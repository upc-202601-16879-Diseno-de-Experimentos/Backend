package com.upc.matchpoint.coaches.interfaces.rest.resources;

public record CoachResource(Long id, String name, String expertise, String phone, String availability,
                             String email, String sportType, Double pricePerHour, String location,
                             String description, String imageUrl, Double rating, Integer totalReviews,
                             Boolean isAvailable, Integer experienceYears) {
}
