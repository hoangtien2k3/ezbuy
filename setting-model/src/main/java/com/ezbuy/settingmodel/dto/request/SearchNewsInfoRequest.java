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
