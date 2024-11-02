package com.ezbuy.productservice.client;

import com.ezbuy.productmodel.dto.ws.CustomerSubscribeInfoCMResponse;
import com.ezbuy.productmodel.dto.ws.SubscriberCMResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CmClient {

    Mono<List<SubscriberCMResponse>> getListSubscriberByIdNo(String idNo);

    Mono<CustomerSubscribeInfoCMResponse> getCustomerSubscriberSmeInfo(Long telecomServiceId, String idNo, String isdn);
}
