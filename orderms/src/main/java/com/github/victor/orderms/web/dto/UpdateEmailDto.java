package com.github.victor.orderms.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailDto {

    @Email(message = "please use a valid email format, example: john@example.com")
    @NotBlank(message = "the email must not be blank")
    private String oldEmail;

    @Email(message = "please use a valid email format, example: john@example.com")
    @NotBlank(message = "the email must not be blank")
    private String newEmail;
}
