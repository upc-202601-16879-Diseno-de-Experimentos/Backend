package com.upc.matchpoint.reviews.application.internal.commandservices;

import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.domain.model.commands.CreateReviewCommand;
import com.upc.matchpoint.reviews.domain.services.ReviewCommandService;
import com.upc.matchpoint.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final CoachRepository coachRepository;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository, CoachRepository coachRepository) {
        this.reviewRepository = reviewRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    @Transactional
    public Optional<Review> handle(CreateReviewCommand command) {
        // Validate coach existence
        var coachOpt = coachRepository.findById(command.coachId());
        if (coachOpt.isEmpty()) {
            throw new IllegalArgumentException("Coach with id " + command.coachId() + " not found");
        }

        // Create review
        var review = new Review(command.coachId(), command.userProfileId(), command.rating(), command.comment());
        var savedReview = reviewRepository.save(review);

        // Update coach rating
        var coach = coachOpt.get();
        coach.updateRating(command.rating().doubleValue());
        coachRepository.save(coach);

        return Optional.of(savedReview);
    }
}
