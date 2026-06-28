package com.upc.matchpoint.bookings.domain.services;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.queries.GetAllBookingsQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingByIdQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingsByCoachIdQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    List<Booking> handle(GetAllBookingsQuery query);
    Optional<Booking> handle(GetBookingByIdQuery query);
    List<Booking> handle(GetBookingsByCoachIdQuery query);
    List<Booking> handle(GetBookingsByUserIdQuery query);
}
