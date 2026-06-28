package com.upc.matchpoint.coaches.domain.services;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import com.upc.matchpoint.coaches.domain.model.queries.GetAllCoachesQuery;
import com.upc.matchpoint.coaches.domain.model.queries.GetCoachByIdQuery;
import com.upc.matchpoint.coaches.domain.model.queries.GetCoachServicesByCoachIdQuery;
import com.upc.matchpoint.coaches.domain.model.queries.SearchCoachesQuery;
import java.util.List;
import java.util.Optional;

public interface CoachQueryService {
    List<Coach> handle(GetAllCoachesQuery query);
    Optional<Coach> handle(GetCoachByIdQuery query);
    List<CoachService> handle(GetCoachServicesByCoachIdQuery query);
    List<Coach> handle(SearchCoachesQuery query);
}
