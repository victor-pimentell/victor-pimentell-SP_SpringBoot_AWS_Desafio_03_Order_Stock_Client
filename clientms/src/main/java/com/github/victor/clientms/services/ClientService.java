package com.github.victor.clientms.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.infra.OrderResourse;
import com.github.victor.clientms.repositories.ClientRepository;
import com.github.victor.clientms.util.HateoasUtil;
import com.github.victor.clientms.web.controllers.ClientController;
import com.github.victor.clientms.web.dto.*;
import com.github.victor.clientms.web.dto.mapper.ClientMapper;
import com.github.victor.clientms.web.exceptions.ClientNotFoundException;
import com.github.victor.clientms.web.exceptions.UniqueEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderResourse orderResourse;

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ClientNotFoundException(
                String.format("Error: There is no account registered with this email: %s", email)));
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }

    public Client createClient(ClientCreateDto clientCreateDto) {
        try {
            Client client = ClientMapper.toClient(clientCreateDto);
            clientRepository.save(client);
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueEntityException(
                    String.format("Error: There is already a account registered with this email: %s", clientCreateDto.getEmail()));
        }
    }

    public Client updateEmail(UpdateEmailDto updateEmailDto) {
        Client client = getClientByEmail(updateEmailDto.getOldEmail());
        client.setEmail(updateEmailDto.getNewEmail());
        orderResourse.updateOrderEmail(updateEmailDto);
        return clientRepository.save(client);
    }

    public Client clientUpdateName(ClientUpdateName clientUpdateName) {
        Client client = getClientByEmail(clientUpdateName.getEmail());
        client.setName(clientUpdateName.getName());
        return clientRepository.save(client);
    }

    public List<OrderResponseDto> getOrdersByEmail(String email) {
        return orderResourse.getOrdersByEmail(email).getBody();
    }

    public void deleteProductById(Long id) {
        clientRepository.deleteById(id);
    }
}