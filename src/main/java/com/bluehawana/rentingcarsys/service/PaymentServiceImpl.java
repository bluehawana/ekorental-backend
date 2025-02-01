package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;
import com.bluehawana.rentingcarsys.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public void confirmPayment(String paymentIntentId) throws PaymentException {
        if (paymentIntentId == null) {
            throw new PaymentException("Payment intent ID cannot be null");
        }
        // Implementation of payment confirmation
    }

    @Override
    public void processRefund(String paymentId) throws PaymentException {
        if (paymentId == null) {
            throw new PaymentException("Payment ID cannot be null");
        }
        // Implementation of refund processing
    }

    @Override
    public Payment createPaymentIntent(Double amount, String currency) {
        // Implementation of payment intent creation
        return null;
    }
}
