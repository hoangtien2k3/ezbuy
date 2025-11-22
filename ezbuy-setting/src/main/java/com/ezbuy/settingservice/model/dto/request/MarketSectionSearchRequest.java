package com.ezbuy.settingservice.model.dto.request;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
