package com.upc.matchpoint.bookings.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.bookings.interfaces.rest.resources.CreateBookingResource;
import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CoachBookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CoachRepository coachRepository;

    private UserProfile user;
    private Court court;
    private Coach coach;

    @BeforeEach
    void setUp() {
        // Arrange
        user = userProfileRepository.save(new UserProfile("Player 1", "player@test.com", "987654321"));
        court = courtRepository.save(new Court("Central Court", "Club X", "Paddle"));
        coach = coachRepository.save(new Coach("Coach Mike", "Paddle Specialist", "999000111"));
    }

    @Test
    @DisplayName("Debería retornar las reservas asociadas a un entrenador específico")
    void getBookingsByCoachId_ShouldReturnCoachBookings() throws Exception {
        // integrationTest

        // Arrange: Crear una reserva con coach
        LocalDateTime startTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endTime = startTime.plusHours(1);
        CreateBookingResource resource = new CreateBookingResource(startTime, endTime, user.getId(), court.getId(), coach.getId());

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        // Act & Assert: Consultar las reservas del coach
        mockMvc.perform(get("/api/v1/bookings/coach/" + coach.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.id").value(user.getId()))
                .andExpect(jsonPath("$[0].court.id").value(court.getId()));
    }
}
