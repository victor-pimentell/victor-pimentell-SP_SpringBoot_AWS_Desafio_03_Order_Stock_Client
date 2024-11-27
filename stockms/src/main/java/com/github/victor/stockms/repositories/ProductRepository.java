package com.github.victor.stockms.repositories;

import com.github.victor.stockms.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
