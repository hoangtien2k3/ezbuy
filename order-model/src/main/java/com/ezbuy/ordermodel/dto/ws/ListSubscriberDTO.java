package com.ezbuy.ordermodel.dto.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
