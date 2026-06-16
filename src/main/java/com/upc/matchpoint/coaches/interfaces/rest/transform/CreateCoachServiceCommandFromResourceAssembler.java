package com.upc.matchpoint.coaches.interfaces.rest.transform;

import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachServiceCommand;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CreateCoachServiceResource;

public class CreateCoachServiceCommandFromResourceAssembler {
    public static CreateCoachServiceCommand toCommandFromResource(Long coachId, CreateCoachServiceResource resource) {
        return new CreateCoachServiceCommand(
            coachId,
            resource.name(),
            resource.description(),
            resource.price()
        );
    }
}
