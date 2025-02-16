package com.ezbuy.productservice.controller;

import static com.ezbuy.authmodel.constants.AuthConstants.SUCCESS;
import static com.ezbuy.productmodel.constants.UrlPaths.*;
import static com.ezbuy.productmodel.constants.UrlPaths.Product.*;

import com.ezbuy.productmodel.dto.ProductImportDTO;
import com.ezbuy.productmodel.dto.ProductImportListDTO;
import com.ezbuy.productmodel.dto.request.*;
import com.ezbuy.productmodel.dto.response.GetProductInfoResponse;
import com.ezbuy.productmodel.dto.response.ProductSearchResult;
import com.ezbuy.productmodel.model.Product;
import com.ezbuy.productservice.service.ProductService;
import com.reactify.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(DEFAULT_V1_PREFIX)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = SEARCH_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<ProductSearchResult>> searchProduct(
            SearchProductRequest request, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.searchProduct(request, organizationId).map(DataResponse::success);
    }

    @GetMapping(value = DETAIL_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Product>>> detailProduct(@RequestParam String productId) {
        return productService.detailProduct(productId).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS, rs)));
    }

    @GetMapping(value = PRODUCT_IMPORT_TEMPLATE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<Resource>> getImportProductTemplate() {
        return productService.getImportProductTemplate();
    }

    @PostMapping(value = PRODUCT_DOWNLOAD_IMPORT_RESULT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<byte[]>> downloadImportResult(@RequestBody List<ProductImportDTO> items) {
        return productService.downloadImportResult(items);
    }

    @PostMapping(value = PRODUCT_VALIDATE_IMPORT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<ProductImportListDTO>>> validateImportProduct(
            @RequestPart("file") FilePart filePart) {
        return productService.validateImportProduct(filePart).map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @PostMapping(value = GET_PRODUCT_INFO)
    public Mono<ResponseEntity<DataResponse<GetProductInfoResponse>>> getProductInfo(
            @RequestBody GetProductInfoRequest request) {
        return productService.getProductInfo(request).map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @PostMapping(value = EXPORT_REPORT)
    public Mono<ResponseEntity<Resource>> exportUserProfiles(@RequestBody QueryReport request) {
        return productService.exportReport(request).map(resource -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource));
    }
}
