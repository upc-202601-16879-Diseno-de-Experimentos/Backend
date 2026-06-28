package com.upc.matchpoint.courts.interfaces.rest.resources;

public record UpdateCourtResource(String name, String location, String type,
                                   String sportType, Double pricePerHour, String description,
                                   String imageUrl, Boolean isAvailable, String openingHours) {
}
