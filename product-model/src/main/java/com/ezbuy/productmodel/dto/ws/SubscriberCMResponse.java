package com.ezbuy.productmodel.dto.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties
public class SubscriberCMResponse {

    private String isdn;

    @XmlElement(name = "subId")
    private String subscriberId;

    @XmlElement(name = "offerId")
    private String productId;

    @XmlElement(name = "productCode")
    private String productCode;

    @XmlElement(name = "staDatetime")
    private String staDatetime;

    @XmlElement(name = "productName")
    private String productName;

    @XmlElement(name = "status")
    private Integer status;

    @XmlElement(name = "accountId")
    private Long accountId;

    @XmlElement(name = "telecomServiceId")
    private Long telecomServiceId;

    @XmlElement(name = "listSubAttDTO")
    private List<SubAttCMResponse> listSubAttDTO;

    @XmlElement(name = "userInfoDTO")
    private UserInfoCMResponse userInfoDTO;

    @XmlElement(name = "telecomServiceAlias")
    private String telecomServiceAlias;
}
