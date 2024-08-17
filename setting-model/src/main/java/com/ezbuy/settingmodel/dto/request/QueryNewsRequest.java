package com.ezbuy.settingmodel.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class QueryNewsRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;
    private String name;
    private Integer sourceType;
    private Integer newsType;
    private Integer status;
    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
}
