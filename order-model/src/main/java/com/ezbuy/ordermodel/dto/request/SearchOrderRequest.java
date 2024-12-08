package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class SearchOrderRequest {
    private Integer pageSize;   //kich thuoc page
    private Integer pageIndex;  //chi muc page
    private String state;   //trang thai
    private String sort;    //sap xep theo
    private String individualId; //id nguoi dung tren keycloak
}
