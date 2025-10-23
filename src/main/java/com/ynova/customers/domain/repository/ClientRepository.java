package com.ynova.customers.domain.repository;

import com.ynova.customers.domain.model.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {
}
