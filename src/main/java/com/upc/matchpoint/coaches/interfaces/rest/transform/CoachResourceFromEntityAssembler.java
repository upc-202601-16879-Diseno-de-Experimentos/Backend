package com.upc.matchpoint.coaches.interfaces.rest.transform;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CoachResource;

public class CoachResourceFromEntityAssembler {
    public static CoachResource toResourceFromEntity(Coach entity) {
        return new CoachResource(
                entity.getId(),
                entity.getName(),
                entity.getExpertise(),
                entity.getPhone(),
                entity.getAvailability(),
                entity.getEmail(),
                entity.getSportType(),
                entity.getPricePerHour(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getRating(),
                entity.getTotalReviews(),
                entity.getIsAvailable(),
                entity.getExperienceYears()
        );
    }
}
