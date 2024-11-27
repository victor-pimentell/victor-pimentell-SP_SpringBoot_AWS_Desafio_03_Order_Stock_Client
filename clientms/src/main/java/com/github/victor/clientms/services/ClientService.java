package com.github.victor.clientms.services;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.repositories.ClientRepository;
import com.github.victor.clientms.web.dto.ClientCreateDto;
import com.github.victor.clientms.web.dto.mapper.ClientMapper;
import com.github.victor.clientms.web.exceptions.ClientNotFoundException;
import com.github.victor.clientms.web.exceptions.UniqueEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }

    public Client createProduct(ClientCreateDto clientCreateDto) {
        try {
            return clientRepository.save(ClientMapper.toClient(clientCreateDto));
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueEntityException(
                    String.format("Error: There is already a account registered with this email: %s", clientCreateDto.getEmail()));
        }
    }
}