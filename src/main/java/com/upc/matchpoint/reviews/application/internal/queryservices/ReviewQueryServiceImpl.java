package com.upc.matchpoint.reviews.application.internal.queryservices;

import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.domain.model.queries.GetReviewsByCoachIdQuery;
import com.upc.matchpoint.reviews.domain.services.ReviewQueryService;
import com.upc.matchpoint.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {
    private final ReviewRepository reviewRepository;

    public ReviewQueryServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> handle(GetReviewsByCoachIdQuery query) {
        return reviewRepository.findByCoachId(query.coachId());
    }
}
