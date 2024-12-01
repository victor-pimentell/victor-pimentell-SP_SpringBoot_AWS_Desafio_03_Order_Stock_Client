package com.github.victor.clientms.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto extends RepresentationModel<ClientResponseDto> implements Serializable {
    private Long id;
    private String email;
    private List<Product> products;
}
