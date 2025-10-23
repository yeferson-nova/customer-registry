package com.ynova.customers.application.service;

import com.ynova.customers.application.dto.CreateClientRequest;
import com.ynova.customers.application.dto.UpdateClientRequest;
import com.ynova.customers.application.exception.ClientInactiveException;
import com.ynova.customers.application.exception.ClientNotFoundException;
import com.ynova.customers.application.mapper.ClientMapper;
import com.ynova.customers.domain.model.Client;
import com.ynova.customers.domain.model.Country;
import com.ynova.customers.domain.model.Gender;
import com.ynova.customers.domain.model.Status;
import com.ynova.customers.domain.repository.ClientRepository;
import com.ynova.customers.domain.service.validation.ClientValidationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import java.time.LocalDate;

@ApplicationScoped
public class ClientApplicationService {

    private static final Logger LOG = Logger.getLogger(ClientApplicationService.class);

    @Inject
    ClientRepository clientRepository;

    @Inject
    ClientMapper clientMapper;

    @Inject
    ClientValidationService clientValidationService;

    @Transactional
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 60000)
    public Client createClient(CreateClientRequest request) {
        LOG.info("Attempting to create a new client");

        Country country = Country.findById(request.countryId());
        if (country == null) {
            throw new IllegalArgumentException("Invalid Country ID: " + request.countryId());
        }

        clientValidationService.validate(request.numCTA(), country.name);

        Client client = clientMapper.toEntity(request);
        client.country = country;
        client.gender = Gender.findById(request.genderId());
        client.status = Status.find("name", "ACTIVE").firstResult();
        client.activatedate = LocalDate.now();

        clientRepository.persist(client);
        LOG.infof("Client created with ID: %d", client.id);
        return client;
    }

    @Transactional
    public Client updateClient(Long id, UpdateClientRequest request) {
        LOG.infof("Attempting to update client with ID: %d", id);
        Client client = findClientById(id);

        if (!"ACTIVE".equals(client.status.name)) {
            throw new ClientInactiveException(id);
        }

        clientValidationService.validate(request.numCTA(), client.country.name);

        client.name = request.name();
        client.birthDate = request.birthDate();
        client.numCTA = request.numCTA();
        client.gender = Gender.findById(request.genderId());

        clientRepository.persist(client);
        LOG.infof("Client with ID: %d updated successfully", id);
        return client;
    }

    @Transactional
    public void inactivateClient(Long id) {
        LOG.infof("Inactivating client with ID: %d", id);
        Client client = findClientById(id);
        client.status = Status.find("name", "INACTIVE").firstResult();
        client.inactivatedate = LocalDate.now();
        client.activatedate = null;
        clientRepository.persist(client);
        LOG.infof("Client with ID: %d inactivated", id);
    }

    @Transactional
    public void activateClient(Long id) {
        LOG.infof("Activating client with ID: %d", id);
        Client client = findClientById(id);
        client.status = Status.find("name", "ACTIVE").firstResult();
        client.activatedate = LocalDate.now();
        client.inactivatedate = null;
        clientRepository.persist(client);
        LOG.infof("Client with ID: %d activated", id);
    }

    @Retry(maxRetries = 2)
    public Client findClientById(Long id) {
        LOG.debugf("Finding client by ID: %d", id);
        return clientRepository.findByIdOptional(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
