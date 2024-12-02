package com.github.victor.stockms.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
    @NotBlank(message = "the name must not be blank")
    private String name;
    @NotNull(message = "the quantity must not be null")
    @Min(value = 1, message = "the quantity must be greater than or equal to 1")
    private Integer quantity;
}
