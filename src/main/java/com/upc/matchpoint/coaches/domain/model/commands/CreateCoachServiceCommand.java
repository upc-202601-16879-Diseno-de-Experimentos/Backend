package com.upc.matchpoint.coaches.domain.model.commands;

public record CreateCoachServiceCommand(Long coachId, String name, String description, Double price) {
}
