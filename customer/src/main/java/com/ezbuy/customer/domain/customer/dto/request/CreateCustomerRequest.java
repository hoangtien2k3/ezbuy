package com.ezbuy.customer.domain.customer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCustomerRequest(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("phone_number") String phoneNumber,
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("subscribed_to_newsletter") Boolean subscribedToNewsletter
) {
}
