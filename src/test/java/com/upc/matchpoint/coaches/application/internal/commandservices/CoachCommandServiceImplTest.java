package com.upc.matchpoint.coaches.application.internal.commandservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachCommandServiceImplTest {

    @Mock
    private CoachRepository coachRepository;

    @InjectMocks
    private CoachCommandServiceImpl coachCommandService;

    private Coach coach;

    @BeforeEach
    void setUp() {
        coach = new Coach("Coach Garcia", "Tennis", "987654321");
        coach.setId(1L);
    }

    @Test
    @DisplayName("Debería crear un entrenador cuando el nombre no existe")
    void handleCreateCoach_ShouldReturnCoach_WhenNameDoesNotExist() {
        // unitTest
        // Arrange
        CreateCoachCommand command = new CreateCoachCommand("Coach Garcia", "Tennis", "987654321");
        when(coachRepository.existsByName("Coach Garcia")).thenReturn(false);
        when(coachRepository.save(any(Coach.class))).thenReturn(coach);

        // Act
        Optional<Coach> result = coachCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Coach Garcia");
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el nombre del entrenador ya existe")
    void handleCreateCoach_ShouldThrowException_WhenNameExists() {
        // unitTest
        // Arrange
        CreateCoachCommand command = new CreateCoachCommand("Coach Garcia", "Tennis", "987654321");
        when(coachRepository.existsByName("Coach Garcia")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> coachCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Coach with name Coach Garcia already exists");
        verify(coachRepository, never()).save(any(Coach.class));
    }

    @Test
    @DisplayName("Debería eliminar el entrenador cuando el ID existe")
    void handleDeleteCoach_ShouldDelete_WhenCoachExists() {
        // unitTest
        // Arrange
        DeleteCoachCommand command = new DeleteCoachCommand(1L);
        when(coachRepository.existsById(1L)).thenReturn(true);

        // Act
        coachCommandService.handle(command);

        // Assert
        verify(coachRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el ID del entrenador no existe")
    void handleDeleteCoach_ShouldThrowException_WhenCoachDoesNotExist() {
        // unitTest
        // Arrange
        DeleteCoachCommand command = new DeleteCoachCommand(1L);
        when(coachRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> coachCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Coach with id 1 not found");
        verify(coachRepository, never()).deleteById(anyLong());
    }
}
