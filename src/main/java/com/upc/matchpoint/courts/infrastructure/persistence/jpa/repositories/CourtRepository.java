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

    @Query(value = "SELECT * FROM courts c WHERE " +
           "(:sportType IS NULL OR LOWER(c.sport_type) = LOWER(:sportType)) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:maxPrice IS NULL OR c.price_per_hour <= :maxPrice) " +
           "ORDER BY CASE WHEN :userLat IS NOT NULL AND :userLon IS NOT NULL AND c.latitude IS NOT NULL AND c.longitude IS NOT NULL THEN " +
           "(6371 * acos(cos(radians(CAST(:userLat AS DOUBLE PRECISION))) * cos(radians(c.latitude)) * cos(radians(c.longitude) - radians(CAST(:userLon AS DOUBLE PRECISION))) + sin(radians(CAST(:userLat AS DOUBLE PRECISION))) * sin(radians(c.latitude)))) " +
           "ELSE 0 END ASC", nativeQuery = true)
    List<Court> searchCourts(@Param("sportType") String sportType,
                              @Param("location") String location,
                              @Param("maxPrice") Double maxPrice,
                              @Param("userLat") Double userLat,
                              @Param("userLon") Double userLon);
}
