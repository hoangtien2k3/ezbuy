package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Organization extends EntityBase {

    @Id
    private String id;
    private String name;
    private String image;
    private String businessType;
    private LocalDateTime foundingDate;
    private String email;
    private String phone;
    private String provinceCode;
    private String districtCode;
    private String precinctCode;
    private String streetBlock;
    private Integer state;
    private Integer status;
    private String orgType;
    private String taxDepartment;
}
