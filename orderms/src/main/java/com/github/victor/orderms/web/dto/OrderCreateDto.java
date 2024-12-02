package com.github.victor.orderms.web.dto;

import com.github.victor.orderms.entities.Product;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    @Email(message = "please use a valid email format, example: john@example.com")
    @NotBlank(message = "the email must not be blank")
    private String email;
    @Size(min = 1, message = "The list must not be empty")
    private List<Product> products;
}
