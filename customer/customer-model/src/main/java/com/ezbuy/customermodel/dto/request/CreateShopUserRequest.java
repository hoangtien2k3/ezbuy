package com.ezbuy.customermodel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateShopUserRequest(
        String username,
        String securityQuestion,
        String securityAnswer
) {
}
