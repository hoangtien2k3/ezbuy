package com.ezbuy.customer.dto.request;

public record CreateCustomerRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Boolean subscribedToNewsletter
) {
}
