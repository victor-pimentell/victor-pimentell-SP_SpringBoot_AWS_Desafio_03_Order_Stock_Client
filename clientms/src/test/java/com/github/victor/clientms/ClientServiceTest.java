package com.github.victor.clientms;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.infra.OrderResourse;
import com.github.victor.clientms.repositories.ClientRepository;
import com.github.victor.clientms.services.ClientService;
import com.github.victor.clientms.web.dto.OrderResponseDto;
import com.github.victor.clientms.web.dto.UpdateEmailDto;
import com.github.victor.clientms.web.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderResourse orderResourse;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateEmail_WithValidArgs_ReturnStatus200() {
        UpdateEmailDto updateEmailDto = new UpdateEmailDto("old@example.com", "new@example.com");
        Client client = new Client();
        client.setEmail(updateEmailDto.getOldEmail());

        when(clientRepository.findByEmail(updateEmailDto.getOldEmail())).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.updateEmail(updateEmailDto);

        assertNotNull(updatedClient);
        assertEquals(updateEmailDto.getNewEmail(), updatedClient.getEmail());
        verify(orderResourse, times(1)).updateOrderEmail(updateEmailDto);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void updateEmail_WithInvalidArgs_ReturnStatus404() {
        UpdateEmailDto updateEmailDto = new UpdateEmailDto("nonexistent@example.com", "new@example.com");

        when(clientRepository.findByEmail(updateEmailDto.getOldEmail())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateEmail(updateEmailDto));
        verify(orderResourse, never()).updateOrderEmail(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getOrdersByEmail_WithValidId_ReturnStatus200() {
        String email = "test@example.com";
        List<OrderResponseDto> mockOrders = List.of(new OrderResponseDto(), new OrderResponseDto());

        when(orderResourse.getOrdersByEmail(email)).thenReturn(ResponseEntity.ok(mockOrders));

        List<OrderResponseDto> orders = clientService.getOrdersByEmail(email);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderResourse, times(1)).getOrdersByEmail(email);
    }

    @Test
    void getOrdersByEmail_NoOrders_ReturnStatus200() {
        String email = "test@example.com";

        when(orderResourse.getOrdersByEmail(email)).thenReturn(ResponseEntity.ok(List.of()));

        List<OrderResponseDto> orders = clientService.getOrdersByEmail(email);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(orderResourse, times(1)).getOrdersByEmail(email);
    }
}
