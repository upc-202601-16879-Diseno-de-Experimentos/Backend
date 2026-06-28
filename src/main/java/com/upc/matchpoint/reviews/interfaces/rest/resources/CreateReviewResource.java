package com.upc.matchpoint.reviews.interfaces.rest.resources;

public record CreateReviewResource(Long coachId, Long userProfileId, Integer rating, String comment) {
}
