package com.stripe.stripe_payments.controllers.Impl;

import com.stripe.stripe_payments.common.dto.*;
import com.stripe.stripe_payments.common.entities.Product;
import com.stripe.stripe_payments.controllers.AuthApi;
import com.stripe.stripe_payments.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<AuthResponse> createUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.createUser(userRequest));
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        return ResponseEntity.ok(authService.createProduct(productRequest));
    }

    @Override
    public ResponseEntity<List<Product>> getAcquiredProducts(Long idUser) {
        return ResponseEntity.ok(authService.getPurchasedProductsByUserId(idUser));
    }
}
