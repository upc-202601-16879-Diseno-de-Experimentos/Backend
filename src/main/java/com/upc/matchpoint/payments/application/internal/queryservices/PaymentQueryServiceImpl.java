package com.upc.matchpoint.payments.application.internal.queryservices;

import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.domain.model.queries.GetAllPaymentsQuery;
import com.upc.matchpoint.payments.domain.model.queries.GetPaymentByIdQuery;
import com.upc.matchpoint.payments.domain.model.queries.GetPaymentsByCoachIdQuery;
import com.upc.matchpoint.payments.domain.model.valueobjects.PaymentStatus;
import com.upc.matchpoint.payments.domain.services.PaymentQueryService;
import com.upc.matchpoint.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }

    @Override
    public List<Payment> handle(GetAllPaymentsQuery query) {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> handle(GetPaymentsByCoachIdQuery query) {
        var bookings = bookingRepository.findAllByCoachServiceCoachId(query.coachId());
        return bookings.stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()) || "COMPLETED".equalsIgnoreCase(b.getStatus()))
                .map(b -> {
                    var payment = new Payment(
                            BigDecimal.valueOf(b.getAmount() != null ? b.getAmount() : 0.0),
                            b.getUser()
                    );
                    payment.setId(b.getId());
                    payment.setCreatedAt(b.getCreatedAt() != null ? b.getCreatedAt() : b.getStartTime());
                    payment.setPaymentStatus(PaymentStatus.COMPLETED);
                    return payment;
                })
                .toList();
    }
}
