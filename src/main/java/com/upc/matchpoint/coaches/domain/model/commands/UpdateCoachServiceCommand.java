package com.upc.matchpoint.coaches.domain.model.commands;

public record UpdateCoachServiceCommand(Long serviceId, String name, String description, Double price) {
}
