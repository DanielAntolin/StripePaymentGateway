package com.stripe.stripe_payments.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {
    @NotNull
    private String customerId;
    @NotNull
    private String productID;
}
