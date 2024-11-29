package com.github.victor.stockms.services;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.repositories.ProductRepository;
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

import javax.naming.InsufficientResourcesException;
import java.util.List;

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
            return productRepository.save(ProductMapper.toProduct(productCreateDto));
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
        return productPage.map(ProductMapper::toDto);
    }

    public void updateProductsQuantities(List<Product> products) {
        List<Long> idList = products.stream().map(Product::getId).toList();
        List<Product> productList = productRepository.findAllById(idList);

        if (productList.size() != products.size()) {
            List<Long> productListIds = productList.stream().map(Product::getId).toList();
            List<Long> idsNotFound = idList.stream().filter(id -> !productListIds.contains(id)).toList();

            throw new ProductNotFoundException("Products with the following IDs were not found: " + idsNotFound);
        }

        for (int i = 0; i < productList.size(); i++) {
            Product productOrder = products.get(i);
            Product productStock = productList.get(i);

            if (productOrder.getQuantity() > productStock.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock of the following product: " + productOrder.getName());
            }

            productStock.setQuantity(productStock.getQuantity() - productOrder.getQuantity());
        }
        productRepository.saveAll(productList);
    }
}
