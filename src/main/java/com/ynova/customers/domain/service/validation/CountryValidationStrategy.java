package com.ynova.customers.domain.service.validation;

public interface CountryValidationStrategy {
    void validate(String numCTA, String countryCode);
}
