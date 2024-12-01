package com.github.victor.stockms.repositories;

import com.github.victor.stockms.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByHash(String hash);
}
