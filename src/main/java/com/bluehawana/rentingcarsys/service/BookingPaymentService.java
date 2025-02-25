package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingPaymentService {
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public void processBookingPayment(BookingDTO bookingDTO) throws PaymentException {
        String sessionId = paymentService.createCheckoutSession(bookingDTO);
        // Handle booking and payment coordination here
    }
}