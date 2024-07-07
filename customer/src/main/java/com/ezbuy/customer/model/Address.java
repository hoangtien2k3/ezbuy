package com.ezbuy.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("address")
public class Address {
    @Id
    private Integer id;
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String street;
    private String company;
    private String city;
    private String postcode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String countryCode;
    private String provinceCode;
    private String provinceName;
}