package com.upc.matchpoint.courts.domain.model.queries;

public record SearchCourtsQuery(String sportType, String location, Double maxPrice) {
}
