package com.stripe.stripe_payments.strategy.Impl;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.stripe_payments.common.entities.Payment;
import com.stripe.stripe_payments.common.entities.Product;
import com.stripe.stripe_payments.common.entities.UserModel;
import com.stripe.stripe_payments.common.enums.StripeEventEnum;
import com.stripe.stripe_payments.repositories.PaymentRepository;
import com.stripe.stripe_payments.repositories.ProductRepository;
import com.stripe.stripe_payments.repositories.UserRepository;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StripeStrategyCheckoutSessionCompleted implements StripeStrategy {
    private final PaymentRepository paymentRepository;
    private  final UserRepository userRepository;
    private final ProductRepository productRepository;

    public StripeStrategyCheckoutSessionCompleted(PaymentRepository paymentRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean isApllicable(Event event) {
        return StripeEventEnum.CHECKOUT_SESSION_COMPLETED.value.equals(event.getType());
    }

    @Override
    public Event process(Event event) {
        var session = this.desrialize(event);
        return Optional.of(event)
                .map(giventevent -> paymentRepository.findByPaymentIntentId(session.getPaymentIntent()))
                .map(payment -> setProductId(payment, session.getMetadata().get("product_id")))
                .map(paymentRepository::save)
                .map(this::setPurchaseProduct)
                .map(given -> event)
                .orElseThrow(() -> new RuntimeException("Error processing"));
    }

    private Payment setPurchaseProduct(Payment payment) {
        try {
            UserModel user = userRepository.findByCustomerId(payment.getCustomerId());
            if (user == null) {
                throw new RuntimeException("User not found with CustomerId: " + payment.getCustomerId());
            }

            Product product = productRepository.findByidProduct(payment.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with ProductId: " + payment.getProductId());
            }

            user.getPurchasedProducts().add(product);
            userRepository.save(user);

        } catch (RuntimeException e) {
            System.out.println("Error in setPurchaseProduct: " + e.getMessage());
            throw e;
        }

        return payment;

    }

    private Payment setProductId(Payment payment, String productId) {
        payment.setProductId(productId);
        payment.setType(StripeEventEnum.CHECKOUT_SESSION_COMPLETED.value);
        return payment;
    }

    private Session desrialize(Event event) {
        return (Session) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new RuntimeException("Error decoding session data"));
    }
}
