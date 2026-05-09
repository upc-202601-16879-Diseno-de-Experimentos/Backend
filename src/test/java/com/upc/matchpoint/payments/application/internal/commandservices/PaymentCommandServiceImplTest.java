package com.upc.matchpoint.payments.application.internal.commandservices;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.domain.model.commands.CreatePaymentCommand;
import com.upc.matchpoint.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    private UserProfile user;
    private Payment payment;

    @BeforeEach
    void setUp() {
        user = new UserProfile("John Doe", "john@example.com", "123456789");
        user.setId(1L);
        payment = new Payment(new BigDecimal("50.00"), user);
        payment.setId(1L);
    }

    @Test
    @DisplayName("Debería registrar un pago cuando el usuario existe")
    void handleCreatePayment_ShouldReturnPayment_WhenUserExists() {
        // unitTest
        // Arrange
        CreatePaymentCommand command = new CreatePaymentCommand(new BigDecimal("50.00"), 1L);
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Optional<Payment> result = paymentCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(new BigDecimal("50.00"));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el usuario no existe al registrar un pago")
    void handleCreatePayment_ShouldThrowException_WhenUserDoesNotExist() {
        // unitTest
        // Arrange
        CreatePaymentCommand command = new CreatePaymentCommand(new BigDecimal("50.00"), 1L);
        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with id 1 not found");
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
