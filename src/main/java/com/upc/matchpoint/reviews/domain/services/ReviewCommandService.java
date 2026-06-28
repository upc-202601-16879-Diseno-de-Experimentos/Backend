package com.upc.matchpoint.reviews.domain.services;

import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.domain.model.commands.CreateReviewCommand;
import java.util.Optional;

public interface ReviewCommandService {
    Optional<Review> handle(CreateReviewCommand command);
}
