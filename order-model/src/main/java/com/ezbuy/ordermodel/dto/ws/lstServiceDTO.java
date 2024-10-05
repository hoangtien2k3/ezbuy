package com.ezbuy.ordermodel.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "lstService")
public class lstServiceDTO {
    private String serviceType;// DS ma dich vu (service_alias ben bccs_product.telecom_service, vi dụ: M, F)
    private Integer prepaid; //loai dich vu: 1- tra truoc, 0- tra sau
    private Integer alowReuser;// 1 co, 0 khong. co dung lai chung tu hay khong
}
