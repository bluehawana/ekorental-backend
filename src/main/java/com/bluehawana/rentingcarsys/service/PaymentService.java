package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Payment;

public interface PaymentService {
    String createCheckoutSession(BookingDTO bookingDTO) throws PaymentException;
    void confirmPayment(String sessionId) throws PaymentException;
    void processRefund(String paymentId) throws PaymentException;
    void handleWebhookEvent(String payload, String signature) throws PaymentException;
}