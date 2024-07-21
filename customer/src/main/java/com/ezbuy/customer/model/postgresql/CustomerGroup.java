package com.ezbuy.customer.model.postgresql;

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
    private Long id; // id
    private String code; // code
    private String name; // ten
}