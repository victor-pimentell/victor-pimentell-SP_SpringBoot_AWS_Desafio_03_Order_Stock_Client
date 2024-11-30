package com.github.victor.stockms.web.controllers;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.services.ProductService;
import com.github.victor.stockms.util.HateoasUtil;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.dto.mapper.ProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Product product = productService.getProductById(id);
        ProductResponseDto responseDto = HateoasUtil.hateoasProductById(product);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<Void> updateProductsQuantities(@RequestBody List<Product> products) {
        productService.updateProductsQuantities(products);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(value = "limit", defaultValue = "12") Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<ProductResponseDto> list = productService.getAll(pageable);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        Product product = productService.createProduct(productCreateDto);
        ProductResponseDto responseDto = HateoasUtil.hateoasCreateProduct(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @PatchMapping("/quantity")
    public ResponseEntity<ProductResponseDto> updateQuantity(@RequestBody ProductQuantityDto productQuantityDto) {
        Product product = productService.updateProductQuantity(productQuantityDto);
        ProductResponseDto responseDto = ProductMapper.toDto(product);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/name")
    public ResponseEntity<ProductResponseDto> updateName(@RequestBody ProductNameDto productNameDto) {
        Product product = productService.updateProductName(productNameDto);
        ProductResponseDto responseDto = ProductMapper.toDto(product);
        return ResponseEntity.ok(responseDto);
    }
}
