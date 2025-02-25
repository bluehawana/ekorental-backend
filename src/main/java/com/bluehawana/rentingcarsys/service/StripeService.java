package com.bluehawana.rentingcarsys.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public String createCheckoutSession(Long carId, BigDecimal totalPrice,
                                        LocalDateTime startTime, LocalDateTime endTime) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:3000/payment-success")
            .setCancelUrl("http://localhost:3000/payment-cancel")
            .addLineItem(SessionCreateParams.LineItem.builder()
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("sek")
                    .setUnitAmount(totalPrice.multiply(new BigDecimal("100")).longValue())
                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Car Rental")
                        .build())
                    .build())
                .setQuantity(1L)
                .build())
            .build();

        Session session = Session.create(params);
        return session.getId();
    }

    public void handleWebhookEvent(String payload, String sigHeader) {
        // Implement webhook handling logic
    }
}
