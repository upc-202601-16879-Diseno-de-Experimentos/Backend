package com.upc.matchpoint.reviews.domain.services;

import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.domain.model.queries.GetReviewsByCoachIdQuery;
import java.util.List;

public interface ReviewQueryService {
    List<Review> handle(GetReviewsByCoachIdQuery query);
}
