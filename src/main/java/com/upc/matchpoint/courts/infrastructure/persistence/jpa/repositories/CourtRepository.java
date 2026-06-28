package com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Court c WHERE " +
           "(:sportType IS NULL OR LOWER(c.sportType) = LOWER(:sportType)) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:maxPrice IS NULL OR c.pricePerHour <= :maxPrice)")
    List<Court> searchCourts(@Param("sportType") String sportType,
                              @Param("location") String location,
                              @Param("maxPrice") Double maxPrice);
}
