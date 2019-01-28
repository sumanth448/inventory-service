package com.paytm.inventory.exception;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class AuthorizationException extends RuntimeException {
    private final String errorCode;

    private final String errorMessage;

    public AuthorizationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
