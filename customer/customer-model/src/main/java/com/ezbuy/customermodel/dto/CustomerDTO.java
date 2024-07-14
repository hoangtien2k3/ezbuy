package com.ezbuy.customermodel.dto;

import com.ezbuy.customermodel.model.Customer;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CustomerDTO(
        Long id,
        Long customerGroupId,
        String email,
        String username,
        String firstName,
        String lastName,
        String displayName,
        LocalDateTime birthday,
        String imageUrl,
        String phoneNumber,
        String gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer isVerifiedEmail,
        Boolean subscribedToNewsletter
) {
    public static CustomerDTO fromModel(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .customerGroupId(customer.getCustomerGroupId())
                .email(customer.getEmail())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .birthday(customer.getBirthday())
                .imageUrl(customer.getImageUrl())
                .phoneNumber(customer.getPhoneNumber())
                .gender(customer.getGender())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .isVerifiedEmail(customer.getIsVerifiedEmail())
                .subscribedToNewsletter(customer.getSubscribedToNewsletter())
                .build();
    }
}
