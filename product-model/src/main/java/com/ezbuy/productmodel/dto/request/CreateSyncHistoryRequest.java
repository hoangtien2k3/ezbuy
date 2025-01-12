package com.ezbuy.productmodel.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSyncHistoryRequest {
    private String orgId; // id doanh nghiep organization id
    private String idNo; // mst doanh nghiep
    private String action; // tac dong INSERT, UPDATE, DELETE
    private String serviceType; // loai dong bo: SPECIFICT - dong bo theo dv chi dinh, ALL_BY_ORG - dong bo theo
    // dv su dung
    private String dstService; // danh sach dinh danh doi tuong VCONTRACT, SINVOICE, MYSIGN ..
    private String syncType; // loai dong bo: ALL - tat ca, EVENT - theo su kien
    private String objectType; // loai du lieu dong bo: PRODUCT
    private String requestId; // id request
    private String responseData; // data tu api tra ve
    private LocalDateTime responseAt; // thoi gian api tra ket qua
    private String errorCode;
    private String responseMessage;
    private Integer retry; // so la retry
    private String state; // trang thai: waitSync - cho dong bo, done - thanh cong, fail - that bai
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private String ids; // danh sach id bang sme_product.product
}
