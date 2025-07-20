package com.ezbuy.settingmodel.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMarketPageRequest extends PageRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    private String serviceId;
    private String serviceAlias;
    private String nameService;
    private String code;
    private String name;
    private String description;
    private Integer status;
}
