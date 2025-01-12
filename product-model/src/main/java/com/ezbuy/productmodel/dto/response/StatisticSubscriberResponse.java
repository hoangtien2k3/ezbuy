package com.ezbuy.productmodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticSubscriberResponse {
    private Long telecomServiceId;
    private String telecomServiceAlias; // bo sung them Alias cho PYC Scontract
    private Long totalSubscriber;
    private Long totalAboutExpiredSubscriber;
    private Long totalExpiredSubscriber;
    private String image;
}
