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
package com.ezbuy.ratingmodel.request;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindRatingRequest extends PageRequest {
    private String ratingTypeCode; // id
    private String targetId;
    private String username; // userName
    private String custName; // custName
    private String content; // noi dung danh gia
    private Integer rating; // so diem danh gia
    private Integer hasImage; // 1 la co anh 0 la khong co anh
    private Integer hasVideo; // 1 la co video 0 la khong co video
    private Integer ratingStatus; // 1 la hoat dong 0 la khong hoat dong
    private String state; // wait_approve, approved, wait_approve_fix
    private Integer displayStatus; // co hien thi auto khong hien thi
    private Integer sumRateStatus; // co danh dau co dua vao de tong hop con so danh gia khong
    private String targetUser; // comment cau hinh CUSTOMER, ADMIN
    private BigInteger fromDate; // tu ngay
    private BigInteger toDate; // toi ngay
}
