package com.ezbuy.ordermodel.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetOrderTransactionToRequest {
    private String orderCode;   //ma don hang
    private String username;    //username
    private String idNo;        //so giay to
    private String phone;       //email
    private String from;        //tu ngay
    private String to;          //den ngay
    private Integer pageIndex;
    private Integer pageSize;
    private Integer limit;
    private String sort;
}
