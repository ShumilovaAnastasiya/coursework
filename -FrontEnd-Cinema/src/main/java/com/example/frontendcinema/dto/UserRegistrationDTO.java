package com.example.frontendcinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String repeatPassword;
}
