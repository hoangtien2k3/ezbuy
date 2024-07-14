package com.ezbuy.customermodel.dto.request;

import lombok.Builder;

@Builder
public record CreateCustomerRequest(
        String firstName,
        String lastName,
        String displayName,
        String phoneNumber,
        String email,
        String password,
        Boolean subscribedToNewsletter
) {
}
