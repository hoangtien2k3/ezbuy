package com.ezbuy.settingservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "cust_type")
public class CustType extends EntityBase {
    private String custType;
    private String name;
    private String groupType;
    private Integer tax;
    private String plan;
    private String description;
}
