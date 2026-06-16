package com.upc.matchpoint.coaches.application.internal.commandservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.UpdateCoachCommand;
import com.upc.matchpoint.coaches.domain.services.CoachCommandService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CoachCommandServiceImpl implements CoachCommandService {
    private final CoachRepository coachRepository;
    private final com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository coachServiceRepository;

    public CoachCommandServiceImpl(CoachRepository coachRepository, com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository coachServiceRepository) {
        this.coachRepository = coachRepository;
        this.coachServiceRepository = coachServiceRepository;
    }

    @Override
    public Optional<Coach> handle(CreateCoachCommand command) {
        if (coachRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Coach with name " + command.name() + " already exists");
        }
        var coach = new Coach(command.name(), command.expertise(), command.phone());
        if (command.availability() != null) {
            coach.setAvailability(command.availability());
        }
        var createdCoach = coachRepository.save(coach);
        return Optional.of(createdCoach);
    }

    @Override
    public Optional<Coach> handle(UpdateCoachCommand command) {
        return coachRepository.findById(command.coachId()).map(coachToUpdate -> {
            coachToUpdate.updateCoach(command.name(), command.expertise(), command.phone(), command.availability());
            return coachRepository.save(coachToUpdate);
        });
    }

    @Override
    public void handle(DeleteCoachCommand command) {
        if (!coachRepository.existsById(command.coachId())) {
            throw new IllegalArgumentException("Coach with id " + command.coachId() + " not found");
        }
        coachRepository.deleteById(command.coachId());
    }

    @Override
    public Optional<com.upc.matchpoint.coaches.domain.model.entities.CoachService> handle(com.upc.matchpoint.coaches.domain.model.commands.CreateCoachServiceCommand command) {
        var coach = coachRepository.findById(command.coachId());
        if (coach.isEmpty()) {
            throw new IllegalArgumentException("Coach not found");
        }
        var coachService = new com.upc.matchpoint.coaches.domain.model.entities.CoachService(coach.get(), command.name(), command.description(), command.price());
        var created = coachServiceRepository.save(coachService);
        return Optional.of(created);
    }

    @Override
    public void handle(com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachServiceCommand command) {
        if (!coachServiceRepository.existsById(command.serviceId())) {
            throw new IllegalArgumentException("Service not found");
        }
        coachServiceRepository.deleteById(command.serviceId());
    }
}

