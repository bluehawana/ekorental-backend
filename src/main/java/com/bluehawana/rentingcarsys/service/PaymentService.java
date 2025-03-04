package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.PaymentDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class PaymentService {

    @Autowired
    private StripeService stripeService;

    public List<PaymentDTO> getPaymentHistory() throws StripeException {
        List<PaymentIntent> paymentIntents = stripeService.getPaymentHistory();
        return paymentIntents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO convertToDTO(PaymentIntent paymentIntent) {
        return PaymentDTO.builder()
                .id(paymentIntent.getId())
                .amount(paymentIntent.getAmount().longValue())
                .status(paymentIntent.getStatus())
                .createdAt(Instant.ofEpochSecond(paymentIntent.getCreated()).atOffset(ZoneOffset.UTC).toLocalDateTime())
                .build();
    }

    public abstract String createCheckoutSession(BookingDTO bookingDTO) throws PaymentException;

    public abstract void confirmPayment(String sessionId) throws PaymentException;

    public abstract void processRefund(String paymentId) throws PaymentException;

    public abstract void handleWebhookEvent(String payload, String signature) throws PaymentException;

    public abstract List<Payment> getAllPayments();

    public abstract List<Payment> getPaymentsByBookingId(Long bookingId);

    public abstract Payment getPaymentById(Long id);
}