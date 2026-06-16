package com.upc.matchpoint.coaches.application.internal.queryservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.queries.GetAllCoachesQuery;
import com.upc.matchpoint.coaches.domain.model.queries.GetCoachByIdQuery;
import com.upc.matchpoint.coaches.domain.services.CoachQueryService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CoachQueryServiceImpl implements CoachQueryService {
    private final CoachRepository coachRepository;
    private final com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository coachServiceRepository;

    public CoachQueryServiceImpl(CoachRepository coachRepository, com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository coachServiceRepository) {
        this.coachRepository = coachRepository;
        this.coachServiceRepository = coachServiceRepository;
    }

    @Override
    public List<Coach> handle(GetAllCoachesQuery query) {
        return coachRepository.findAll();
    }

    @Override
    public Optional<Coach> handle(GetCoachByIdQuery query) {
        return coachRepository.findById(query.coachId());
    }

    @Override
    public List<com.upc.matchpoint.coaches.domain.model.entities.CoachService> handle(com.upc.matchpoint.coaches.domain.model.queries.GetCoachServicesByCoachIdQuery query) {
        return coachServiceRepository.findByCoachId(query.coachId());
    }
}

