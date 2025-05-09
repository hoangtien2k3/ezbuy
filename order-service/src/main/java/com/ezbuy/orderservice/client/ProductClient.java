package com.ezbuy.orderservice.client;

import com.ezbuy.productmodel.dto.response.ProductOfferTemplateDTO;
import com.ezbuy.settingmodel.model.Telecom;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ProductClient {

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    Mono<List<Telecom>> getTelecomByAlias(List<String> lstTelecomServiceId);
}
