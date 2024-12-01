package com.github.victor.orderms.repositories;

import com.github.victor.orderms.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
