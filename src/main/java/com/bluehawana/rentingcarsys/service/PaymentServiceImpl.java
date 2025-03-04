package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends PaymentService {
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
            ).toString();
        } catch (Exception e) {
            log.error("Payment creation failed", e);
            throw new PaymentException("Failed to create checkout session");
        }
    }

    @Override
    public void confirmPayment(String sessionId) throws PaymentException {
        try {
            // Implement payment confirmation logic
        } catch (Exception e) {
            throw new PaymentException("Payment confirmation failed");
        }
    }

    @Override
    public void processRefund(String paymentId) throws PaymentException {
        try {
            // Implement refund logic
        } catch (Exception e) {
            throw new PaymentException("Refund processing failed");
        }
    }

    @Override
    public void handleWebhookEvent(String payload, String signature) throws PaymentException {
        try {
            stripeService.handleWebhookEvent(payload, signature);
        } catch (Exception e) {
            throw new PaymentException("Webhook processing failed");
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        return List.of();
    }

    @Override
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return List.of();
    }

    @Override
    public Payment getPaymentById(Long id) {
        return null;
    }
}
