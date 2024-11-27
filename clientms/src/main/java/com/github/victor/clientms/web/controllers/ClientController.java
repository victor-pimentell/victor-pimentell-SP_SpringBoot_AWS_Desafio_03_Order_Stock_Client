package com.github.victor.clientms.web.controllers;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.services.ClientService;
import com.github.victor.clientms.web.dto.ClientCreateDto;
import com.github.victor.clientms.web.dto.ClientResponseDto;
import com.github.victor.clientms.web.dto.mapper.ClientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDto> getClientByEmail(@PathVariable String email) {
        Client client = clientService.getClientByEmail(email);
        ClientResponseDto responseDto = ClientMapper.toDto(client);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        ClientResponseDto responseDto = ClientMapper.toDto(client);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientService.createProduct(clientCreateDto);
        ClientResponseDto responseDto = ClientMapper.toDto(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }
}
