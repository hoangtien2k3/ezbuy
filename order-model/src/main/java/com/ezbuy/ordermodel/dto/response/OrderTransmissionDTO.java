package com.ezbuy.ordermodel.dto.response;

import lombok.Data;

@Data
public class OrderTransmissionDTO {
    private String id;
    private String orderCode;
    private Long totalFee; // tong tien
    private String currency; // don vi tien
    private Integer status;
    private Integer state;
    private String detailAddress;
    private String type;
    private Integer rating;
    private String duration;
    private String idNo;
    private String name;
    private String email;
    private String phone;
    private Object createAt; // ngay tao
    private Object createBy;
    private Object updateAt;
    private Object updateBy;
}
