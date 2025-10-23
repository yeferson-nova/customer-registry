package com.ynova.customers.domain.service.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientValidationService {

    private final Instance<CountryValidationStrategy> validationStrategies;

    @Inject
    public ClientValidationService(Instance<CountryValidationStrategy> validationStrategies) {
        this.validationStrategies = validationStrategies;
    }

    public void validate(String numCTA, String countryCode) {
        Instance<CountryValidationStrategy> selectedStrategy = validationStrategies.select(new CountryCode.Literal(countryCode.toUpperCase()));

        CountryValidationStrategy strategy = selectedStrategy.isUnsatisfied()
                ? validationStrategies.select(Default.Literal.INSTANCE).get()
                : selectedStrategy.get();

        strategy.validate(numCTA, countryCode);
    }
}
