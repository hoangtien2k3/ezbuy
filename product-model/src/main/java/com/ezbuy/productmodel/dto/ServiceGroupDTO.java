package com.ezbuy.productmodel.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceGroupDTO {
    private String id; // id
    private String name; // ten nhom dich vu
    private String code; // ma nhom dich vu
    private Integer displayOrder; // thu tu hien thi
    private Integer status; // trang thai: 1 - Hieu luc, 0 - Khong hieu luc
    private String createBy; // nguoi tao
    private LocalDateTime createAt; // ngay tao
    private String updateBy; // nguoi cap nhat
    private LocalDateTime updateAt; // ngay cap nhat
}
