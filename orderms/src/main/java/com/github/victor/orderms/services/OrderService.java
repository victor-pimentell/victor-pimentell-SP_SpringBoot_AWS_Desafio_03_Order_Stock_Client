package com.github.victor.orderms.services;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.infra.ClientResourse;
import com.github.victor.orderms.infra.StockResourse;
import com.github.victor.orderms.repositories.OrderRepository;
import com.github.victor.orderms.repositories.ProductRepository;
import com.github.victor.orderms.web.dto.OrderCreateDto;
import com.github.victor.orderms.web.dto.mapper.OrderMapper;
import com.github.victor.orderms.web.exceptions.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientResourse clientResourse;
    private final StockResourse stockResourse;


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByEmail(email);
    }

    public Order createOrder(OrderCreateDto orderCreateDto) {
        Order order = OrderMapper.toOrder(orderCreateDto);
        // request made in order to verify if the client passed on the orderCreate request really exists.
        clientResourse.getClientByEmail(order.getEmail()).getBody();

        stockResourse.updateProductsQuantities(order.getProducts());

        order.getProducts().stream().peek(p -> p.setId(null)).toList();

        productRepository.saveAll(order.getProducts());
        return orderRepository.save(order);
    }
}
