package com.upc.matchpoint.courts.application.internal.commandservices;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.domain.model.commands.CreateCourtCommand;
import com.upc.matchpoint.courts.domain.model.commands.DeleteCourtCommand;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
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
class CourtCommandServiceImplTest {

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private CourtCommandServiceImpl courtCommandService;

    private Court court;

    @BeforeEach
    void setUp() {
        court = new Court("Cancha 1", "Sede San Isidro", "Fútbol");
        court.setId(1L);
    }

    @Test
    @DisplayName("Debería crear una cancha cuando el nombre no existe")
    void handleCreateCourt_ShouldReturnCourt_WhenNameDoesNotExist() {
        // unitTest
        // Arrange
        CreateCourtCommand command = new CreateCourtCommand("Cancha 1", "Sede San Isidro", "Fútbol", null, null, null, null, null, null);
        when(courtRepository.existsByName("Cancha 1")).thenReturn(false);
        when(courtRepository.save(any(Court.class))).thenReturn(court);

        // Act
        Optional<Court> result = courtCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Cancha 1");
        verify(courtRepository).save(any(Court.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el nombre de la cancha ya existe")
    void handleCreateCourt_ShouldThrowException_WhenNameExists() {
        // unitTest
        // Arrange
        CreateCourtCommand command = new CreateCourtCommand("Cancha 1", "Sede San Isidro", "Fútbol", null, null, null, null, null, null);
        when(courtRepository.existsByName("Cancha 1")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> courtCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Court with name Cancha 1 already exists");
        verify(courtRepository, never()).save(any(Court.class));
    }

    @Test
    @DisplayName("Debería eliminar la cancha cuando el ID existe")
    void handleDeleteCourt_ShouldDelete_WhenCourtExists() {
        // unitTest
        // Arrange
        DeleteCourtCommand command = new DeleteCourtCommand(1L);
        when(courtRepository.existsById(1L)).thenReturn(true);

        // Act
        courtCommandService.handle(command);

        // Assert
        verify(courtRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el ID de la cancha no existe")
    void handleDeleteCourt_ShouldThrowException_WhenCourtDoesNotExist() {
        // unitTest
        // Arrange
        DeleteCourtCommand command = new DeleteCourtCommand(1L);
        when(courtRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> courtCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Court with id 1 not found");
        verify(courtRepository, never()).deleteById(anyLong());
    }
}
