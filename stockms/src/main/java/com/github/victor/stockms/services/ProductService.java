package com.github.victor.stockms.services;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.repositories.ProductRepository;
import com.github.victor.stockms.util.HateoasUtil;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.dto.mapper.ProductMapper;
import com.github.victor.stockms.web.exceptions.ErrorCreatingHashException;
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
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }

    public Product createProduct(ProductCreateDto productCreateDto) {
        try {
            Product product = ProductMapper.toProduct(productCreateDto);
            product.setHash(generateHash(product.getName()));
            return productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueEntityException(
                    String.format("Error: There is already a product with this name: %s", productCreateDto.getName()));
        }
    }

    public Product updateProductQuantity(ProductQuantityDto productQuantityDto) {
        Product product = getProductById(productQuantityDto.getId());
        product.setQuantity(productQuantityDto.getQuantity());
        return productRepository.save(product);
    }

    public Product updateProductName(ProductNameDto productNameDto) {
        Product product = getProductById(productNameDto.getId());
        product.setName(productNameDto.getName());
        return productRepository.save(product);
    }

    public Page<ProductResponseDto> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(HateoasUtil::hateoasOnlyId);
    }

    public void updateProductsQuantities(List<Product> products) {
        for (Product value : products) {
            Optional<Product> productOptional = productRepository.findByHash(value.getHash());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setQuantity(product.getQuantity() + value.getQuantity());
                productRepository.save(product);
            }
        }
    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ErrorCreatingHashException("Error creating hash");
        }
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
