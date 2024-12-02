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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/stock/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("Request received: get product by ID: {}", id);

        Product product = productService.getProductById(id);
        ProductResponseDto responseDto = HateoasUtil.hateoasOnlyList(product);
        log.info("Product retrieved successfully: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<Void> updateProductsQuantities(@RequestBody List<Product> products) {
        log.info("Request received: update quantities for {} products.", products.size());

        productService.updateProductsQuantities(products);
        log.info("Product quantities updated successfully.");
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(value = "limit", defaultValue = "12") Integer limit) {
        log.info("Request received: get all products with pagination. Page: {}, Limit: {}", page, limit);

        Pageable pageable = PageRequest.of(page, limit);
        Page<ProductResponseDto> list = productService.getAll(pageable);
        log.info("Successfully retrieved {} products for Page: {}", list.getNumberOfElements(), page);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        log.info("Request received: create new product with name: {}", productCreateDto.getName());
        Product product = productService.createProduct(productCreateDto);
        ProductResponseDto responseDto = HateoasUtil.hateoasListAndId(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        log.info("Product created successfully with ID: {}", product.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @PatchMapping("/quantity")
    public ResponseEntity<ProductResponseDto> updateQuantity(@RequestBody ProductQuantityDto productQuantityDto) {
        log.info("Request received: update quantity for product with ID: {}", productQuantityDto.getId());

        Product product = productService.updateProductQuantity(productQuantityDto);
        ProductResponseDto responseDto = HateoasUtil.hateoasListAndId(product);
        log.info("Product quantity updated successfully: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/name")
    public ResponseEntity<ProductResponseDto> updateName(@RequestBody ProductNameDto productNameDto) {
        log.info("Request received: update name for product with ID: {}", productNameDto.getId());

        Product product = productService.updateProductName(productNameDto);
        ProductResponseDto responseDto = HateoasUtil.hateoasListAndId(product);
        log.info("Product name updated successfully for ID: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        log.info("Request received: delete product by ID: {}", id);

        productService.deleteProductById(id);
        log.info("Product deleted successfully for ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
