package com.upc.matchpoint.reviews.interfaces.rest.transform;

import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.interfaces.rest.resources.ReviewResource;

public class ReviewResourceFromEntityAssembler {
    public static ReviewResource toResourceFromEntity(Review entity) {
        return new ReviewResource(
                entity.getId(),
                entity.getCoachId(),
                entity.getUserProfileId(),
                entity.getRating(),
                entity.getComment(),
                entity.getCreatedAt()
        );
    }
}
