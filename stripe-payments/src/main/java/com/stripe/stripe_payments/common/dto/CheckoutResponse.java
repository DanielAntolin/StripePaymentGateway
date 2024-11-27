package com.stripe.stripe_payments.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutResponse {
    private String urlPayment;
}
