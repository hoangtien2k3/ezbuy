package com.ezbuy.cartservice.infrastructure.client.impl;

import com.ezbuy.cartservice.application.dto.FilterProductTemplateDTO;
import com.ezbuy.cartservice.application.dto.response.ListProductOfferResponse;
import com.ezbuy.cartservice.infrastructure.client.ProductClient;
import com.ezbuy.core.client.BaseRestClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class ProductClientImpl implements ProductClient {

    private final WebClient product;
    private final BaseRestClient baseRestClient;

    public ProductClientImpl(@Qualifier("productServiceClient") WebClient product,
                             BaseRestClient baseRestClient) {
        this.product = product;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds) {
        FilterProductTemplateDTO filterProductTemplateDTO = new FilterProductTemplateDTO();
        filterProductTemplateDTO.setListId(templateIds);
        return baseRestClient
                .post(product, "/v1/filter-product-template", null, filterProductTemplateDTO, ListProductOfferResponse.class)
                .mapNotNull(optional -> optional.orElse(null));
    }
}
