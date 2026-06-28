package com.upc.matchpoint.reviews.interfaces.rest;

import com.upc.matchpoint.reviews.domain.model.queries.GetReviewsByCoachIdQuery;
import com.upc.matchpoint.reviews.domain.services.ReviewCommandService;
import com.upc.matchpoint.reviews.domain.services.ReviewQueryService;
import com.upc.matchpoint.reviews.interfaces.rest.resources.CreateReviewResource;
import com.upc.matchpoint.reviews.interfaces.rest.resources.ReviewResource;
import com.upc.matchpoint.reviews.interfaces.rest.transform.CreateReviewCommandFromResourceAssembler;
import com.upc.matchpoint.reviews.interfaces.rest.transform.ReviewResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reviews", description = "Review Management Endpoints")
public class ReviewsController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    public ReviewsController(ReviewCommandService reviewCommandService, ReviewQueryService reviewQueryService) {
        this.reviewCommandService = reviewCommandService;
        this.reviewQueryService = reviewQueryService;
    }

    @PostMapping
    public ResponseEntity<ReviewResource> createReview(@RequestBody CreateReviewResource resource) {
        var command = CreateReviewCommandFromResourceAssembler.toCommandFromResource(resource);
        var review = reviewCommandService.handle(command);
        return review.map(r -> new ResponseEntity<>(ReviewResourceFromEntityAssembler.toResourceFromEntity(r), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<ReviewResource>> getReviewsByCoachId(@PathVariable Long coachId) {
        var query = new GetReviewsByCoachIdQuery(coachId);
        var reviews = reviewQueryService.handle(query);
        var resources = reviews.stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
