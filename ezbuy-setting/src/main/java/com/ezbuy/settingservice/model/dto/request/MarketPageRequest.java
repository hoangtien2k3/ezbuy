package com.ezbuy.settingservice.model.dto.request;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class MarketPageRequest {
    private String id;
    private String serviceId;
    private String serviceAlias; // alias of service PYCXXX/LuongToanTrinhScontract

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    private String code;
    private String name;
    private String description;
    private Integer status;
}
