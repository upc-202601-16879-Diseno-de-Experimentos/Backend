package com.upc.matchpoint.coaches.domain.model.queries;

public record SearchCoachesQuery(String sportType, String location, Double maxPrice, Double minRating) {
}
