package com.example.demo.schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "Username cannot be blank")
    private String userName;
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
