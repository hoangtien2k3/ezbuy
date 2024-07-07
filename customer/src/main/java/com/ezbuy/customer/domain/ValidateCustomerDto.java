package com.ezbuy.customer.domain;

public record ValidateCustomerDto(
        Boolean isValid,
        String errorMessage
) {
}
