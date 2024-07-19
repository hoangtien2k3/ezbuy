package com.ezbuy.customermodel.dto.request;

public record CustomerLoginRequest(
        String email,
        String password
) {
}
