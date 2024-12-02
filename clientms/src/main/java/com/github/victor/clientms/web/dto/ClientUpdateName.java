package com.github.victor.clientms.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateName {
    @NotBlank(message = "the name must not be blank")
    private String name;

    @Email(message = "please use a valid email format, example: john@example.com")
    @NotBlank(message = "the email must not be blank")
    private String email;
}
