package com.ezbuy.auth.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    @NotEmpty(message = "refresh.token.not.empty")
    private String refreshToken;

    @NotEmpty(message = "client.id.not.empty")
    private String clientId;
}
