package com.ezbuy.orderservice.client;


import com.ezbuy.common.ViettelService;
import reactor.core.publisher.Mono;


public interface ProvisioningClient {
    Mono<ViettelService> callProvisioning(ViettelService dataConnect);
}
