package com.ezbuy.customer.domain.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("customer")
public class Customer {
    @Id
    private Integer id;
    private Integer customerGroupId;
    private Integer defaultAddressId;
    private String email;
    private String emailCanonical;
    private String firstName;
    private String lastName;
    private LocalDateTime birthday;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String phoneNumber;
    private Boolean subscribedToNewsletter;
}