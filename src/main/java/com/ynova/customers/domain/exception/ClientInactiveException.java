package com.ynova.customers.domain.exception;

public class ClientInactiveException extends BusinessException {
    public ClientInactiveException(Long id) { super("Cliente inactivo, no se puede actualizar: " + id); }
}