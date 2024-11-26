package com.github.victor.stockms.web.dto.mapper;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toProduct(ProductCreateDto dto) {
        return new ModelMapper().map(dto, Product.class);
    }

    public static ProductResponseDto toDto(Product product) {
        return new ModelMapper().map(product, ProductResponseDto.class);
    }

    public static List<ProductResponseDto> toListDto(List<Product> products){
        return products.stream().map(ProductMapper::toDto).toList();
    }
}
