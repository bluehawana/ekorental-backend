package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.exception.ErrorResponse;
import com.bluehawana.rentingcarsys.service.BookingService;
import com.bluehawana.rentingcarsys.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.bluehawana.rentingcarsys.controller.BookingController.log;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;
    private final BookingService bookingService;
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody BookingDTO bookingDTO) {
        try {
            // Create Stripe session
            String sessionId = stripeService.createCheckoutSession(
                    bookingDTO.getCarId(),
                    bookingDTO.getTotalPrice(),
                    bookingDTO.getStartTime(),
                    bookingDTO.getEndTime()
            );

            return ResponseEntity.ok(Collections.singletonMap("sessionId", sessionId));
        } catch (Exception e) {
            log.error("Error creating checkout session: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to create checkout session"));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(@RequestBody String payload,
                                                 @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            stripeService.handleWebhookEvent(payload, sigHeader);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error handling webhook: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Webhook processing failed"));
        }
    }
}