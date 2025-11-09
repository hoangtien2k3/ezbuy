package com.ezbuy.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestSync {
    private String clientId;
    private String clientSecret;
}
