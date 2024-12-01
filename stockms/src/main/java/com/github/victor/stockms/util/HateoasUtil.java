package com.github.victor.stockms.util;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.web.controllers.ProductController;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.dto.mapper.ProductMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasUtil {

    public static ProductResponseDto hateoasOnlyList(Product input) {
        ProductResponseDto output = ProductMapper.toDto(input);
        output.add(linkTo(methodOn(ProductController.class).getAllProducts(0, 12)).withRel("products_list"));
        return output;
    }

    public static ProductResponseDto hateoasOnlyId(Product input) {
        ProductResponseDto output = ProductMapper.toDto(input);
        output.add(linkTo(methodOn(ProductController.class).getProductById(input.getId())).withSelfRel());
        return output;
    }

    public static ProductResponseDto hateoasListAndId(Product input) {
        ProductResponseDto output = ProductMapper.toDto(input);
        output.add(linkTo(methodOn(ProductController.class).getProductById(input.getId())).withSelfRel());
        output.add(linkTo(methodOn(ProductController.class).getAllProducts(0, 12)).withRel("products_list"));
        return output;
    }
}
