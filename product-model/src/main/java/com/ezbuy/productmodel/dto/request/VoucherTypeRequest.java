package com.ezbuy.productmodel.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherTypeRequest {
    private String code;
    private String name;
    private Integer priorityLevel;
    private String description;
    private String actionType;
    private String actionValue;
    private String state;
    private Integer status;
    private String conditionUse;
    private String payment;
    private LocalDate createFromDate;
    private LocalDate createToDate;
}
