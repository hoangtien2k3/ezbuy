package com.ezbuy.productmodel.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class StaffDTO {
    private String address;
    private String areaCode;
    private String bankplusBankCode;
    private String bankplusMobile;
    private String channelTypeId;
    private String tel;
    private Long staffid;
    private Long shopId;
    private String staffCode;
    private String status;
    private String precinct;
    private String province;
    private String district;
    private Date birthday;
}
