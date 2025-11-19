package com.ezbuy.productservice.service;

import com.ezbuy.authmodel.model.OrganizationUnit;
import com.ezbuy.productmodel.dto.ProductImportDTO;
import com.ezbuy.productmodel.dto.ProductImportListDTO;
import com.ezbuy.productmodel.dto.request.*;
import com.ezbuy.productmodel.dto.response.GetProductInfoResponse;
import com.ezbuy.productmodel.dto.response.ProductSearchResult;
import com.ezbuy.productmodel.model.Product;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ProductService {

    /**
     * Hàm tìm kiếm hàng hóa
     *
     * @return
     */
    Mono<ProductSearchResult> searchProduct(SearchProductRequest request, String organizationId);

    /**
     * Ham xem thong tin chi tiet hang hoa
     *
     * @param productId
     * @return
     */
    Mono<Product> detailProduct(String productId);

    /**
     * Ham tai file import mau cho hang hoa
     *
     * @return
     */
    Mono<ResponseEntity<Resource>> getImportProductTemplate();

    /**
     * Ham tra ve ket qua import file hang hoa
     *
     * @param items
     * @return
     */
    Mono<ResponseEntity<byte[]>> downloadImportResult(List<ProductImportDTO> items);

    /**
     * Ham validate thong tin import
     *
     * @param filePart
     * @return
     */
    Mono<ProductImportListDTO> validateImportProduct(FilePart filePart);

    /**
     * Ham them moi thong tin hang hoa
     *
     * @param product
     * @param organizationId
     * @return
     */
    public Mono<DataResponse<Product>> createProduct(Product product, String organizationId);

    /**
     * Ham cap nhat thong tin hang hoa
     *
     * @param product
     * @return
     */
    Mono<DataResponse<Boolean>> updateProduct(Product product);

    /**
     * Ham xoa hang hoa
     *
     * @param productId
     * @return
     */
    Mono<DataResponse<Boolean>> deleteProduct(String productId);

    /**
     * Ham khoa hang hoa
     *
     * @param request
     * @return
     */
    Mono<DataResponse<Boolean>> lockProduct(LockProductRequest request);

    /**
     * Ham import hang hoa
     *
     * @param filePart
     * @return
     */
    Mono<ProductImportListDTO> importProduct(FilePart filePart, String organizationId);

    /**
     * Ham khoa nhieu ban ghi hang hoa
     *
     * @param request
     * @return
     */
    Mono<Boolean> lockMultiProduct(LockMultiProductRequest request);

    /**
     * Ham them moi hang hoa
     *
     * @param product
     * @param unitList
     * @param organizationId
     * @return
     */
    Mono<DataResponse<Product>> handleCreateProduct(
            Product product, List<OrganizationUnit> unitList, String organizationId);

    /**
     *
     * @param request
     * @return
     */
    Mono<GetProductInfoResponse> getProductInfo(GetProductInfoRequest request);

    Mono<Resource> exportReport(QueryReport request);
}
