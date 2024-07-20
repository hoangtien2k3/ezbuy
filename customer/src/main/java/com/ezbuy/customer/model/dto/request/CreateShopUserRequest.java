package com.ezbuy.customer.model.dto.request;

public record CreateShopUserRequest(
        String username,
        String securityQuestion,
        String securityAnswer
) {
}
