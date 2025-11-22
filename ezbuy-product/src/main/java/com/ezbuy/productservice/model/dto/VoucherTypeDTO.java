package com.ezbuy.productservice.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherTypeDTO {
    private String id;
    private String code;
    private String name;
    private Integer priorityLevel;
    private String description;
    private String actionType;
    private String actionValue;
    private String state;
    private Integer status;
    private String payment;
    private String conditionUse;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
}
