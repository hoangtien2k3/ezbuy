package com.ezbuy.customer.domain.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("customer_group")
public class CustomerGroup {
    @Id
    private Integer id;
    private String code;
    private String name;
}