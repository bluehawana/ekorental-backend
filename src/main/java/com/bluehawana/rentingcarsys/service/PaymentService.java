package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;

public interface PaymentService {
    void confirmPayment(String paymentIntentId) throws PaymentException;
    void processRefund(String paymentId) throws PaymentException;
    Payment createPaymentIntent(Double amount, String currency);
}
