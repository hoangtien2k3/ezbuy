package com.ezbuy.settingservice.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionSetValueDTO {
    private String code; // ma chinh sach
    private String text; // label chinh sach
    private Boolean required; // chinh sach bat buoc
    private Boolean checked; // chinh sach da tich chon
    private Boolean error; // bao loi
    private String id; // id chi tiet cau hinh nhom
    private String optionSetId; // id cau hinh nhom mapping
    private String value; // gia tri chi tiet cau hinh nhom
    private String description; // mo ta chi tiet cau hinh nhom
    private Integer status; // trang thai
    private String createBy; // nguoi tao
    private LocalDateTime createAt; // thoi gian tao
    private String updateBy; // nguoi cap nhat
    private LocalDateTime updateAt; // thoi gian cap nhat
}
