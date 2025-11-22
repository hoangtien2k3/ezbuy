package com.ezbuy.auth.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "login.username.not.empty")
    @Length(max = 255, min = 1, message = "login.username.over.length")
    @NonNull
    private String username;

    @NotEmpty(message = "login.password.not.empty")
    private String password;

    private String clientId;
}
