package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionSetDTO {
    private String id;
    private String code; //ma cau hinh nhom
    private String description; //mo ta cau hinh nhom
    private Integer status; //trang thai: 0-Khong hieu luc, 1-Hieu luc
    private String createBy; //nguoi tao
    private LocalDateTime createAt;//thoi gian tao
    private String updateBy; //nguoi cap nhat
    private LocalDateTime updateAt; //thoi gian cap nhat
}
