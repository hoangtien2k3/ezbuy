package com.ezbuy.settingmodel.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MarketSectionSearchRequest extends PageRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;
    private String type;
    private String code;
    private String name;
    private String description;
    private String displayOrder;
    private String data;
    private Long status;
}
