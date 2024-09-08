/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.ratingmodel.dto;

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
public class RatingDTO {
    private String id; // id ban ghi rating
    private String ratingTypeCode; // ma loai danh gia
    private String targetId; // id doi tuong duoc danh gia
    private String username; // tai khoan dang nhap
    private String custName; // ten khach hang
    private Float rating; // so diem danh gia
    private String content; // noi dung danh gia

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime ratingDate; // thoi diem danh gia

    private Integer hasImage; // chua anh
    private Integer hasVideo; // chua video
    private Integer status; // trang thai 1: hieu luc , 0 : khong hieu luc
    private String state; // trang thai phe duyet wait_approve, approved, wait_approve_fix
    private Integer displayStatus; // trang thai hien thi
    private Integer sumRateStatus; // tinh thong tin
    private String targetUser; // ADMIN: quan tri vien , CUSTOMER: khach hang
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String service_alias;
}
