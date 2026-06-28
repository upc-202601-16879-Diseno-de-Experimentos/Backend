package com.upc.matchpoint.coaches.application.internal.commandservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.*;
import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import com.upc.matchpoint.coaches.domain.services.CoachCommandService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CoachCommandServiceImpl implements CoachCommandService {
    private final CoachRepository coachRepository;
    private final CoachServiceRepository coachServiceRepository;

    public CoachCommandServiceImpl(CoachRepository coachRepository, CoachServiceRepository coachServiceRepository) {
        this.coachRepository = coachRepository;
        this.coachServiceRepository = coachServiceRepository;
    }

    @Override
    public Optional<Coach> handle(CreateCoachCommand command) {
        if (coachRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Coach with name " + command.name() + " already exists");
        }
        var coach = new Coach(
                command.name(), command.expertise(), command.phone(),
                command.email(), command.sportType(), command.pricePerHour(),
                command.location(), command.description(), command.imageUrl(),
                command.isAvailable(), command.experienceYears()
        );
        if (command.availability() != null) {
            coach.setAvailability(command.availability());
        }
        return Optional.of(coachRepository.save(coach));
    }

    @Override
    public Optional<Coach> handle(UpdateCoachCommand command) {
        return coachRepository.findById(command.coachId()).map(coachToUpdate -> {
            coachToUpdate.updateCoach(
                    command.name(), command.expertise(), command.phone(), command.availability(),
                    command.email(), command.sportType(), command.pricePerHour(), command.location(),
                    command.description(), command.imageUrl(), command.isAvailable(), command.experienceYears()
            );
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
    public Optional<CoachService> handle(CreateCoachServiceCommand command) {
        var coach = coachRepository.findById(command.coachId());
        if (coach.isEmpty()) throw new IllegalArgumentException("Coach not found");
        var coachService = new CoachService(coach.get(), command.name(), command.description(), command.price());
        return Optional.of(coachServiceRepository.save(coachService));
    }

    @Override
    public Optional<CoachService> handle(UpdateCoachServiceCommand command) {
        return coachServiceRepository.findById(command.serviceId()).map(service -> {
            service.setName(command.name());
            service.setDescription(command.description());
            service.setPrice(command.price());
            return coachServiceRepository.save(service);
        });
    }

    @Override
    public void handle(DeleteCoachServiceCommand command) {
        if (!coachServiceRepository.existsById(command.serviceId())) {
            throw new IllegalArgumentException("Service not found");
        }
        coachServiceRepository.deleteById(command.serviceId());
    }
}
