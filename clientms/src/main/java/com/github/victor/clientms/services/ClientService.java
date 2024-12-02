package com.github.victor.clientms.services;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.infra.OrderResourse;
import com.github.victor.clientms.repositories.ClientRepository;
import com.github.victor.clientms.web.dto.*;
import com.github.victor.clientms.web.dto.mapper.ClientMapper;
import com.github.victor.clientms.web.exceptions.ClientNotFoundException;
import com.github.victor.clientms.web.exceptions.UniqueEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderResourse orderResourse;

    public Client getClientByEmail(String email) {
        log.info("Fetching client from database by email: {}", email);

        Optional<Client> client = clientRepository.findByEmail(email);

        if (client.isPresent()) {
            return client.get();
        } else {
            log.warn("Client not found for email: {}", email);

            throw  new ClientNotFoundException(
                    String.format("Error: There is no account registered with this email: %s", email));
        }
    }

    public Client getClientById(Long id) {
        log.info("Fetching client from database by ID: {}", id);

        return clientRepository.findById(id).orElseThrow(() -> {
            log.warn("Client not found for ID: {}", id);

            return new ClientNotFoundException("Client not found");
        });
    }

    public Client createClient(ClientCreateDto clientCreateDto) {
        log.info("Creating a new client: {}", clientCreateDto);

        try {
            Client client = ClientMapper.toClient(clientCreateDto);
            log.debug("Mapped Client entity: {}", client);
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create client due to data integrity violation: {}", clientCreateDto.getEmail());

            throw new UniqueEntityException(
                    String.format("Error: There is already a account registered with this email: %s", clientCreateDto.getEmail()));
        }
    }

    public Client updateEmail(UpdateEmailDto updateEmailDto) {
        log.info("Updating email for client: {}", updateEmailDto);
        Client client = getClientByEmail(updateEmailDto.getOldEmail());

        client.setEmail(updateEmailDto.getNewEmail());
        log.debug("Client email updated in memory: {}", client);

        orderResourse.updateOrderEmail(updateEmailDto);
        return clientRepository.save(client);
    }

    public Client clientUpdateName(ClientUpdateName clientUpdateName) {
        log.info("Updating name for client: {}", clientUpdateName);

        Client client = getClientByEmail(clientUpdateName.getEmail());
        log.debug("Client name updated in memory: {}", client);

        client.setName(clientUpdateName.getName());
        return clientRepository.save(client);
    }

    public List<OrderResponseDto> getOrdersByEmail(String email) {
        log.info("Fetching orders for client email: {}", email);
        List<OrderResponseDto> orders = orderResourse.getOrdersByEmail(email).getBody();
        return orders;
    }

    public void deleteProductById(Long id) {
        log.info("Deleting client by ID: {}", id);
        clientRepository.deleteById(id);
    }
}