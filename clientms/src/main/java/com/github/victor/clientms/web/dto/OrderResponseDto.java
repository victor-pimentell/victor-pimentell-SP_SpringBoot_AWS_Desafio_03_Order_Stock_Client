package com.github.victor.clientms.web.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto extends RepresentationModel<ClientResponseDto> implements Serializable {
    private Long id;
    private String email;
    private List<Product> products;
}
