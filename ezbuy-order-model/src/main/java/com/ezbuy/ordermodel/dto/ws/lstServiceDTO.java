package com.ezbuy.ordermodel.dto.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@XmlRootElement(name = "lstService")
public class lstServiceDTO {
    private String serviceType; // DS ma dich vu (service_alias ben bccs_product.telecom_service, vi dụ: M, F)
    private Integer prepaid; // loai dich vu: 1- tra truoc, 0- tra sau
    private Integer alowReuser; // 1 co, 0 khong. co dung lai chung tu hay khong
}
