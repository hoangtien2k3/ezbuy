package com.ezbuy.settingmodel.request;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class SearchSettingRequest extends PageRequest {
    private String code; // Ma code
    private Integer status; // Trang thai

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate; // Tu ngay

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate; // Den ngay
}
