package com.upc.matchpoint.bookings.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.bookings.interfaces.rest.resources.CreateBookingResource;
import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CourtRepository courtRepository;

    private UserProfile user;
    private Court court;

    @BeforeEach
    void setUp() {
        // Limpiar base de datos para asegurar aislamiento
        bookingRepository.deleteAll();
        userProfileRepository.deleteAll();
        courtRepository.deleteAll();

        // Arrange: Preparar datos base en la BD
        user = new UserProfile("Integration User", "test@integration.com", "999888777");
        user = userProfileRepository.save(user);

        court = new Court("Cancha de Integración", "Sede Norte", "Tennis");
        court = courtRepository.save(court);
    }

    @Test
    @DisplayName("Debería crear una reserva exitosamente a través del API")
    void createBooking_ShouldReturnCreatedStatusAndPersistData() throws Exception {
        // integrationTest
        
        // Arrange
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);
        CreateBookingResource resource = new CreateBookingResource(startTime, endTime, user.getId(), court.getId(), null);

        // Act
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        // Assert
        var bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(1);
        Booking savedBooking = bookings.get(0);
        assertThat(savedBooking.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedBooking.getCourt().getId()).isEqualTo(court.getId());
    }
}
