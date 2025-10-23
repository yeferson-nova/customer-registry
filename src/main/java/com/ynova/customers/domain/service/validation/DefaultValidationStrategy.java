package com.ynova.customers.domain.service.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;

@ApplicationScoped
@Default
public class DefaultValidationStrategy implements CountryValidationStrategy {

    @Override
    public void validate(String numCTA, String countryCode) {
    }
}
