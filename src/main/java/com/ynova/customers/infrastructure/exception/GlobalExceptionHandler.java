package com.ynova.customers.infrastructure.exception;

import com.ynova.customers.application.exception.ClientInactiveException;
import com.ynova.customers.application.exception.ClientNotFoundException;
import com.ynova.customers.application.exception.InvalidAccountException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ClientNotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND, exception.getMessage());
        }
        if (exception instanceof ClientInactiveException) {
            return buildResponse(Response.Status.CONFLICT, exception.getMessage());
        }
        if (exception instanceof InvalidAccountException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }
        if (exception instanceof IllegalArgumentException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    private Response buildResponse(Response.Status status, String message) {
        return Response.status(status)
                .entity(Map.of("error", message, "code", status.getStatusCode()))
                .build();
    }
}
