package com.stripe.stripe_payments.services.Impl;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceListParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.stripe_payments.common.dto.CheckoutRequest;
import com.stripe.stripe_payments.common.dto.CheckoutResponse;
import com.stripe.stripe_payments.services.StripeService;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeServiceImpl implements StripeService {
    private final String endpointSecret;
    private final List<StripeStrategy> stripeStrategies;

    public StripeServiceImpl(@Value("${stripe.endpoint.secret}")String endpointSecret, List<StripeStrategy> stripeStrategies,
                             @Value("${stripe.secret.key}") String stripeKey) {
        Stripe.apiKey = stripeKey;
        this.endpointSecret = endpointSecret;
        this.stripeStrategies = stripeStrategies;
    }

    @Override
    public void manageWebhook(Event event) {
        Optional.of(event)
                .map(this::processStrategy);
    }

    private Event processStrategy(Event event) {
    return stripeStrategies.stream()
            .filter(stripeStrategy -> stripeStrategy.isApllicable(event))
            .findFirst()
            .map(stripeStrategy -> stripeStrategy.process(event))
            .orElseGet(Event::new);

    }

    @Override
    public Event constructEvent(String payload, String stripeHeader) {
        try {
            return Webhook.constructEvent(payload, stripeHeader, endpointSecret);
        } catch (SignatureVerificationException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Customer createCustomer(String email, String name) {
        var customerCreateParams = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
        try {
            return Customer.create(customerCreateParams);
        } catch(StripeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Product createProduct(String name, String description,boolean active) {
        var productCreateParams = ProductCreateParams.builder()
                .setName(name)
                .setDescription(description)
                .setActive(active)
                .setType(ProductCreateParams.Type.SERVICE)
                .build();
        try {
            return Product.create(productCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Price createPrice(String productId, Long amount) {
        var createPrice = PriceCreateParams.builder()
                .setCurrency("eur")
                .setProduct(productId)
                .setUnitAmount(amount)
                .build();
        try {
            return Price.create(createPrice);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CheckoutResponse createCheckout(CheckoutRequest checkoutRequest) {
        var priceId = getPriceIdForProduct(checkoutRequest.getProductID());
        var session = SessionCreateParams.builder()
                .setCustomer(checkoutRequest.getCustomerId())
                .setSuccessUrl("http://localhost:8080")
                .setCancelUrl("http://localhost:8080")
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build()
                )
                .putExtraParam("metadata", extraMetaData(checkoutRequest.getProductID()))
                .build();
        try {
            return Optional.of(Session.create(session))
                    .map(sessionCreated -> CheckoutResponse.builder()
                            .urlPayment(sessionCreated.getUrl())
                            .build()
                    )
                    .orElseThrow(() -> new RuntimeException("Error checking out session"));
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String,Object> extraMetaData(String productID) {
        Map<String,Object> metaData = new HashMap<>();
        metaData.put("product_id",productID);
        return metaData;
    }

    private String getPriceIdForProduct(String productID) {
        List<Price> prices = null;
        try {
            prices = Price.list(PriceListParams.builder().setProduct(productID).build()).getData();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return prices.stream()
                .findFirst()
                .map(Price::getId)
                .orElseThrow(() -> new RuntimeException("No price found for product: " + productID));
    }
}
