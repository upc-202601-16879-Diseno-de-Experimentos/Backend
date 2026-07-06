package com.upc.matchpoint.users.interfaces.rest.resources;

public record CreateUserProfileResource(Long userId, String name, String email, String phone) {
}

