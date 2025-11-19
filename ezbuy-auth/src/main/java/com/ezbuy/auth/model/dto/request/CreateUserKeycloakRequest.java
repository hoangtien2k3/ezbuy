package com.ezbuy.auth.model.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserKeycloakRequest {
    private String username;
    private Boolean enabled;
    private String email;
    private List<String> groups;
    private List<CredentialKeycloakRequest> credentials;
}
