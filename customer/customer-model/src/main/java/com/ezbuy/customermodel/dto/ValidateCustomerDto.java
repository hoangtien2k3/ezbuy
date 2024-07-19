package com.ezbuy.customermodel.dto;

import lombok.Builder;

@Builder
public record ValidateCustomerDto(
        Boolean isValid,
        String errorMessage
) {
}