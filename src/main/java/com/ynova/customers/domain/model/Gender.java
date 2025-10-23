package com.ynova.customers.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "genders")
public class Gender extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 50)
    public String name;
}
