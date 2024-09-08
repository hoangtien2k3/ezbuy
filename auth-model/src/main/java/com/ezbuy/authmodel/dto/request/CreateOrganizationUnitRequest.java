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
package com.ezbuy.authmodel.dto.request;

import com.ezbuy.authmodel.dto.BusinessRegisInforDto;
import com.ezbuy.authmodel.dto.PresentativeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationUnitRequest {
    private String organizationUnitId;
    private String name; // tên don vi
    private String shortName; // tên viết tắt
    private String parentId; // mã đơn vị cha
    private String code; // mã đơn vị
    private String fieldOfActivity; // Lĩnh vực hoạt động
    private String unitTypeId; // Mã loại đơn vị
    private String address; // địa chỉ
    private String description; // Chức năng nhiệm vụ
    private String leaderId; // Lãnh đạo
    private String placeName; // địa danh
    private String leaderName;
    private String unitTypeName;
    private BusinessRegisInforDto businessRegistrationInfor; // Thông tin đăng kí kinh doanh
    private PresentativeDto presentative; // Thông tin người đại diện
}
