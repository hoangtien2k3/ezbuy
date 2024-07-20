package com.ezbuy.customer.model.dto.response;

public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
