package com.ezbuy.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    @NotEmpty(message = "refresh.token.not.null")
    private String refreshToken;

    @NotEmpty(message = "client.id.not.empty")
    private String clientId;
}
