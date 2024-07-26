package com.ezbuy.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ClientLogin extends ProviderLogin {
    @NotEmpty(message = "login.client.id.not.empty")
    private String clientId;

    private String redirectUri;

    private String organizationId;//id cong ty chon, phuc vu cho luong sso tu phia doi tac
}
