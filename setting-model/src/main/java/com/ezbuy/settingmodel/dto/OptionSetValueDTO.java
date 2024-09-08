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
package com.ezbuy.settingmodel.dto;

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
