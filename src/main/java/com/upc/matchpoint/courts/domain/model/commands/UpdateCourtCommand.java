package com.upc.matchpoint.courts.domain.model.commands;

public record UpdateCourtCommand(Long courtId, String name, String location, String type,
                                  String sportType, Double pricePerHour, String description,
                                  String imageUrl, Boolean isAvailable, String openingHours) {
}
