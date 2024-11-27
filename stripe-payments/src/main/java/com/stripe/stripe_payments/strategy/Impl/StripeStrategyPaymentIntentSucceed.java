package com.stripe.stripe_payments.strategy.Impl;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.stripe_payments.common.entities.Payment;
import com.stripe.stripe_payments.common.enums.StripeEventEnum;
import com.stripe.stripe_payments.repositories.PaymentRepository;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StripeStrategyPaymentIntentSucceed implements StripeStrategy {
    private  final  PaymentRepository paymentRepository ;


    public StripeStrategyPaymentIntentSucceed(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public boolean isApllicable(Event event) {
        return StripeEventEnum.PAYMENT_INTENT_SUCCEEDED.value.equals(event.getType());
    }

    @Override
    public Event process(Event event) {
        return Optional.of(event)
                .map(this::deserialize)
                .map(this::mapToEntity)
                .map(payment -> {
                    System.out.println("Payment entity to be saved: " + payment);
                    return paymentRepository.save(payment);
                })
                .map(given -> event)
                .orElseThrow(() -> new RuntimeException("Stripe payment intent is not applicable"));
    }

    private Payment mapToEntity(PaymentIntent paymentIntent) {

        return Payment.builder()
                .paymentIntentId(paymentIntent.getId())

                .customerId(paymentIntent.getCustomer())
                .amount(paymentIntent.getAmount())
                .currency(paymentIntent.getCurrency())
                .type(StripeEventEnum.PAYMENT_INTENT_SUCCEEDED.value)
                .build();
    }

    private PaymentIntent deserialize(Event event) {
        return (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new RuntimeException("Error deserializing payment intent data"));
    }
}
