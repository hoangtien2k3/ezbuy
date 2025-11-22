package com.ezbuy.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFieldConfig {

    private Long id;

    private Integer name;
    private Integer email;
    private Integer phone;
    private Integer identity;
    private Integer taxCode;

    private Integer areaCode;
    private Integer detailAddress;

    private Integer status;

    private LocalDateTime createAt;

    private String createdBy;

    private LocalDateTime updateAt;

    private String updateBy;
    private Integer fromStaff;
    private String serviceId;
    private String serviceAlias;
    private Integer amStaff;
}
