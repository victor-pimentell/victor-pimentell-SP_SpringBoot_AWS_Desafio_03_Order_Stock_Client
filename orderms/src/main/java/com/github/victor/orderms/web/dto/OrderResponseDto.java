package com.github.victor.orderms.web.dto;

import com.github.victor.orderms.entities.Product;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto extends RepresentationModel<OrderResponseDto> implements Serializable {
    private Long id;
    private String email;
    private List<Product> products;
}
