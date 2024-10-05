package com.ezbuy.ordermodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupCAInfo {
    private String groupCode;//ma nhom
    private Long groupId;//id nhom
    private String groupName;//ten nhom
    private Long maxSub;// Ham muc thanh vien
    private Long numberSub;//So luong thanh vien
    private Long totalSign;//Tong so luong ky
    private String status;//trang thai
    private String createDate;//Ngay mua
    private List<SubGroupCaDTO> lstSubscriberCA;
    private String groupIDOcs;
    private String groupTypeOcs;
    private String mainIsdnOcs;
    private Long totalRecord;//tong so ban ghi
}
