package com.ezbuy.ratingmodel.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRequest extends PageRequest {
    private String id; // id ban ghi rating
    private String ratingTypeCode; // ma loai danh gia
    private String targetId; // id doi tuong duoc danh gia
    private String username; // tai khoan dang nhap
    private String custName; // ten khach hang
    private Long rating; // so diem danh gia
    private String content; // noi dung danh gia

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime ratingDate; // thoi diem danh gia

    private Integer hasImage;
    private Integer hasVideo;
    private Integer status; // trang thai 1: hieu luc , 0 : khong hieu luc
    private String state; // trang thai phe duyet wait_approve, approved, wait_approve_fix
    private Integer displayStatus; // trang thai hien thi
    private Integer sumRateStatus; // tinh thong tin
    private String targetUser; // ADMIN: quan tri vien , CUSTOMER: khach hang
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String approveBy;
    private LocalDateTime approveAt;
}
