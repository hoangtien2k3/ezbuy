package com.ezbuy.customer.model.postgresql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("customer_group")
public class CustomerGroup {
    @Id
    private Long id; // id

    private String code; // code
    private String name; // ten
}
