package com.github.victor.orderms.repositories;

import com.github.victor.orderms.entities.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEmail(String email);

    List<Order> findAllByEmail(String email);
}
