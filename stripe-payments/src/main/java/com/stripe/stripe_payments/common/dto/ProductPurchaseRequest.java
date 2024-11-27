package com.stripe.stripe_payments.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPurchaseRequest {
    @NotNull
    private String name;

}
