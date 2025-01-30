package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;
import com.bluehawana.rentingcarsys.model.PaymentStatus;
import com.bluehawana.rentingcarsys.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void init() {
        if (stripeSecretKey != null && !stripeSecretKey.isEmpty()) {
            Stripe.apiKey = stripeSecretKey;
        } else {
            throw new IllegalStateException("Stripe secret key not configured");
        }
    }

    @Transactional
    public void confirmPayment(String paymentIntentId) throws PaymentException {
        logger.info("Confirming payment for intent ID: {}", paymentIntentId);
        try {
            // Retrieve the payment intent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Confirm the payment intent
            PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder().build();
            PaymentIntent confirmedIntent = paymentIntent.confirm(params);

            // Save payment record
            Payment payment = new Payment();
            payment.setPaymentIntentId(paymentIntentId);
            payment.setAmount(confirmedIntent.getAmount() / 100.0); // Convert from cents to dollars
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setCurrency(confirmedIntent.getCurrency().toUpperCase());
            payment.setPaymentMethod(confirmedIntent.getPaymentMethod());
            payment.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // Send payment confirmation email
            notificationService.sendPaymentConfirmation(payment.getUserId());

            logger.info("Payment confirmed successfully for intent ID: {}", paymentIntentId);
        } catch (StripeException e) {
            logger.error("Stripe error while confirming payment: {}", e.getMessage());
            throw new PaymentException("Failed to confirm payment: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error confirming payment: {}", e.getMessage());
            throw new PaymentException("Unexpected error during payment confirmation");
        }
    }

    @Transactional
    public void processRefund(String paymentId) throws PaymentException {
        logger.info("Processing refund for payment ID: {}", paymentId);
        try {
            // Retrieve the payment from our database
            Payment payment = (Payment) PaymentRepository.findByPaymentIntentId(paymentId)
                    .orElseThrow(() -> new PaymentException("Payment not found"));

            // Create refund parameters
            Map<String, Object> params = new HashMap<>();
            params.put("payment_intent", paymentId);
            params.put("reason", "requested_by_customer");

            // Create refund through Stripe
            Refund refund = Refund.create(params);

            // Update payment status in our database
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRefundId(refund.getId());
            payment.setRefundedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Send refund confirmation email
            notificationService.sendRefundConfirmation(payment);

            logger.info("Refund processed successfully for payment ID: {}", paymentId);
        } catch (StripeException e) {
            logger.error("Stripe error while processing refund: {}", e.getMessage());
            throw new PaymentException("Failed to process refund: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing refund: {}", e.getMessage());
            throw new PaymentException("Unexpected error during refund processing");
        }
    }

    public Payment createPaymentIntent(Double amount, String currency) throws PaymentException {
        logger.info("Creating payment intent for amount: {} {}", amount, currency);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100)); // Convert to cents
            params.put("currency", currency.toLowerCase());
            params.put("payment_method_types", List.of("card"));

            PaymentIntent intent = PaymentIntent.create(params);

            // Create payment record
            Payment payment = new Payment();
            payment.setPaymentIntentId(intent.getId());
            payment.setClientSecret(intent.getClientSecret());
            payment.setAmount(amount);
            payment.setCurrency(currency);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());

            return paymentRepository.save(payment);
        } catch (StripeException e) {
            logger.error("Stripe error while creating payment intent: {}", e.getMessage());
            throw new PaymentException("Failed to create payment intent: " + e.getMessage());
        }
    }

    public void handleWebhookEvent(String payload, String signatureHeader) throws PaymentException {
        try {
            // Verify webhook signature
            com.stripe.model.Event event = Webhook.constructEvent(
                    payload, signatureHeader, webhookSecret
            );

            // Handle different event types
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed(event);
                    break;
                case "charge.refunded":
                    handleChargeRefunded(event);
                    break;
                default:
                    logger.info("Unhandled event type: {}", event.getType());
            }
        } catch (SignatureVerificationException e) {
            logger.error("Invalid webhook signature: {}", e.getMessage());
            throw new PaymentException("Invalid webhook signature");
        }
    }

    private void handlePaymentIntentSucceeded(com.stripe.model.Event event) throws PaymentException {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        Payment payment = (Payment) PaymentRepository.findByPaymentIntentId(intent.getId())
                .orElseThrow(() -> new PaymentException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    private void handlePaymentIntentFailed(com.stripe.model.Event event) throws PaymentException {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        Payment payment = (Payment) PaymentRepository.findByPaymentIntentId(intent.getId())
                .orElseThrow(() -> new PaymentException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    private void handleChargeRefunded(com.stripe.model.Event event) throws PaymentException {
        Charge charge = (Charge) event.getDataObjectDeserializer().getObject().get();
        Payment payment = (Payment) PaymentRepository.findByPaymentIntentId(charge.getPaymentIntent())
                .orElseThrow(() -> new PaymentException("Payment not found"));

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }
}