package com.github.victor.stockms.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto extends RepresentationModel<ProductResponseDto> implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private String hash;
}
