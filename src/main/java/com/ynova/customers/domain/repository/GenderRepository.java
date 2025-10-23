package com.ynova.customers.domain.repository;

import com.ynova.customers.domain.model.Gender;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface GenderRepository {
    Optional<Gender> findByName(String name);
    Optional<Gender> findById(Long id);
}
