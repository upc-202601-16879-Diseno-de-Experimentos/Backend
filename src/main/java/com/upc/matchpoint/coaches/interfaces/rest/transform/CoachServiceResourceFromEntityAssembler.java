package com.upc.matchpoint.coaches.interfaces.rest.transform;

import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CoachServiceResource;

public class CoachServiceResourceFromEntityAssembler {
    public static CoachServiceResource toResourceFromEntity(CoachService entity) {
        return new CoachServiceResource(
            entity.getId(),
            entity.getCoach().getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice()
        );
    }
}
