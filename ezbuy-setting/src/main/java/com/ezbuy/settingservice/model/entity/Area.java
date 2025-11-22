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
@Table(name = "area")
public class Area extends EntityBase {
    private String areaCode;
    private String parentCode;
    private String province;
    private String district;
    private String precinct;
    private String streetBlock;
    private String fullName;
    private String name;
    private String center;
}
