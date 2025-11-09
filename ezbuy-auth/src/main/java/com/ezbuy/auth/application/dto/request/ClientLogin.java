package com.ezbuy.auth.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClientLogin extends ProviderLogin {
    @NotEmpty(message = "login.client.id.not.empty")
    private String clientId;

    private String redirectUri;

    private String organizationId;
}
