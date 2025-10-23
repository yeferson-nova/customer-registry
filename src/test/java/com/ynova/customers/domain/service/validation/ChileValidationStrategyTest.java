package com.ynova.customers.domain.service.validation;

import com.ynova.customers.application.exception.InvalidAccountException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class ChileValidationStrategyTest {

    @Inject
    ChileValidationStrategy chileValidationStrategy;

    @Test
    void whenAccountNumberIsValidForChile_thenNoExceptionIsThrown() {
        String validAccountNumber = "003123456789";
        String countryCode = "CHILE";

        assertDoesNotThrow(() -> chileValidationStrategy.validate(validAccountNumber, countryCode));
    }

    @Test
    void whenAccountNumberIsInvalidForChile_thenInvalidAccountExceptionIsThrown() {
        String invalidAccountNumber = "123456789012";
        String countryCode = "CHILE";

        assertThrows(InvalidAccountException.class, () -> {
            chileValidationStrategy.validate(invalidAccountNumber, countryCode);
        });
    }

    @Test
    void whenAccountNumberIsNullForChile_thenInvalidAccountExceptionIsThrown() {
        String nullAccountNumber = null;
        String countryCode = "CHILE";

        assertThrows(InvalidAccountException.class, () -> {
            chileValidationStrategy.validate(nullAccountNumber, countryCode);
        });
    }

    @Test
    void whenCountryIsNotChile_thenNoExceptionIsThrown() {
        String anyAccountNumber = "123456789012";
        String countryCode = "COLOMBIA";

        assertDoesNotThrow(() -> chileValidationStrategy.validate(anyAccountNumber, countryCode));
    }
}
