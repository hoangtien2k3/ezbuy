package com.ezbuy.productservice.controller;

import static com.ezbuy.authmodel.constants.AuthConstants.SUCCESS;
import static com.ezbuy.productmodel.constants.UrlPaths.*;
import static com.ezbuy.productmodel.constants.UrlPaths.Product.*;

import com.ezbuy.productmodel.dto.ProductImportDTO;
import com.ezbuy.productmodel.dto.request.*;
import com.ezbuy.productmodel.model.Product;
import com.ezbuy.productservice.service.ProductService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
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

    /**
     * Bo sung tra ve alias cho PYC Scontract
     *
     * @return
     */
    @GetMapping(value = GET_GROUP_AND_SERVICE_ACTIVE)
    public Mono<DataResponse> getAllServiceGroupAndTelecomServiceActive() {
        return productService
                .getAllServiceGroupAndTelecomServiceActive()
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @PostMapping(value = GET_REGISTERED_TELECOM_SERVICE_BY_ID_NO_LIST)
    public Mono<DataResponse> getAllRegisterdTelecomServicesByIdNoList(
            @RequestBody RegisteredServiceRequest registeredServiceRequest) {
        return productService
                .getAllRegisterdTelecomServicesByIdNoList(registeredServiceRequest.getIdNoList())
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @PostMapping(value = GET_REGISTERED_TELECOM_SERVICE_ALIAS_BY_ID_NO_LIST)
    public Mono<DataResponse> getAllRegisterdTelecomServicesAliasByIdNoList(
            @RequestBody RegisteredServiceRequest registeredServiceRequest) {
        return productService
                .getAllRegisterdTelecomServicesAliasByIdNoList(registeredServiceRequest.getIdNoList())
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @GetMapping(value = SEARCH_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse> searchProduct(
            SearchProductRequest request, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService
                .searchProduct(request, organizationId)
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @PostMapping(value = CREATE_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Product>>> createProduct(
            @RequestBody Product product, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.createProductSync(product, organizationId).map(rs -> ResponseEntity.ok(rs));
    }

    @PostMapping(value = UPDATE_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Boolean>>> updateProduct(
            @RequestBody Product product, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.updateProductSync(product, organizationId).map(rs -> ResponseEntity.ok(rs));
    }

    @DeleteMapping(value = DELETE_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Boolean>>> deleteProduct(
            @RequestParam String productId, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.deleteProductSync(productId, organizationId).map(rs -> ResponseEntity.ok(rs));
    }

    @GetMapping(value = DETAIL_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> detailProduct(@RequestParam String productId) {
        return productService.detailProduct(productId).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS, rs)));
    }

    @PostMapping(value = LOCK_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<Boolean>>> lockProduct(
            @RequestBody LockProductRequest request, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.lockProductSync(request, organizationId).map(rs -> ResponseEntity.ok(rs));
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
    public Mono<ResponseEntity<DataResponse>> validateImportProduct(@RequestPart("file") FilePart filePart) {
        return productService
                .validateImportProduct(filePart)
                .map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS, rs)));
    }

    @PostMapping(value = IMPORT_PRODUCT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> importProduct(
            @RequestPart("file") FilePart filePart, @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService
                .importProductSync(filePart, organizationId)
                .map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS, rs)));
    }

    @PostMapping(value = LOCK_PRODUCT_MULTI)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<List<Product>>>> lockMultiProduct(
            @RequestBody LockMultiProductRequest request,
            @RequestHeader(value = "ORGANIZATION-ID") String organizationId) {
        return productService.lockMultiProductSync(request, organizationId).map(rs -> ResponseEntity.ok(rs));
    }

    @PostMapping(value = GET_PRODUCT_INFO)
    public Mono<ResponseEntity<DataResponse>> getProductInfo(@RequestBody GetProductInfoRequest request) {
        return productService
                .getProductInfo(request)
                .map(rs -> ResponseEntity.ok(new DataResponse("common.success", rs)));
    }

    @PostMapping(value = VALIDATE_SUB_INS)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> validateSubIns(@RequestBody ValidateSubInsRequest request) {
        return productService.validateSubIns(request).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS, rs)));
    }

    @PostMapping(value = GET_LIST_AREA_INS)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> getListAreaIns(@RequestBody getListAreaInsRequest request) {
        return productService.getListAreaIns(request).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS, rs)));
    }

    @PostMapping(value = GET_DATA_REPORT)
    public Mono<ResponseEntity<DataResponse>> getDataReport(@RequestBody QueryReport request) {
        return productService
                .getDataReport(request)
                .map(rs -> ResponseEntity.ok(new DataResponse("common.success", rs)));
    }

    @PostMapping(value = EXPORT_REPORT)
    public Mono<ResponseEntity<Resource>> exportUserProfiles(@RequestBody QueryReport request) {
        return productService.exportReport(request).map(resource -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource));
    }

    /**
     * Job collect daily report
     *
     * @param request
     * @return
     */
    @PostMapping(value = CREATE_SUMMARY_REPORT)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> createSummaryReport(@RequestBody CreateSummaryRequest request) {
        return productService
                .createSummaryReport(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS, rs.getData())));
    }
}
