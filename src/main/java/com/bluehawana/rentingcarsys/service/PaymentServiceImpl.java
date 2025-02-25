package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;
import com.bluehawana.rentingcarsys.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final StripeService stripeService;
    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public String createCheckoutSession(BookingDTO bookingDTO) throws PaymentException {
        try {
            return stripeService.createCheckoutSession(
                    bookingDTO.getCarId(),
                    bookingDTO.getTotalPrice(),
                    bookingDTO.getStartTime(),
                    bookingDTO.getEndTime()
            );
        } catch (Exception e) {
            log.error("Payment creation failed", e);
            throw new PaymentException("Failed to create checkout session", e);
        }
    }

    @Override
    public void confirmPayment(String sessionId) throws PaymentException {
        try {
            // Implement payment confirmation logic
        } catch (Exception e) {
            throw new PaymentException("Payment confirmation failed", e);
        }
    }

    @Override
    public void processRefund(String paymentId) throws PaymentException {
        try {
            // Implement refund logic
        } catch (Exception e) {
            throw new PaymentException("Refund processing failed", e);
        }
    }

    @Override
    public void handleWebhookEvent(String payload, String signature) throws PaymentException {
        try {
            stripeService.handleWebhookEvent(payload, signature);
        } catch (Exception e) {
            throw new PaymentException("Webhook processing failed", e);
        }
    }
}
