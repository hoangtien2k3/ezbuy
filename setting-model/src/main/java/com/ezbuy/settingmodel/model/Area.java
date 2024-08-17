package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
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
//    private Integer status;
//    private String createBy;
//    private String updateBy;
//    private LocalDateTime createAt;
//    private LocalDateTime updateAt;
}
