package com.ynova.customers.domain.service.validation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
class DefaultValidationStrategyTest {

    @Inject
    DefaultValidationStrategy defaultValidationStrategy;

    @Test
    void validate_shouldAlwaysPass() {
        String numCTA = "anyAccountNumber";
        String countryCode = "anyCountryCode";

        assertDoesNotThrow(() -> defaultValidationStrategy.validate(numCTA, countryCode));
    }

    @Test
    void validate_shouldPassWithNullInputs() {
        String numCTA = null;
        String countryCode = null;

        assertDoesNotThrow(() -> defaultValidationStrategy.validate(numCTA, countryCode));
    }
}
