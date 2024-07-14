package com.ezbuy.customermodel.model;

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
    private Long id; // id
    private Long customerId; // id khach hang
    private String street; // ten duong
    private String province; // tinh
    private String district; // huyen
    private String precinct; // xa
    private String city; // thanh pho
    private String addressDetail;
    private String postCode; // ma buu dien
    private String countryCode; // ma quoc gia
    private LocalDateTime createdAt; // thoi gian tao
    private LocalDateTime updatedAt; // thoi gian cap nhat
    private String company; // ten cong ty
}