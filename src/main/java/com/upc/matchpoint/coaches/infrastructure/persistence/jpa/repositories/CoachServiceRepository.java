package com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachServiceRepository extends JpaRepository<CoachService, Long> {
    List<CoachService> findByCoachId(Long coachId);
}
