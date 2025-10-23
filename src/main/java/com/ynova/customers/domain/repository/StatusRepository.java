package com.ynova.customers.domain.repository;

import com.ynova.customers.domain.model.Status;

import java.util.Optional;

public interface StatusRepository {
    Optional<Status> findById(Long id);
    Optional<Status> findByName(String name);
}
