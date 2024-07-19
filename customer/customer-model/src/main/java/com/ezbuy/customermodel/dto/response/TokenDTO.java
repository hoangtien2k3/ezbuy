package com.ezbuy.customermodel.dto.response;

public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
