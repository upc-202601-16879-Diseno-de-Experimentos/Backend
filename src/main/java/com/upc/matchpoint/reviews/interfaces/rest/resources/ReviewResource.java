package com.upc.matchpoint.reviews.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReviewResource(Long id, Long coachId, Long userProfileId, Integer rating, String comment, LocalDateTime createdAt) {
}
