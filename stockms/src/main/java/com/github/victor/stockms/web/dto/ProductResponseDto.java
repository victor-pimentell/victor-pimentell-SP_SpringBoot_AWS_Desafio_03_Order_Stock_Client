package com.github.victor.stockms.web.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto extends RepresentationModel<ProductResponseDto> implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private String hash;
}
