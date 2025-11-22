package com.ezbuy.auth.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotEmpty(message = "dto.email.not.empty")
    @Size(max = 200, message = "dto.email.over.length")
    private String email;

    public void setEmail(@NonNull String email) {
        this.email = email.trim();
    }
}
