package com.ezbuy.customer.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("customer_id")
    private Long customerId; // id khach hang
    private String street; // ten duong
    private String province; // tinh
    private String district; // huyen
    private String precinct; // xa
    private String city; // thanh pho
    @Column("address_detail")
    private String addressDetail;
    private String postCode; // ma buu dien
    @Column("country_code")
    private String countryCode; // ma quoc gia
    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao
    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat
    private String company; // ten cong ty
}