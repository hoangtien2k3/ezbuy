package com.ezbuy.orderservice.client;

import com.ezbuy.sme.productmodel.model.Subscriber;
import com.ezbuy.sme.productmodel.response.ProductOfferTemplateDTO;
import com.ezbuy.sme.settingmodel.model.Telecom;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductClient {

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId);

    Mono<List<Telecom>> getTelecomByAlias(List<String> lstTelecomServiceId);

}
