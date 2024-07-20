package com.ezbuy.customer.model.dto;

import lombok.Builder;

@Builder
public record ValidateCustomerDto(
        Boolean isValid,
        String errorMessage
) {
}