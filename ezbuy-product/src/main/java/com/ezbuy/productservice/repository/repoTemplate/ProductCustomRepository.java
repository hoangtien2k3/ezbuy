package com.ezbuy.productservice.repository.repoTemplate;

import java.util.List;
import java.util.UUID;

import com.ezbuy.productservice.model.dto.request.SearchProductRequest;
import com.ezbuy.productservice.model.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCustomRepository {
    Flux<Product> searchProduct(SearchProductRequest request, String organizationId);

    Mono<Integer> countProduct(SearchProductRequest request, String organizationId);

    Flux<Product> getProductByIdAndOrganizationIdAndTransId(List<String> id, UUID organizationId, Integer offset, Integer limit, String transactionId);
}
