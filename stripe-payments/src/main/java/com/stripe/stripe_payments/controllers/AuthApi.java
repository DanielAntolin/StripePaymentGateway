package com.stripe.stripe_payments.controllers;

import com.stripe.stripe_payments.common.constants.ApiPathCostants;
import com.stripe.stripe_payments.common.dto.*;
import com.stripe.stripe_payments.common.entities.Product;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ApiPathCostants.V1_ROUTE + ApiPathCostants.AUTH_ROUTE)
public interface AuthApi {
    @PostMapping("/createUser")
    ResponseEntity<AuthResponse> createUser(@RequestBody @Valid UserRequest userRequest);
    @PostMapping("/createProduct")
    ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest);
    @GetMapping("{idUser}"+"/acquired")
    public ResponseEntity<List<Product>> getAcquiredProducts(@PathVariable Long idUser);

}
