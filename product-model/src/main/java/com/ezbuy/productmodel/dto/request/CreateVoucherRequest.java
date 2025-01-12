package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherRequest {
    private String code; //ma voucher
    private String voucherTypeId; //id bang voucher_type
    private String batchId; //id bang voucher_batch
    private LocalDateTime expiredDate; //ngay het han
    private Integer expiredPeriod; //ngay het han
    private String state; //trang thai
    private String voucherId; //id voucher
    private String organizationId; //id to chuc
    private String userId;//id user
    private List<String> voucherTypeCodeList; //danh sach voucher type code trong nhom voucher
}
