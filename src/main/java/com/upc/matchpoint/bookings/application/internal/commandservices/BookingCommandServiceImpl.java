package com.upc.matchpoint.bookings.application.internal.commandservices;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.commands.CreateBookingCommand;
import com.upc.matchpoint.bookings.domain.model.commands.DeleteBookingCommand;
import com.upc.matchpoint.bookings.domain.model.commands.UpdateBookingCommand;
import com.upc.matchpoint.bookings.domain.services.BookingCommandService;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BookingCommandServiceImpl implements BookingCommandService {
    private final BookingRepository bookingRepository;
    private final UserProfileRepository userProfileRepository;
    private final CourtRepository courtRepository;
    private final CoachRepository coachRepository;

    public BookingCommandServiceImpl(BookingRepository bookingRepository, UserProfileRepository userProfileRepository, CourtRepository courtRepository, CoachRepository coachRepository) {
        this.bookingRepository = bookingRepository;
        this.userProfileRepository = userProfileRepository;
        this.courtRepository = courtRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    public Optional<Booking> handle(CreateBookingCommand command) {
        var user = userProfileRepository.findById(command.userId()).orElseThrow(() -> new IllegalArgumentException("User with id " + command.userId() + " not found"));
        var court = courtRepository.findById(command.courtId()).orElseThrow(() -> new IllegalArgumentException("Court with id " + command.courtId() + " not found"));
        
        var booking = new Booking(command.startTime(), command.endTime(), user, court);
        
        if (command.coachId() != null) {
            var coach = coachRepository.findById(command.coachId())
                    .orElseThrow(() -> new IllegalArgumentException("Coach with id " + command.coachId() + " not found"));
            booking.setCoach(coach);
        }

        var createdBooking = bookingRepository.save(booking);
        return Optional.of(createdBooking);
    }

    @Override
    public Optional<Booking> handle(UpdateBookingCommand command) {
        return bookingRepository.findById(command.bookingId()).map(bookingToUpdate -> {
            bookingToUpdate.updateBooking(command.startTime(), command.endTime());
            return bookingRepository.save(bookingToUpdate);
        });
    }

    @Override
    public void handle(DeleteBookingCommand command) {
        if (!bookingRepository.existsById(command.bookingId())) {
            throw new IllegalArgumentException("Booking with id " + command.bookingId() + " not found");
        }
        bookingRepository.deleteById(command.bookingId());
    }
}

