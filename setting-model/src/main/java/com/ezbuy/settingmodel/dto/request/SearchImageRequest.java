package com.ezbuy.settingmodel.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchImageRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;
    private String name;
    private Integer pageIndex;
    private Integer pageSize;
    private String sort;
}
