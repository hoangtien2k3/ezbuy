package com.ezbuy.productservice.service;

import com.ezbuy.core.model.response.DataResponse;
import java.util.List;

import com.ezbuy.productservice.model.dto.OrganizationUnit;
import com.ezbuy.productservice.model.dto.request.GetProductInfoRequest;
import com.ezbuy.productservice.model.dto.request.LockMultiProductRequest;
import com.ezbuy.productservice.model.dto.request.LockProductRequest;
import com.ezbuy.productservice.model.dto.request.SearchProductRequest;
import com.ezbuy.productservice.model.dto.response.GetProductInfoResponse;
import com.ezbuy.productservice.model.dto.response.ProductSearchResult;
import com.ezbuy.productservice.model.entity.Product;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<ProductSearchResult> searchProduct(SearchProductRequest request, String organizationId);

    Mono<Product> detailProduct(String productId);

    Mono<DataResponse<Boolean>> updateProduct(Product product);

    Mono<DataResponse<Boolean>> deleteProduct(String productId);

    Mono<DataResponse<Boolean>> lockProduct(LockProductRequest request);

    Mono<Boolean> lockMultiProduct(LockMultiProductRequest request);

    Mono<DataResponse<Product>> handleCreateProduct(Product product, List<OrganizationUnit> unitList, String organizationId);

    Mono<GetProductInfoResponse> getProductInfo(GetProductInfoRequest request);
}
