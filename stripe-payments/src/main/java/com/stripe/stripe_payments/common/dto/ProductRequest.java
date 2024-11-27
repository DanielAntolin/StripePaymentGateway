package com.stripe.stripe_payments.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Long price;
    @NotNull
    private String customerId;
}
