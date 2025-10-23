package com.ynova.customers.domain.exception;


public class ClientNotFoundException extends BusinessException {
    public ClientNotFoundException(Long id) { super("Cliente no encontrado: " + id); }
}