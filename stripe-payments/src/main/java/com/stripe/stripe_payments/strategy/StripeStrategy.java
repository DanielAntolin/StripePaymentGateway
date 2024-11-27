package com.stripe.stripe_payments.strategy;

import com.stripe.model.Event;

public interface StripeStrategy {
    boolean isApllicable(Event event);
    Event process(Event event);
}
