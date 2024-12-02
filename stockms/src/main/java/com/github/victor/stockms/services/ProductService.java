package com.github.victor.stockms.services;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.repositories.ProductRepository;
import com.github.victor.stockms.util.HateoasUtil;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.dto.mapper.ProductMapper;
import com.github.victor.stockms.web.exceptions.InsufficientStockException;
import com.github.victor.stockms.web.exceptions.ProductNotFoundException;
import com.github.victor.stockms.web.exceptions.UniqueEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);

        return productRepository.findById(id).orElseThrow(() -> {
            log.warn("Product with ID: {} not found", id);

            return new ProductNotFoundException("Product not found.");
        });
    }

    public Product createProduct(ProductCreateDto productCreateDto) {
        try {
            log.info("Creating product with name: {}", productCreateDto.getName());

            Product product = ProductMapper.toProduct(productCreateDto);
            product.setHash(generateHash(product.getName()));

            return productRepository.save(product);
        } catch (DataIntegrityViolationException | NoSuchAlgorithmException ex) {
            log.error("Product creation failed. Duplicate name: {}", productCreateDto.getName());

            throw new UniqueEntityException(
                    String.format("Error: There is already a product with this name: %s", productCreateDto.getName()));
        }
    }

    public Product updateProductQuantity(ProductQuantityDto productQuantityDto) {
        Product product = getProductById(productQuantityDto.getId());
        product.setQuantity(productQuantityDto.getQuantity());
        Product updatedProduct = productRepository.save(product);
        log.info("Product ID: {} quantity updated to: {}", updatedProduct.getId(), updatedProduct.getQuantity());
        return updatedProduct;
    }

    public Product updateProductName(ProductNameDto productNameDto) {
        Product product = getProductById(productNameDto.getId());
        product.setName(productNameDto.getName());
        Product updatedProduct = productRepository.save(product);
        log.info("Product ID: {} name updated to: {}", updatedProduct.getId(), updatedProduct.getName());
        return updatedProduct;
    }

    public Page<ProductResponseDto> getAll(Pageable pageable) {
        log.info("Fetching all products with pagination: page = {}, size = {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Product> productPage = productRepository.findAll(pageable);
        log.info("Found {} products", productPage.getTotalElements());
        return productPage.map(HateoasUtil::hateoasOnlyId);
    }

    public void updateProductsQuantities(List<Product> products) {
        // Method used by the orderms, use this endpoint directly will return a wrong answer.
        log.info("Updating quantities for {} products", products.size());

        for (Product value : products) {
            Optional<Product> productOptional = productRepository.findByHash(value.getHash());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (value.getQuantity() + product.getQuantity() < 0) {
                    throw new InsufficientStockException("Insufficient stock of the following product: " + product.getName());
                }
                int newQuantity = product.getQuantity() + value.getQuantity();
                product.setQuantity(newQuantity);
                productRepository.save(product);
                log.info("Updated quantity for product ID: {} to: {}", product.getId(), newQuantity);
            } else {
                log.warn("Product with the following hash not found: {}", value.getHash());
                throw  new ProductNotFoundException(
                        String.format("Product with the following hash not found: %s", value.getHash())
                );
            }
        }
    }

    public void deleteProductById(Long id) {
        getProductById(id);
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
    }

    private String generateHash(String input) throws NoSuchAlgorithmException {
        log.info("Generating hash for input: {}", input);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());

        String hash = HexFormat.of().formatHex(hashBytes);
        log.info("Hash generated successfully");
        return hash;
    }
}
