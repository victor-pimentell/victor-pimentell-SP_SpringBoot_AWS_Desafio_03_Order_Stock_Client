package com.github.victor.stockms.web.controllers;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.services.ProductService;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stock/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> list = productService.getAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        ProductResponseDto product = productService.createProduct(productCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PatchMapping("/quantity")
    public ResponseEntity<ProductResponseDto> updateQuantity(@RequestBody ProductQuantityDto productQuantityDto) {
        ProductResponseDto product = productService.updateProductQuantity(productQuantityDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/name")
    public ResponseEntity<ProductResponseDto> updateName(@RequestBody ProductNameDto productNameDto) {
        ProductResponseDto product = productService.updateProductName(productNameDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.ok(product);
    }

}
