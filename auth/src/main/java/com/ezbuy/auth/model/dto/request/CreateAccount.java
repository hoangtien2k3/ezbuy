package com.ezbuy.auth.model.dto.request;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccount {
    @Size(max = 6, min = 6, message = "dto.otp.invalid")
    private String otp;

    @Size(max = 255, min = 1, message = "signup.email.invalid")
    private String email;

    @Size(max = 255, min = 8, message = "dto.password.invalid")
    private String password;

    private String system;
}
