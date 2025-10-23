package com.ynova.customers.application.exception;

public class ClientInactiveException extends RuntimeException {

    public ClientInactiveException(Long id) {
        super("Client with id " + id + " is inactive and cannot be updated.");
    }
}
