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
