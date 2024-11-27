package com.stripe.stripe_payments.common.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;
    private String email;
    private String password;
    private String name;
    private String customerId;
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Product> createdProducts;
    @ManyToMany
    @JoinTable(
            name = "user_purchased_products",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "idProduct")
    )
    private List<Product> purchasedProducts;
}
