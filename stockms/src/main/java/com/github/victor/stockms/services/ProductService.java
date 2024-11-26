package com.github.victor.stockms.services;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.repositories.ProductRepository;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.dto.mapper.ProductMapper;
import com.github.victor.stockms.web.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        return ProductMapper.toDto(product);
    }

    public ProductResponseDto createProduct(ProductCreateDto productCreateDto) {
        Product product = productRepository.save(ProductMapper.toProduct(productCreateDto));
        return ProductMapper.toDto(product);
    }

    public ProductResponseDto updateProductQuantity(ProductQuantityDto productQuantityDto) {
        Product product = productRepository.findById(productQuantityDto.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.setQuantity(productQuantityDto.getQuantity());
        return ProductMapper.toDto(productRepository.save(product));
    }

    public ProductResponseDto updateProductName(ProductNameDto productNameDto) {
        Product product = productRepository.findById(productNameDto.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.setName(productNameDto.getName());
        return ProductMapper.toDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAll() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.toListDto(products);
    }
}
