package com.ezbuy.settingmodel.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchMarketInfoRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    private String serviceId;
    private String serviceAlias;
    private String title;
    private String slogan;

    private Integer pageIndex;
    private Integer pageSize;
    private String sort;
}
