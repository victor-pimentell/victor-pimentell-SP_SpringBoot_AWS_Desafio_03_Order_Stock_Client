package com.github.victor.orderms.infra;

import com.github.victor.orderms.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stockms", url = "http://ec2-3-16-49-224.us-east-2.compute.amazonaws.com:8080", path = "/api/v1/stock/products")
public interface StockResourse {

    @PutMapping
    ResponseEntity<?> updateProductsQuantities(@RequestBody List<Product> products);
}
