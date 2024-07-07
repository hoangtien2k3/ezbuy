package com.ezbuy.customer.domain.customer.dto;

import java.time.LocalDateTime;

public record CustomerDto(
        Integer id,
        Integer customerGroupId,
        Integer defaultAddressId,
        String email,
        String emailCanonical,
        String firstName,
        String lastName,
        LocalDateTime birthday,
        String gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String phoneNumber,
        Short subscribedToNewsletter
) {
}
