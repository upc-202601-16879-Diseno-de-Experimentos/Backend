package com.upc.matchpoint.coaches.application.internal.queryservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import com.upc.matchpoint.coaches.domain.model.queries.*;
import com.upc.matchpoint.coaches.domain.services.CoachQueryService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CoachQueryServiceImpl implements CoachQueryService {
    private final CoachRepository coachRepository;
    private final CoachServiceRepository coachServiceRepository;

    public CoachQueryServiceImpl(CoachRepository coachRepository, CoachServiceRepository coachServiceRepository) {
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
    public List<CoachService> handle(GetCoachServicesByCoachIdQuery query) {
        return coachServiceRepository.findByCoachId(query.coachId());
    }

    @Override
    public List<Coach> handle(SearchCoachesQuery query) {
        return coachRepository.searchCoaches(query.sportType(), query.location(), query.maxPrice(), query.minRating());
    }
}
