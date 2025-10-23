package com.ynova.customers.domain.service.validation;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@QuarkusTest
class ClientValidationServiceTest {

    @Inject
    ClientValidationService clientValidationService;

    @InjectMock
    ChileValidationStrategy chileValidationStrategy;

    @InjectMock
    DefaultValidationStrategy defaultValidationStrategy;


    @Test
    void whenCountryIsChile_thenChileStrategyShouldBeCalled() {
        String numCTA = "003123456789";
        String countryCode = "CHILE";

        doReturn("CHILE").when(chileValidationStrategy).getCountryCode();
        doReturn("DEFAULT").when(defaultValidationStrategy).getCountryCode();

        clientValidationService.validate(numCTA, countryCode);

        Mockito.verify(chileValidationStrategy, times(1)).validate(numCTA, countryCode);
        Mockito.verify(defaultValidationStrategy, times(0)).validate(anyString(), anyString());
    }

    @Test
    void whenCountryIsNotChile_thenDefaultStrategyShouldBeCalled() {
        String numCTA = "123456789012";
        String countryCode = "COLOMBIA";

        doReturn("CHILE").when(chileValidationStrategy).getCountryCode();
        doReturn("DEFAULT").when(defaultValidationStrategy).getCountryCode();

        clientValidationService.validate(numCTA, countryCode);

        Mockito.verify(defaultValidationStrategy, times(1)).validate(numCTA, countryCode);
        Mockito.verify(chileValidationStrategy, times(0)).validate(anyString(), anyString());
    }
}