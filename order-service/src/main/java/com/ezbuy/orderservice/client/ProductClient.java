package com.ezbuy.orderservice.client;

import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productmodel.dto.response.ProductOfferTemplateDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ProductClient {

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId);

    Mono<List<Telecom>> getTelecomByAlias(List<String> lstTelecomServiceId);
}
