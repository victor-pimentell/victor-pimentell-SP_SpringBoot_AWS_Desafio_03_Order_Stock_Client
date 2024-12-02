package com.github.victor.orderms.web.controllers;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.services.OrderService;
import com.github.victor.orderms.util.HateoasUtil;
import com.github.victor.orderms.web.dto.OrderCreateDto;
import com.github.victor.orderms.web.dto.OrderResponseDto;
import com.github.victor.orderms.web.dto.UpdateEmailDto;
import com.github.victor.orderms.web.dto.OrderUpdateProductsDto;
import com.github.victor.orderms.web.dto.mapper.OrderMapper;
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
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        log.info("Request received: Get order by ID: {}", id);
        Order order = orderService.getOrderById(id);

        OrderResponseDto responseDto = HateoasUtil.hateoasEmailOrders(order);
        log.info("Order retrieved successfully: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByEmail(@PathVariable String email) {
        log.info("Request received: Get orders by email: {}", email);
        List<Order> orders = orderService.getOrdersByEmail(email);

        List<OrderResponseDto> responseDtos = HateoasUtil.hateoasId(orders);
        log.info("Orders retrieved successfully for email: {}", email);
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        log.info("Request received: Create new order: {}", orderCreateDto);
        Order order = orderService.createOrder(orderCreateDto);
        OrderResponseDto responseDto = HateoasUtil.hateoasIdAndEmailOrders(order);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        log.info("Order created successfully with ID: {}", order.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping
    public ResponseEntity<OrderResponseDto> updateOrderProducts(@Valid @RequestBody OrderUpdateProductsDto orderUpdateProductsDto) {
        log.info("Request received: Update products in order: {}", orderUpdateProductsDto);

        Order order = orderService.orderUpdateProduct(orderUpdateProductsDto);
        OrderResponseDto responseDto = HateoasUtil.hateoasIdAndEmailOrders(order);
        log.info("Order products updated successfully for order ID: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/email")
    public ResponseEntity<OrderResponseDto> updateOrderEmail(@Valid @RequestBody UpdateEmailDto orderUpdateEmailDto) {
        log.info("Request received: Update order email: {}", orderUpdateEmailDto);

        orderService.updateEmailOrder(orderUpdateEmailDto);
        log.info("Order email updated successfully for new email: {}", orderUpdateEmailDto.getNewEmail());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        log.info("Request received: Delete order by ID: {}", id);

        orderService.deleteOrderById(id);
        log.info("Order deleted successfully for ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
