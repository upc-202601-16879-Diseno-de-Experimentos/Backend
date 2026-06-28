package com.upc.matchpoint.courts.interfaces.rest.transform;

import com.upc.matchpoint.courts.domain.model.commands.CreateCourtCommand;
import com.upc.matchpoint.courts.interfaces.rest.resources.CreateCourtResource;

public class CreateCourtCommandFromResourceAssembler {
    public static CreateCourtCommand toCommandFromResource(CreateCourtResource resource) {
        return new CreateCourtCommand(
                resource.name(),
                resource.location(),
                resource.type(),
                resource.sportType(),
                resource.pricePerHour(),
                resource.description(),
                resource.imageUrl(),
                resource.isAvailable(),
                resource.openingHours()
        );
    }
}
