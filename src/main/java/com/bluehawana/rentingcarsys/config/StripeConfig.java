package com.bluehawana.rentingcarsys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Configuration
@Getter
public class StripeConfig {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.publishable.key}")
    private String publishableKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void initializeStripe() {
        try {
            Stripe.apiKey = secretKey;
        } catch (Exception e) {
            // Log the error but don't prevent application startup
            System.err.println("Failed to initialize Stripe: " + e.getMessage());
        }
    }
}