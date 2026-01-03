package com.ezbuy.auth.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "dto.email.not.empty")
    @Size(max = 200, message = "dto.email.over.length")
    private String email;

    @NotEmpty(message = "dto.otp.not.empty")
    private String otp;

    @Size(max = 50, message = "dto.password.over.length")
    @NotEmpty(message = "dto.password.not.empty")
    private String password;

    public void setEmail(@NonNull String email) {
        this.email = email.trim();
    }
}
