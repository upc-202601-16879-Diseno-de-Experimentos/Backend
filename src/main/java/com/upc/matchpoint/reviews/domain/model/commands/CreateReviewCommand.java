package com.upc.matchpoint.reviews.domain.model.commands;

public record CreateReviewCommand(Long coachId, Long userProfileId, Integer rating, String comment) {
}
