package com.upc.matchpoint.users.domain.model.commands;

public record CreateUserProfileCommand(Long userId, String name, String email, String phone) {
}

