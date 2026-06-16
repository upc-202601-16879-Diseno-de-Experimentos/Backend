package com.upc.matchpoint.users.application.internal.commandservices;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.domain.model.commands.CreateUserProfileCommand;
import com.upc.matchpoint.users.domain.model.commands.DeleteUserProfileCommand;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
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
class UserProfileCommandServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileCommandServiceImpl userProfileCommandService;

    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile("John Doe", "john@example.com", "123456789");
        userProfile.setId(1L);
    }

    @Test
    @DisplayName("Debería crear un perfil de usuario cuando el email no existe")
    void handleCreateUserProfile_ShouldReturnUserProfile_WhenEmailDoesNotExist() {
        // unitTest
        // Arrange
        CreateUserProfileCommand command = new CreateUserProfileCommand("John Doe", "john@example.com", "123456789");
        when(userProfileRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // Act
        Optional<UserProfile> result = userProfileCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el email del perfil ya existe")
    void handleCreateUserProfile_ShouldThrowException_WhenEmailExists() {
        // unitTest
        // Arrange
        CreateUserProfileCommand command = new CreateUserProfileCommand("John Doe", "john@example.com", "123456789");
        when(userProfileRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userProfileCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with email john@example.com already exists");
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Debería eliminar el perfil de usuario cuando el ID existe")
    void handleDeleteUserProfile_ShouldDelete_WhenUserExists() {
        // unitTest
        // Arrange
        DeleteUserProfileCommand command = new DeleteUserProfileCommand(1L);
        when(userProfileRepository.existsById(1L)).thenReturn(true);

        // Act
        userProfileCommandService.handle(command);

        // Assert
        verify(userProfileRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el ID del perfil de usuario no existe")
    void handleDeleteUserProfile_ShouldThrowException_WhenUserDoesNotExist() {
        // unitTest
        // Arrange
        DeleteUserProfileCommand command = new DeleteUserProfileCommand(1L);
        when(userProfileRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userProfileCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with id 1 not found");
        verify(userProfileRepository, never()).deleteById(anyLong());
    }
}
