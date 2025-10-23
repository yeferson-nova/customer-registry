package com.ynova.customers.application.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long id) {
        super("Client with id " + id + " not found.");
    }
}
