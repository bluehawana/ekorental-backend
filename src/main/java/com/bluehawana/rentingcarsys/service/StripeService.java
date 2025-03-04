package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentListParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private BookingServiceImpl bookingService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Map<String, String> createCheckoutSession(String bookingId, Long amount) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?booking_id=" + bookingId)
                .setCancelUrl(cancelUrl + "?booking_id=" + bookingId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("sek")
                                .setUnitAmount(amount) // amount should be in cents
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Car Booking #" + bookingId)
                                        .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .putMetadata("bookingId", bookingId)
                .build();

        Session session = Session.create(params);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("url", session.getUrl());
        return responseData;
    }

    public void handleWebhookEvent(String payload, String sigHeader) throws StripeException {
        Event event = Webhook.constructEvent(payload, sigHeader, stripeSecretKey);

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) {
                log.error("Failed to deserialize webhook event data");
                throw new PaymentException("Invalid webhook event data", null, null, 0, null);
            }

            String bookingId = session.getMetadata().get("bookingId");

            try {
                Long bookingIdLong = Long.parseLong(bookingId);
                bookingService.confirmBooking(bookingIdLong);
                log.info("Booking {} confirmed after successful payment", bookingId);
            } catch (Exception e) {
                log.error("Failed to confirm booking {}: {}", bookingId, e.getMessage());
                throw new PaymentException("Failed to update booking status", null, null, 0, e);
            }
        }
    }

    public List<PaymentIntent> getPaymentHistory() throws StripeException {
        PaymentIntentListParams params = PaymentIntentListParams.builder()
                .setLimit(100L) // adjust the limit as needed
                .build();

        return PaymentIntent.list(params).getData();
    }

    public Object createCheckoutSession(Long carId, BigDecimal totalPrice, LocalDateTime startTime, LocalDateTime endTime) throws StripeException {
        return createCheckoutSession(String.valueOf(carId), totalPrice.longValue());
    }
}