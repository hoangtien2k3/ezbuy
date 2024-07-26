package com.ezbuy.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderLogin {
    @NotEmpty(message = "login.provider.code.not.empty")
    private String code;
}
