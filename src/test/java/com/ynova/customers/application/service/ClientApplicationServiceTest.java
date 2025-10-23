package com.ynova.customers.application.service;

import com.ynova.customers.application.dto.CreateClientRequest;
import com.ynova.customers.application.dto.UpdateClientRequest;
import com.ynova.customers.application.exception.ClientInactiveException;
import com.ynova.customers.application.exception.ClientNotFoundException;
import com.ynova.customers.application.mapper.ClientMapper;
import com.ynova.customers.domain.model.Client;
import com.ynova.customers.domain.model.Country;
import com.ynova.customers.domain.model.Status;
import com.ynova.customers.domain.repository.ClientRepository;
import com.ynova.customers.domain.service.validation.ClientValidationService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientApplicationServiceTest {

    @InjectMocks
    private ClientApplicationService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientValidationService clientValidationService;

    private MockedStatic<Country> countryMockedStatic;
    private MockedStatic<Status> statusMockedStatic;

    @BeforeEach
    void setUp() {
        countryMockedStatic = mockStatic(Country.class);
        statusMockedStatic = mockStatic(Status.class);
    }

    @AfterEach
    void tearDown() {
        countryMockedStatic.close();
        statusMockedStatic.close();
    }

    @Test
    void createClient_shouldPersistClientWithActiveStatus() {
        Country country = new Country();
        country.name = "COLOMBIA";
        Status activeStatus = new Status();
        activeStatus.name = "ACTIVE";

        CreateClientRequest request = new CreateClientRequest("New Client", LocalDate.now().minusYears(20), 1L, "123456789012", 1L);
        Client clientEntity = new Client();

        when(Country.findById(anyLong())).thenReturn(country);
        
        @SuppressWarnings("unchecked")
        PanacheQuery<Status> activeQuery = mock(PanacheQuery.class);
        when(activeQuery.firstResult()).thenReturn(activeStatus);
        when(Status.find(anyString(), anyString())).thenReturn(activeQuery);
        
        when(clientMapper.toEntity(request)).thenReturn(clientEntity);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        clientService.createClient(request);

        verify(clientValidationService).validate(request.numCTA(), country.name);
        verify(clientRepository).persist(clientCaptor.capture());
        Client persistedClient = clientCaptor.getValue();
        assertNotNull(persistedClient);
        assertEquals(activeStatus, persistedClient.status);
        assertNotNull(persistedClient.activatedate);
    }

    @Test
    void updateClient_whenClientIsInactive_shouldThrowException() {
        Client client = new Client();
        client.status = new Status();
        client.status.name = "INACTIVE";
        when(clientRepository.findByIdOptional(1L)).thenReturn(Optional.of(client));

        UpdateClientRequest request = new UpdateClientRequest("Updated Name", LocalDate.now(), 1L, "123");

        assertThrows(ClientInactiveException.class, () -> clientService.updateClient(1L, request));
        verify(clientRepository, never()).persist(any(Client.class));
    }
}
