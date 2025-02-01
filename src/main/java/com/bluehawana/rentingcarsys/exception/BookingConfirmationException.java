package com.bluehawana.rentingcarsys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingConfirmationException extends RuntimeException {
    public BookingConfirmationException(String message) {
        super(message);
    }
}