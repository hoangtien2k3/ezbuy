package com.ezbuy.settingmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SearchNewsInfoRequest {
    private String id;
    private String title; // tieu de
    private String code; // ma thong tin
    private Integer displayOrder; // thu tu sap xep
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String groupNewsId; // id nhom tin tuc
    private String summary; // tom tat noi dung
    private String state; // trang thai nghiep vu

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate; // tu ngay

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate; // toi ngay

    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
    private Boolean isWeb; // kiem tra duoc goi tu dau
}
