package com.ezbuy.settingservice.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingDTO {
    private String id; // id
    private String code; // ma cai dat
    private String value; // gia tri
    private String description; // chi tiet
    private Integer status; // trang thai
    private String createBy; // tao boi
    private String updateBy; // update boi
    private LocalDateTime createAt; // thoi gian tao
    private LocalDateTime updateAt; // thoi gian cap nhat
}
