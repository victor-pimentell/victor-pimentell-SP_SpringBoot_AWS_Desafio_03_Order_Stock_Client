package com.github.victor.clientms.web.controllers;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.services.ClientService;
import com.github.victor.clientms.util.HateoasUtil;
import com.github.victor.clientms.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDto> getClientByEmail(@PathVariable String email) {
        log.info("Request received to return client by the following email: {}", email);
        Client client = clientService.getClientByEmail(email);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdAndEmailOrders(client);
        log.info("Returning client response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
        log.info("Request received to return client by the following ID: {}", id);
        Client client = clientService.getClientById(id);
        ClientResponseDto responseDto = HateoasUtil.hateoasEmailAndEmailOrders(client);
        log.info("Returning client response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientCreateDto clientCreateDto) {
        log.info("Request received to create a new client: {}", clientCreateDto);
        Client client = clientService.createClient(clientCreateDto);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();

        log.info("Client created with ID: {} and location: {}", client.getId(), location);
        return ResponseEntity.created(location).body(responseDto);
    }

    @PatchMapping("/email")
    public ResponseEntity<ClientResponseDto> emailUpdate(@Valid @RequestBody UpdateEmailDto updateEmailDto) {
        log.info("Request received to update email for client: {}", updateEmailDto);
        Client client = clientService.updateEmail(updateEmailDto);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);
        log.info("Returning updated client response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/name")
    public ResponseEntity<ClientResponseDto> nameUpdate(@Valid @RequestBody ClientUpdateName clientUpdateName) {
        log.info("Request received to update name for client: {}", clientUpdateName);
        Client client = clientService.clientUpdateName(clientUpdateName);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);
        log.info("Returning updated client response for name: {}", responseDto.getName());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/orders/{email}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByEmail(@PathVariable String email) {
        log.info("Request received to return orders for client by his email: {}", email);
        List<OrderResponseDto> orders = clientService.getOrdersByEmail(email);
        log.info("Returning orders for client email: {}", email);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        log.info("Request received to delete client by ID: {}", id);
        clientService.deleteProductById(id);
        log.info("Client with ID: {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }
}