package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.ws.GetCustomerSubscriberSmeInfoResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface CmClient {

    Mono<Optional<GetCustomerSubscriberSmeInfoResponse>> getCustomerSubscriberSmeInfo(
            String idNo, String isdn, String telecomServiceId);
}
