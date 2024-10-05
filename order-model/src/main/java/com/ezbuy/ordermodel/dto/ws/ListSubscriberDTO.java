package com.ezbuy.ordermodel.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ListSubscriberDTO {

    private Long accountId;

    private Integer telecomServiceId;
    private String telecomServiceAlias; // alias cua dich vu (da check soap tra ve)

    private Long subId;
}
