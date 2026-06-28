package com.upc.matchpoint.payments.domain.services;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.domain.model.queries.GetAllPaymentsQuery;
import com.upc.matchpoint.payments.domain.model.queries.GetPaymentByIdQuery;
import com.upc.matchpoint.payments.domain.model.queries.GetPaymentsByCoachIdQuery;
import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);
    List<Payment> handle(GetAllPaymentsQuery query);
    List<Payment> handle(GetPaymentsByCoachIdQuery query);
}
