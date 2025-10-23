package com.ynova.customers.application.dto;

import java.time.LocalDate;

public record ClientResponse(
    Long id,
    String name,
    LocalDate birthDate,
    String numCTA,
    LocalDate inactivatedate,
    LocalDate activatedate,
    String gender,
    String status,
    String country
) {}
