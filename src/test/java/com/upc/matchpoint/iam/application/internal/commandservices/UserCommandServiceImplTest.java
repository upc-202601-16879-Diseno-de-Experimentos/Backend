package com.upc.matchpoint.iam.application.internal.commandservices;

import com.upc.matchpoint.iam.application.internal.outboundservices.hashing.HashingService;
import com.upc.matchpoint.iam.application.internal.outboundservices.tokens.TokenService;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.commands.SignInCommand;
import com.upc.matchpoint.iam.domain.model.commands.SignUpCommand;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Roles.ROLE_USER);
        user = new User("testuser", "hashedpassword", Collections.singletonList(role));
    }

    @Test
    @DisplayName("Debería registrar un usuario cuando el nombre de usuario no existe")
    void handleSignUp_ShouldReturnUser_WhenUsernameDoesNotExist() {
        // unitTest
        // Arrange
        SignUpCommand command = new SignUpCommand("testuser", "password", Collections.emptyList());
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_USER)).thenReturn(Optional.of(role));
        when(hashingService.encode("password")).thenReturn("hashedpassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el nombre de usuario ya existe")
    void handleSignUp_ShouldThrowException_WhenUsernameExists() {
        // unitTest
        // Arrange
        SignUpCommand command = new SignUpCommand("testuser", "password", Collections.emptyList());
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debería retornar un token cuando las credenciales son válidas")
    void handleSignIn_ShouldReturnToken_WhenCredentialsAreValid() {
        // unitTest
        // Arrange
        SignInCommand command = new SignInCommand("testuser", "password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(hashingService.matches("password", "hashedpassword")).thenReturn(true);
        when(tokenService.generateToken("testuser")).thenReturn("testtoken");

        // Act
        var result = userCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getRight()).isEqualTo("testtoken");
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando la contraseña es inválida")
    void handleSignIn_ShouldThrowException_WhenPasswordIsInvalid() {
        // unitTest
        // Arrange
        SignInCommand command = new SignInCommand("testuser", "wrongpassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(hashingService.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }
}
