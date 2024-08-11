package com.ezbuy.authmodel.dto.request;

import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPermission {
    @NotEmpty(message = "username.not.empty")
    private String username;

    @NotEmpty(message = "telecom.service.not.empty")
    private String telecomServiceId;

    @NotEmpty(message = "telecom.service.not.empty")
    private String telecomServiceAlias;
}
