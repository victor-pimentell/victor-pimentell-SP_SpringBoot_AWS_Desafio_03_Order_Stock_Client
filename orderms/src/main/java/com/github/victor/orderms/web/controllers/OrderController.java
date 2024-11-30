package com.github.victor.orderms.web.controllers;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.services.OrderService;
import com.github.victor.orderms.web.dto.OrderCreateDto;
import com.github.victor.orderms.web.dto.OrderResponseDto;
import com.github.victor.orderms.web.dto.UpdateEmailDto;
import com.github.victor.orderms.web.dto.OrderUpdateProductsDto;
import com.github.victor.orderms.web.dto.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        OrderResponseDto responseDto = OrderMapper.toDto(order);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByEmail(@PathVariable String email) {
        List<Order> orders = orderService.getOrdersByEmail(email);
        List<OrderResponseDto> responseDtos = OrderMapper.toListDto(orders);
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        Order order = orderService.createOrder(orderCreateDto);
        OrderResponseDto responseDto = OrderMapper.toDto(order);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping
    public ResponseEntity<OrderResponseDto> updateOrderProducts(@Valid @RequestBody OrderUpdateProductsDto orderUpdateProductsDto) {
        Order order = orderService.orderUpdateProduct(orderUpdateProductsDto);
        OrderResponseDto responseDto = OrderMapper.toDto(order);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/email")
    public ResponseEntity<OrderResponseDto> updateOrderEmail(@Valid @RequestBody UpdateEmailDto orderUpdateEmailDto) {
        orderService.updateEmailOrder(orderUpdateEmailDto);
        return ResponseEntity.noContent().build();
    }
}
