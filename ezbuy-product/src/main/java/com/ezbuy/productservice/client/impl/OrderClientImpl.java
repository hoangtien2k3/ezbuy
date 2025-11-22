package com.ezbuy.productservice.client.impl;

import com.ezbuy.productservice.client.OrderClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {

    private final WebClient orderClient;
    private final BaseRestClient baseRestClient;
    private final ObjectMapperUtil objectMapperUtil;

    public OrderClientImpl(@Qualifier(value = "orderClient") WebClient orderClient,
                           BaseRestClient baseRestClient,
                           ObjectMapperUtil objectMapperUtil) {
        this.orderClient = orderClient;
        this.baseRestClient = baseRestClient;
        this.objectMapperUtil = objectMapperUtil;
    }
}
