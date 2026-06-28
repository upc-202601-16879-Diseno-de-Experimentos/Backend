package com.upc.matchpoint.coaches.domain.services;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.*;
import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import java.util.Optional;

public interface CoachCommandService {
    Optional<Coach> handle(CreateCoachCommand command);
    Optional<Coach> handle(UpdateCoachCommand command);
    void handle(DeleteCoachCommand command);
    Optional<CoachService> handle(CreateCoachServiceCommand command);
    Optional<CoachService> handle(UpdateCoachServiceCommand command);
    void handle(DeleteCoachServiceCommand command);
}
