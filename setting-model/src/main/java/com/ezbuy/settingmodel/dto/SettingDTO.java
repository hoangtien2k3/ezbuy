package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingDTO {
    private String id; //id
    private String code; //ma cai dat
    private String value; //gia tri
    private String description; //chi tiet
    private Integer status; //trang thai
    private String createBy; //tao boi
    private String updateBy; //update boi
    private LocalDateTime createAt; //thoi gian tao
    private LocalDateTime updateAt; //thoi gian cap nhat
}
