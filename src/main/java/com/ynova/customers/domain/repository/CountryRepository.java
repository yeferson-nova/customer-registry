package com.ynova.customers.domain.repository;

import com.ynova.customers.domain.model.Country;

import java.util.Optional;

public interface CountryRepository {
    Optional<Country> findByName(String name);
    Optional<Country> findById(Long id);
}
