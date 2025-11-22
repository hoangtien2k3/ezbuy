package com.ezbuy.settingservice.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SearchGroupNewsRequest {
    private String id;
    private String name; // ten nhom tin tuc
    private String code; // ma nhom tin tuc
    private Integer displayOrder; // thu tu sap xep
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
