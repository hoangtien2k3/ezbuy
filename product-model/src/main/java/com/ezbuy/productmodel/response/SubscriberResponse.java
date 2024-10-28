package com.ezbuy.productmodel.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Builder
public class SubscriberResponse {

    private String isdn;
    private String idNo;
    private Long telecomServiceId;
    private String subscriberId;
    private String productId;
    private String productCode;
    private String productName;
    private Integer status;
    private LocalDate activationDate;
    private LocalDate expiredDate;
    private String address;
    private Integer groupType;
    private Long accountId;
    private String telecomServiceAlias;
}
