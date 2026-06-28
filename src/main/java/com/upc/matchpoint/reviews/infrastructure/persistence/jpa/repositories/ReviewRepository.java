package com.upc.matchpoint.reviews.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCoachId(Long coachId);
}
