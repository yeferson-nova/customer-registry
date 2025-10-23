package com.ynova.customers.infrastructure.resource;

import com.ynova.customers.application.dto.ClientResponse;
import com.ynova.customers.application.dto.CreateClientRequest;
import com.ynova.customers.application.dto.UpdateClientRequest;
import com.ynova.customers.application.mapper.ClientMapper;
import com.ynova.customers.application.service.ClientApplicationService;
import com.ynova.customers.domain.model.Client;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Client Management", description = "Endpoints for creating, updating, and managing clients")
@Authenticated
public class ClientResource {

    @Inject
    ClientApplicationService clientService;

    @Inject
    ClientMapper clientMapper;

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    @Operation(summary = "Get a client by ID", description = "Retrieves a single client's details.")
    @APIResponse(responseCode = "200", description = "Client found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ClientResponse.class)))
    @APIResponse(responseCode = "404", description = "Client not found")
    public Response getClientById(@PathParam("id") Long id) {
        Client client = clientService.findClientById(id);
        return Response.ok(clientMapper.toResponse(client)).build();
    }

    @POST
    @RolesAllowed("user")
    @Operation(summary = "Create a new client", description = "Creates a new client with an ACTIVE status by default.")
    @APIResponse(responseCode = "201", description = "Client created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ClientResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid request data (e.g., validation error, invalid account number)")
    public Response createClient(@Valid CreateClientRequest request) {
        Client client = clientService.createClient(request);
        return Response.status(Response.Status.CREATED).entity(clientMapper.toResponse(client)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    @Operation(summary = "Update an existing client", description = "Updates a client's information. The client must be ACTIVE.")
    @APIResponse(responseCode = "200", description = "Client updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ClientResponse.class)))
    @APIResponse(responseCode = "404", description = "Client not found")
    @APIResponse(responseCode = "409", description = "Client is inactive and cannot be updated")
    public Response updateClient(@PathParam("id") Long id, @Valid UpdateClientRequest request) {
        Client client = clientService.updateClient(id, request);
        return Response.ok(clientMapper.toResponse(client)).build();
    }

    @DELETE
    @Path("/{id}/inactivate")
    @RolesAllowed("admin")
    @Operation(summary = "Inactivate a client", description = "Changes a client's status to INACTIVE.")
    @APIResponse(responseCode = "204", description = "Client inactivated successfully")
    @APIResponse(responseCode = "404", description = "Client not found")
    public Response inactivateClient(@PathParam("id") Long id) {
        clientService.inactivateClient(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/activate")
    @RolesAllowed("admin")
    @Operation(summary = "Activate a client", description = "Changes a client's status to ACTIVE.")
    @APIResponse(responseCode = "204", description = "Client activated successfully")
    @APIResponse(responseCode = "404", description = "Client not found")
    public Response activateClient(@PathParam("id") Long id) {
        clientService.activateClient(id);
        return Response.noContent().build();
    }
}
