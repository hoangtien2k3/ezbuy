package com.ezbuy.settingmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewsInfoRequest {
    private String title; //tieu de
    private String code; //ma thong tin
    private Integer displayOrder; //thu tu sap xep
    private Integer status; //trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String groupNewsId; //id nhom tin tuc
    private String summary; //tom tat noi dung
    private String navigatorUrl; //anh dai dien
    private String state; //trang thai nghiep vu
    private String image; //hinh anh base 64
}
