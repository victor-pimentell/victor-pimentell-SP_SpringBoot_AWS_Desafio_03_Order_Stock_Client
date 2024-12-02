package com.github.victor.stockms.web.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDto {
    private Long id;
    private Integer quantity;
}
