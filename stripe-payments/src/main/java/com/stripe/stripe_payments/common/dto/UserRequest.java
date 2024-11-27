package com.stripe.stripe_payments.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
}
