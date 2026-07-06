package com.upc.matchpoint.users.interfaces.rest.resources;

public record UserProfileResource(Long id, Long userId, String name, String email, String phone) {
}

