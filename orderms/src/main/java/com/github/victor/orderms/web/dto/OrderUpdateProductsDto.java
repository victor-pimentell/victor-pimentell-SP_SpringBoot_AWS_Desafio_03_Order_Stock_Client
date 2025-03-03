package com.github.victor.orderms.web.dto;

import com.github.victor.orderms.entities.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateProductsDto {

    @NotNull
    private Long id;

    @Size(min = 1, message = "The list must not be empty")
    private List<Product> products;
}
