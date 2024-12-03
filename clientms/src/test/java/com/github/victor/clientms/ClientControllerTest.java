package com.github.victor.clientms;

import com.github.victor.clientms.services.ClientService;
import com.github.victor.clientms.util.HateoasUtil;
import com.github.victor.clientms.web.controllers.ClientController;
import com.github.victor.clientms.web.dto.OrderResponseDto;
import com.github.victor.clientms.web.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private HateoasUtil hateoasUtil;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void GetOrdersByEmail_WithValidArgs_ReturnStatus200() {
        String email = "test@example.com";
        List<OrderResponseDto> mockOrders = List.of(new OrderResponseDto(), new OrderResponseDto());

        when(clientService.getOrdersByEmail(email)).thenReturn(mockOrders);

        ResponseEntity<List<OrderResponseDto>> response = clientController.getOrdersByEmail(email);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(mockOrders), response);
        verify(clientService, times(1)).getOrdersByEmail(email);
    }
}

