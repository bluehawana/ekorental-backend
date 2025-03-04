package com.bluehawana.rentingcarsys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Object o, Object o1, int i, Object o2, Object o3, Object o4, Object o5) {
    }

    public PaymentException(String failedToUpdateBookingStatus, Object o, Object o1, int i, Exception e) {
    }
}