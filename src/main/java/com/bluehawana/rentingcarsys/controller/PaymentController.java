package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.PaymentDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/history")
    public ResponseEntity<List<PaymentDTO>> getPaymentHistory() {
        try {
            List<PaymentDTO> paymentHistory = paymentService.getPaymentHistory();
            return ResponseEntity.ok(paymentHistory);
        } catch (PaymentException | StripeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getPaymentHistory();
            return ResponseEntity.ok(payments);
        } catch (PaymentException e) {
            return ResponseEntity.status(500).body(null);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}