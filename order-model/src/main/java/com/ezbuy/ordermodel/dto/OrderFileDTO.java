package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class OrderFileDTO {
    private String contentType;
    private String fileCode;
    private String fileName;
    private String recordTypeId;
    private String url;
    private String server;
    private String base64; // file upload dinh dang base64
    private String telecomServiceId; // id dich vu
    private String productOfferingCode; // ma goi cuoc
}
