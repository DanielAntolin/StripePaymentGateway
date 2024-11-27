package com.stripe.stripe_payments.repositories;

import com.stripe.stripe_payments.common.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentIntentId(String paymentId);
}
