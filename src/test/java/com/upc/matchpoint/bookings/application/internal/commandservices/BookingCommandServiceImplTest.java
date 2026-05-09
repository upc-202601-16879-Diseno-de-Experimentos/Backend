package com.upc.matchpoint.bookings.application.internal.commandservices;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.commands.CreateBookingCommand;
import com.upc.matchpoint.bookings.domain.model.commands.DeleteBookingCommand;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingCommandServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository coachRepository;

    @InjectMocks
    private BookingCommandServiceImpl bookingCommandService;

    private UserProfile user;
    private Court court;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new UserProfile("John Doe", "john@example.com", "123456789");
        user.setId(1L);
        court = new Court("Main Court", "Central Park", "Tennis");
        court.setId(1L);
        booking = new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), user, court);
    }

    @Test
    @DisplayName("Debería crear una reserva cuando el usuario y la cancha existen")
    void handleCreateBooking_ShouldReturnBooking_WhenUserAndCourtExist() {
        // unitTest
        // Arrange
        CreateBookingCommand command = new CreateBookingCommand(LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, 1L, null);
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courtRepository.findById(1L)).thenReturn(Optional.of(court));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Optional<Booking> result = bookingCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getName()).isEqualTo("John Doe");
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el usuario no existe")
    void handleCreateBooking_ShouldThrowException_WhenUserDoesNotExist() {
        // unitTest
        // Arrange
        CreateBookingCommand command = new CreateBookingCommand(LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, 1L, null);
        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookingCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with id 1 not found");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Debería eliminar la reserva cuando existe")
    void handleDeleteBooking_ShouldDelete_WhenBookingExists() {
        // unitTest
        // Arrange
        DeleteBookingCommand command = new DeleteBookingCommand(1L);
        when(bookingRepository.existsById(1L)).thenReturn(true);

        // Act
        bookingCommandService.handle(command);

        // Assert
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando la reserva no existe")
    void handleDeleteBooking_ShouldThrowException_WhenBookingDoesNotExist() {
        // unitTest
        // Arrange
        DeleteBookingCommand command = new DeleteBookingCommand(1L);
        when(bookingRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> bookingCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking with id 1 not found");
        verify(bookingRepository, never()).deleteById(anyLong());
    }
}
