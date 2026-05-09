package com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCoachId(Long coachId);
}

