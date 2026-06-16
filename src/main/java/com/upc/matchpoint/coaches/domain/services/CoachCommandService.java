package com.upc.matchpoint.coaches.domain.services;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.UpdateCoachCommand;
import java.util.Optional;

public interface CoachCommandService {
    Optional<Coach> handle(CreateCoachCommand command);
    Optional<Coach> handle(UpdateCoachCommand command);
    void handle(DeleteCoachCommand command);
    Optional<com.upc.matchpoint.coaches.domain.model.entities.CoachService> handle(com.upc.matchpoint.coaches.domain.model.commands.CreateCoachServiceCommand command);
    void handle(com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachServiceCommand command);
}

