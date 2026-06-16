package com.upc.matchpoint.coaches.interfaces.rest.resources;

public record CoachServiceResource(Long id, Long coachId, String name, String description, Double price) {
}
