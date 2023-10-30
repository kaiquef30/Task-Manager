package com.projectmicrosoft.microsoft.api.controller.client;

import com.projectmicrosoft.microsoft.api.DTO.ClientDTO;
import com.projectmicrosoft.microsoft.api.security.AuthenticatedUser;
import com.projectmicrosoft.microsoft.exception.ClientNotFoundException;
import com.projectmicrosoft.microsoft.exception.messages.ClientMessageConfig;
import com.projectmicrosoft.microsoft.model.Client;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientMessageConfig clientMessageConfig;

    public ClientController(ClientService clientService, ClientMessageConfig clientMessageConfig) {
        this.clientService = clientService;
        this.clientMessageConfig = clientMessageConfig;
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Display all customers")
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN", "USER"})
    @Operation(summary = "Register a new customer")
    @ApiResponse(responseCode = "201", description = "Successfully registered client!",
            content = {@Content(schema = @Schema(implementation = Client.class))})
    @ApiResponse(responseCode = "409", description = "Client already exists!", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<?> registerClient(@RequestBody @Valid ClientDTO clientDto, @AuthenticationPrincipal User user) {
        Client clientRegistered = clientService.registerClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientRegistered);
    }

    @AuthenticatedUser(requiredRoles = {"USER"})
    @Operation(summary = "Search customer by name")
    @ApiResponse(responseCode = "200", description = "Customer successfully found.",
            content = {@Content(schema = @Schema(implementation = Client.class))})
    @ApiResponse(responseCode = "404", description = "Customer not found.", content = @Content)
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "204", description = "Customer successfully deleted.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Customer not found.", content = @Content)
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Long clientId) {
        try {
            clientService.deleteClient(clientId);
            return ResponseEntity.noContent().build();
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clientMessageConfig.getClientNotFound());
        }
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer successfully updated.", content =
            {@Content(schema = @Schema(implementation = Client.class))})
    @ApiResponse(responseCode = "404", description = "Customer not found.", content = @Content)
    @ApiResponse(responseCode = "403", description = "You are not allowed to update this customer.", content = @Content)
    @PutMapping("/{clientId}")
    public ResponseEntity<?> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDto) {
        try {
            Client clientUpdated = clientService.updateClient(clientId, clientDto);
            return ResponseEntity.status(HttpStatus.OK).body(clientUpdated);
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clientMessageConfig.getClientNotFound());
        }
    }

}