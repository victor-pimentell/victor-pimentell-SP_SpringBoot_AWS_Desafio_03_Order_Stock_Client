package com.github.victor.orderms.services;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.entities.Product;
import com.github.victor.orderms.infra.ClientResourse;
import com.github.victor.orderms.infra.StockResourse;
import com.github.victor.orderms.repositories.OrderRepository;
import com.github.victor.orderms.repositories.ProductRepository;
import com.github.victor.orderms.web.dto.OrderCreateDto;
import com.github.victor.orderms.web.dto.UpdateEmailDto;
import com.github.victor.orderms.web.dto.OrderUpdateProductsDto;
import com.github.victor.orderms.web.dto.mapper.OrderMapper;
import com.github.victor.orderms.web.exceptions.OrderNotFoundException;
import jakarta.validation.Valid;
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

        order.getProducts().stream().peek(p -> p.setQuantity(p.getQuantity() * -1)).toList();

        stockResourse.updateProductsQuantities(order.getProducts());

        order.getProducts().stream().peek(p -> p.setId(null)).peek(p -> p.setQuantity(p.getQuantity() * -1)).toList();

        productRepository.saveAll(order.getProducts());
        return orderRepository.save(order);
    }

    public Order orderUpdateProduct(@Valid OrderUpdateProductsDto orderUpdateProductsDto) {
        Order order = getOrderById(orderUpdateProductsDto.getId());
        Order update = OrderMapper.toOrder(orderUpdateProductsDto);

        for (int i = 0; i < order.getProducts().size(); i++) {
            Product productUpdate = update.getProducts().get(i);
            order.getProducts()
                    .stream()
                    .filter(p -> p.getHash().equals(productUpdate.getHash()))
                    .findFirst()
                    .ifPresent(p -> p.setQuantity(p.getQuantity() - productUpdate.getQuantity()));
        }

        update.setEmail(order.getEmail());
        stockResourse.updateProductsQuantities(order.getProducts());
        productRepository.saveAll(update.getProducts());
        return orderRepository.save(update);
    }

    public void updateEmailOrder(UpdateEmailDto orderUpdateEmailDto) {
        List<Order> orderList = orderRepository.findAllByEmail(orderUpdateEmailDto.getOldEmail());
        orderList.stream().peek(order -> order.setEmail(orderUpdateEmailDto.getNewEmail())).toList();
        orderRepository.saveAll(orderList);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
