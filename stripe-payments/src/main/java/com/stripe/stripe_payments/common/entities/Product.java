package com.stripe.stripe_payments.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String idProduct;
    private String name;
    private String description;
    private Long price;
    private Date created;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "idUser")
    @JsonIgnore
    private UserModel creator;


}
