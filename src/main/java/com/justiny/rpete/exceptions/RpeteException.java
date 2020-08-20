package com.justiny.rpete.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RpeteException extends RuntimeException {
    public RpeteException(String exMessage) {
        super(exMessage);
    }
}
