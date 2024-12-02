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
import com.github.victor.orderms.web.exceptions.ProductQuantityInvalidException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientResourse clientResourse;
    private final StockResourse stockResourse;


    public Order getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> {
            log.error("Order not found for ID: {}", id);

            return new OrderNotFoundException("Order not found.");
        });
    }

    public List<Order> getOrdersByEmail(String email) {
        log.info("Fetching orders by email: {}", email);

        List<Order> orders = orderRepository.findByEmail(email);
        log.info("Found {} orders for email: {}", orders.size(), email);
        return orders;
    }

    public Order createOrder(OrderCreateDto orderCreateDto) {
        log.info("Creating order with data: {}", orderCreateDto);
        Order order = OrderMapper.toOrder(orderCreateDto);

        log.debug("Validating existence of client for email: {}", order.getEmail());
        clientResourse.getClientByEmail(order.getEmail()).getBody();

        log.debug("Updating stock quantities for products in the order");
        order.getProducts().stream().peek(p -> p.setQuantity(p.getQuantity() * -1)).toList();
        stockResourse.updateProductsQuantities(order.getProducts());

        log.debug("Persisting products in the order");
        order.getProducts().stream().peek(p -> p.setId(null)).peek(p -> p.setQuantity(p.getQuantity() * -1)).toList();
        productRepository.saveAll(order.getProducts());

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public Order orderUpdateProduct(@Valid OrderUpdateProductsDto orderUpdateProductsDto) {
        log.info("Updating products in order with ID: {}", orderUpdateProductsDto.getId());
        Order order = getOrderById(orderUpdateProductsDto.getId());
        Order update = OrderMapper.toOrder(orderUpdateProductsDto);

        for (Product product: update.getProducts()) {
            if (product.getQuantity() < 1) {
                throw new ProductQuantityInvalidException("Quantity must be higher than zero.");
            }
        }

        log.debug("Updating product quantities in the order");
        for (int i = 0; i < order.getProducts().size(); i++) {
            Product productUpdate = update.getProducts().get(i);

            order.getProducts()
                    .stream()
                    .filter(p -> p.getHash().equals(productUpdate.getHash()))
                    .findFirst()
                    .ifPresent(p -> p.setQuantity(p.getQuantity() - productUpdate.getQuantity()));
        }

        update.setEmail(order.getEmail());

        log.debug("Updating stock quantities for modified products");
        stockResourse.updateProductsQuantities(order.getProducts());

        log.debug("Persisting updated products");
        productRepository.saveAll(update.getProducts());

        Order updatedOrder = orderRepository.save(update);
        return updatedOrder;
    }

    public void updateEmailOrder(UpdateEmailDto orderUpdateEmailDto) {
        log.info("Updating email for orders from {} to {}", orderUpdateEmailDto.getOldEmail(), orderUpdateEmailDto.getNewEmail());
        List<Order> orderList = orderRepository.findAllByEmail(orderUpdateEmailDto.getOldEmail());

        log.debug("Updating email field for {} orders", orderList.size());
        orderList.stream().peek(order -> order.setEmail(orderUpdateEmailDto.getNewEmail())).toList();

        orderRepository.saveAll(orderList);
        log.info("Email updated successfully for {} orders", orderList.size());
    }

    public void deleteOrderById(Long id) {
        Order order = getOrderById(id);

        log.debug("Reverting stock quantities for products in the order");
        stockResourse.updateProductsQuantities(order.getProducts());
        orderRepository.deleteById(id);
    }
}
