package com.ezbuy.productservice.controller;

import com.ezbuy.productservice.model.dto.request.GetProductInfoRequest;
import com.ezbuy.productservice.model.dto.request.LockMultiProductRequest;
import com.ezbuy.productservice.model.dto.request.LockProductRequest;
import com.ezbuy.productservice.model.dto.request.SearchProductRequest;
import com.ezbuy.productservice.model.dto.response.GetProductInfoResponse;
import com.ezbuy.productservice.model.dto.response.ProductSearchResult;
import com.ezbuy.productservice.model.entity.Product;
import com.ezbuy.productservice.service.ProductService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<ProductSearchResult>>> searchProduct(
            SearchProductRequest request, @RequestHeader(value = "organization-id") String organizationId) {
        return productService.searchProduct(request, organizationId).map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Product>>> detailProduct(@RequestParam String productId) {
        return productService.detailProduct(productId).map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @PostMapping("/get-product-info")
    public Mono<ResponseEntity<DataResponse<GetProductInfoResponse>>> getProductInfo(
            @RequestBody GetProductInfoRequest request) {
        return productService.getProductInfo(request).map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @PostMapping("/update-product")
    public Mono<ResponseEntity<DataResponse<Boolean>>> updateProduct(@RequestBody Product request) {
        return productService.updateProduct(request).map(ResponseEntity::ok);
    }

    @PostMapping("/delete-product")
    public Mono<ResponseEntity<DataResponse<Boolean>>> deleteProduct(@RequestParam String productId) {
        return productService.deleteProduct(productId).map(ResponseEntity::ok);
    }

    @PostMapping("/lock-product")
    public Mono<ResponseEntity<DataResponse<Boolean>>> lockProduct(@RequestBody LockProductRequest request) {
        return productService.lockProduct(request).map(ResponseEntity::ok);
    }

    @PostMapping("/lock-multi-product")
    public Mono<ResponseEntity<Boolean>> lockMultiProduct(@RequestBody LockMultiProductRequest request) {
        return productService.lockMultiProduct(request).map(ResponseEntity::ok);
    }
}
