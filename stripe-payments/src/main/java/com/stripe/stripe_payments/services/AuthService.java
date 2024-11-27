package com.stripe.stripe_payments.services;

import com.stripe.stripe_payments.common.dto.*;
import com.stripe.stripe_payments.common.entities.Product;

import java.util.List;

public interface AuthService {
    AuthResponse createUser(UserRequest userRequest);
    ProductResponse createProduct(ProductRequest productRequest);
    List<Product> getPurchasedProductsByUserId(Long userId);

}
