package com.github.victor.orderms.infra;

import com.github.victor.orderms.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stockms", url = "http://localhost:8082", path = "/stock/products")
public interface StockResourse {

    @PutMapping
    public ResponseEntity<?> updateProductsQuantities(@RequestBody List<Product> products);
}
