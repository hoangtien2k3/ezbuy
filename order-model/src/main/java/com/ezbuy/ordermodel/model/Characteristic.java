package com.ezbuy.ordermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "characteristic")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Characteristic {

    private String id;

    private String name;

    private String valueType;

    private String value;

    private String code;

    private String orderItemId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private Integer status;
}
