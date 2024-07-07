package com.ezbuy.customer.dto;

public record ValidateCustomerDto(
        Boolean isValid,
        String errorMessage
) {
}
