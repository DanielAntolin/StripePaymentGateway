package com.stripe.stripe_payments.common.entities;

import com.stripe.stripe_payments.common.enums.StripeEventEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String paymentIntentId;
    private String customerId;
    private String productId;
    private Long amount;
    private String currency;
   private String type;

    @Override
    public String toString() {
        return "Payment{" +
                "paymentIntentId='" + paymentIntentId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", type=" + type +
                '}';
    }
}
