package com.upc.matchpoint.reviews.interfaces.rest.transform;

import com.upc.matchpoint.reviews.domain.model.commands.CreateReviewCommand;
import com.upc.matchpoint.reviews.interfaces.rest.resources.CreateReviewResource;

public class CreateReviewCommandFromResourceAssembler {
    public static CreateReviewCommand toCommandFromResource(CreateReviewResource resource) {
        return new CreateReviewCommand(
                resource.coachId(),
                resource.userProfileId(),
                resource.rating(),
                resource.comment()
        );
    }
}
