package com.ynova.customers.application.exception;

import jakarta.ws.rs.core.Response;

public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException(String message) {
        super(message);
    }
}
