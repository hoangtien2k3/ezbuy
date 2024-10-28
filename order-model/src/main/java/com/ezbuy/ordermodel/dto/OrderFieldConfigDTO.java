package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class OrderFieldConfigDTO {

    private Integer name;
    private Integer email;
    private Integer phone;
    private Integer identity;
    private Integer taxCode;

    private Integer areaCode;

    private Integer detailAddress;

    private Integer fromStaff; // nhan vien moi gioi

    private Integer amStaff; // ma AM ho tro
}
