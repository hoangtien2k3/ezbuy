package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class ServiceInfo {
    private Integer maxFileSize; // kich thuoc file toi da
    private Integer prepaid; // tra truoc, tra sau, 1-tra truoc, 2- trau sau
    private Integer serviceId; // id dich vu
    private String serviceType; // Ma dich vu BCCS
    private String serviceTypeName; // Ten dich vu
    private Integer telecomServiceId; // id dich vu
}
