package com.ynova.customers.domain.service.validation;

import com.ynova.customers.application.exception.InvalidAccountException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@CountryCode("CHILE")
public class ChileValidationStrategy implements CountryValidationStrategy {

    private static final String CHILE_COUNTRY_CODE = "CHILE";
    private static final String CHILE_ACCOUNT_PREFIX = "003";

    @Override
    public void validate(String numCTA, String countryCode) {
        if (CHILE_COUNTRY_CODE.equalsIgnoreCase(countryCode)) {
            if (numCTA == null || !numCTA.startsWith(CHILE_ACCOUNT_PREFIX)) {
                throw new InvalidAccountException("Invalid account number for Chile. It must start with '" + CHILE_ACCOUNT_PREFIX + "'.");
            }
        }
    }
}
