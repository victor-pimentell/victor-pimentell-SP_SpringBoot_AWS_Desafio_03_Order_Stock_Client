package com.github.victor.clientms.web.controllers;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.services.ClientService;
import com.github.victor.clientms.util.HateoasUtil;
import com.github.victor.clientms.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDto> getClientByEmail(@PathVariable String email) {
        Client client = clientService.getClientByEmail(email);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdAndEmailOrders(client);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        ClientResponseDto responseDto = HateoasUtil.hateoasEmailAndEmailOrders(client);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientService.createClient(clientCreateDto);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @PatchMapping("/email")
    public ResponseEntity<ClientResponseDto> emailUpdate(@Valid @RequestBody UpdateEmailDto updateEmailDto) {
        Client client = clientService.updateEmail(updateEmailDto);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/name")
    public ResponseEntity<ClientResponseDto> nameUpdate(@Valid @RequestBody ClientUpdateName clientUpdateName) {
        Client client = clientService.clientUpdateName(clientUpdateName);
        ClientResponseDto responseDto = HateoasUtil.hateoasIdEmailAndEmailOrders(client);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/orders/{email}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByEmail(@PathVariable String email) {
        List<OrderResponseDto> orders = clientService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }
}
