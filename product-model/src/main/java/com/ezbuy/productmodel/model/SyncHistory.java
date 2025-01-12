package com.ezbuy.productmodel.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sync_history")
@Builder
public class SyncHistory implements Persistable<String> {
    @Id
    private String id;
    private String orgId; // id doanh nghiep organization id
    private String idNo; // mst doanh nghiep
    private String action; // tac dong INSERT, UPDATE, DELETE
    private String serviceType; // loai dong bo: SPECIFICT - dong bo theo dv chi dinh, ALL_BY_ORG
    private String dstService; // danh sach dinh danh doi tuong VCONTRACT, SINVOICE, MYSIGN ..
    private String syncType; // loai dong bo: ALL - tat ca, EVENT - theo su kien
    private String objectType; // loai du lieu dong bo: CUSTOMER
    private String ids; // danh sach id bang sme_customer.customer
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
    private String syncTransId; // ma transId do databus tra ve de dinh danh luong dong bo

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
