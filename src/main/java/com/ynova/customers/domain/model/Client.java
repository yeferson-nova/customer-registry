package com.ynova.customers.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client extends PanacheEntity {

    @Column(length = 255, nullable = false)
    public String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    public LocalDate birthDate;

    @Column(length = 15, nullable = false)
    public String numCTA;

    @Column
    public LocalDate inactivatedate;

    @Column
    public LocalDate activatedate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false)
    public Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    public Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    public Country country;
}
