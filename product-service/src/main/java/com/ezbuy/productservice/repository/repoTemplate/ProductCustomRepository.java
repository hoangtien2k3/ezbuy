package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.sme.productmodel.model.Product;
import com.ezbuy.sme.productmodel.request.SearchProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductCustomRepository {
    Flux<Product> searchProduct(SearchProductRequest request, String organizationId);

    Mono<Integer> countProduct(SearchProductRequest request, String organizationId);

    Flux<Product> getProductByIdAndOrganizationIdAndTransId(List<String> id, UUID organizationId, Integer offset, Integer limit, String transactionId);
}
