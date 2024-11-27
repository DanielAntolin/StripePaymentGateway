package com.stripe.stripe_payments.services;

import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.stripe_payments.common.dto.CheckoutRequest;
import com.stripe.stripe_payments.common.dto.CheckoutResponse;

public interface StripeService {
    void manageWebhook(Event event);
    Event constructEvent(String payload, String stripeHeader);
    Customer createCustomer(String email, String name);
    Product createProduct(String name, String description, boolean active);
    Price createPrice(String productId, Long amount);

    CheckoutResponse createCheckout(CheckoutRequest checkoutRequest);
}
