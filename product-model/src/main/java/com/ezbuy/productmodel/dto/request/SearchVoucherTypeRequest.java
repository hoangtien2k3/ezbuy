package com.ezbuy.productmodel.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchVoucherTypeRequest {
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
    private String code;
    private String name;
    private Integer priorityLevel;
    private String description;
    private String actionType;
    private String actionValue;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate createFromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate createToDate;

    private String state;
    private Integer status;
}
