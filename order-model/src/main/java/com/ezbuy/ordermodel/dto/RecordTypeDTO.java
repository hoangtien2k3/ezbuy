package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class RecordTypeDTO {
    private Long electronicSign;
    private String recordCode;
    private Long sourceId;
    private Long recordId;
    private String recordName;
    private Long reqScan;
    private Long serviceId;
    private String serviceType;
    private String serviceTypeName;
    private String recordType;
}

