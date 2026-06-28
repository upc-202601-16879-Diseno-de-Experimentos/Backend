package com.upc.matchpoint.courts.interfaces.rest.resources;

public record CourtResource(Long id, String name, String location, String type,
                             String sportType, Double pricePerHour, String description,
                             String imageUrl, Boolean isAvailable, String openingHours) {
}
