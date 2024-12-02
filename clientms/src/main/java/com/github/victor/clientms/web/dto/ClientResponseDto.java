package com.github.victor.clientms.web.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto extends RepresentationModel<ClientResponseDto> implements Serializable {
    private Long id;
    private String name;
    private String email;
}
