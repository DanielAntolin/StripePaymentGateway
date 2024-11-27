package com.stripe.stripe_payments.repositories;

import com.stripe.stripe_payments.common.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByidProduct(String id);

}
