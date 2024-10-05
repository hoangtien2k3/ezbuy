package com.ezbuy.productservice.client;

import com.ezbuy.sme.productmodel.dto.ws.CustomerSubscribeInfoCMResponse;
import com.ezbuy.sme.productmodel.dto.ws.SubscriberCMResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CmClient {

    Mono<List<SubscriberCMResponse>> getListSubscriberByIdNo(String idNo);

    Mono<CustomerSubscribeInfoCMResponse> getCustomerSubscriberSmeInfo(Long telecomServiceId, String idNo, String isdn);
}
