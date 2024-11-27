package com.stripe.stripe_payments.services.Impl;

import com.stripe.stripe_payments.common.dto.*;
import com.stripe.stripe_payments.common.entities.Product;
import com.stripe.stripe_payments.common.entities.UserModel;
import com.stripe.stripe_payments.repositories.ProductRepository;
import com.stripe.stripe_payments.repositories.UserRepository;
import com.stripe.stripe_payments.services.AuthService;
import com.stripe.stripe_payments.services.StripeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public AuthServiceImpl(StripeService stripeService, UserRepository userRepository, ProductRepository productRepository) {
        this.stripeService = stripeService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public AuthResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToEntityUser)
                .map(this::setUserCustomer)
                .map(userRepository::save)
                .map(userModel -> AuthResponse.builder()
                        .customerId(userModel.getCustomerId())
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("Error create user"));
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        return Optional.of(productRequest)
                .map(this:: mapToEntityProduct)
                .map(this::setUserProduct)
                .map(productRepository::save)
                .map(this::setUserProductCreatedProducts)
                .map(product -> ProductResponse.builder().
                        ProductId(product.getIdProduct())
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("Error create product"));
    }

    private Product setUserProductCreatedProducts(Product product) {

        UserModel userMod = product.getCreator();
        userMod.getCreatedProducts().add(product);
        userRepository.save(userMod);
        return product;
    }
    @Override
    public List<Product> getPurchasedProductsByUserId(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error find user"));
        return user.getPurchasedProducts();
    }

    private Product setUserProduct(Product product) {
        var productCreated = stripeService.createProduct(product.getName(), product.getDescription(),true);
        stripeService.createPrice(productCreated.getId(),product.getPrice());
        product.setIdProduct(productCreated.getId());
        return product;
    }

    private Product mapToEntityProduct(ProductRequest productRequest) {
        UserModel user = userRepository.findByCustomerId(productRequest.getCustomerId());
        if (user == null) {
            throw new RuntimeException("User not found with CustomerId: ");
        }
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .created(new Date())
                .creator(user)
                .build();
    }

    private UserModel setUserCustomer(UserModel userModel) {
        var customerCreated = stripeService.createCustomer(userModel.getEmail(),
                userModel.getName());
        userModel.setCustomerId(customerCreated.getId());
        return userModel;
    }



    private UserModel mapToEntityUser(UserRequest userRequest) {
        return  UserModel.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .build();
    }
}
