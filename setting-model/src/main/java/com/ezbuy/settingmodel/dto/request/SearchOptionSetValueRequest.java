package com.ezbuy.settingmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SearchOptionSetValueRequest {
    private String id; // id option set value
    private String optionSetId; // id option set mapping
    private String value; // gia tri
    private String code; // ma option set value
    private String description; // mo ta option set value
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate; // tu ngay

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate; // toi ngay

    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
