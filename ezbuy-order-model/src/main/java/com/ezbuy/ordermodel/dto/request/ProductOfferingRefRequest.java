package com.ezbuy.ordermodel.dto.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOfferingRefRequest {
    private String id;
    private Long telecomServiceId;
    private String telecomServiceAlias; // Bo sung alias PYCXXX/LuongToanTrinh
}
