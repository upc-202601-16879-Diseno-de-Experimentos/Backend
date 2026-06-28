package com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Coach c WHERE " +
           "(:sportType IS NULL OR LOWER(c.sportType) = LOWER(:sportType)) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:maxPrice IS NULL OR c.pricePerHour <= :maxPrice) AND " +
           "(:minRating IS NULL OR c.rating >= :minRating)")
    List<Coach> searchCoaches(@Param("sportType") String sportType,
                               @Param("location") String location,
                               @Param("maxPrice") Double maxPrice,
                               @Param("minRating") Double minRating);
}
