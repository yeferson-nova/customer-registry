package com.ynova.customers.application.mapper;

import com.ynova.customers.application.dto.ClientResponse;
import com.ynova.customers.application.dto.CreateClientRequest;
import com.ynova.customers.domain.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "cdi")
public interface ClientMapper {

    @Mappings({
        @Mapping(source = "gender.name", target = "gender"),
        @Mapping(source = "status.name", target = "status"),
        @Mapping(source = "country.name", target = "country")
    })
    ClientResponse toResponse(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inactivatedate", ignore = true)
    @Mapping(target = "activatedate", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "country", ignore = true)
    Client toEntity(CreateClientRequest request);
}
