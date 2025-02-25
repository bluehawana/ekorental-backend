package com.bluehawana.rentingcarsys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentException extends RuntimeException {
    public PaymentException(String message, Exception e) {
        super(message);
    }
}