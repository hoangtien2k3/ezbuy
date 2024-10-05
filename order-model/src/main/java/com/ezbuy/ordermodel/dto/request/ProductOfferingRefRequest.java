package com.ezbuy.ordermodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOfferingRefRequest {
    private String id;
    private Long telecomServiceId;
    private String telecomServiceAlias; //Bo sung alias PYCXXX/LuongToanTrinh
}
