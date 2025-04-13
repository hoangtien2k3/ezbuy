package com.ezbuy.productservice.service.impl;

import static com.ezbuy.authmodel.constants.AuthConstants.STATUS_ACTIVE;
import static com.ezbuy.productmodel.constants.Constants.*;
import static com.ezbuy.productmodel.constants.Constants.LOCK_STATUS.LOCK_STATUS_LOCK;
import static com.ezbuy.productmodel.constants.Constants.LOCK_STATUS.LOCK_STATUS_UNLOCK;
import static com.ezbuy.productmodel.constants.Constants.Message.FAIL;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.productmodel.constants.TemplateConstants.*;

import com.ezbuy.authmodel.constants.AuthConstants;
import com.ezbuy.authmodel.model.OrganizationUnit;
import com.ezbuy.productmodel.dto.*;
import com.ezbuy.productmodel.dto.request.*;
import com.ezbuy.productmodel.dto.response.GetProductInfoResponse;
import com.ezbuy.productmodel.dto.response.ProductSearchResult;
import com.ezbuy.productmodel.model.Product;
import com.ezbuy.productservice.client.AuthClient;
import com.ezbuy.productservice.repository.ProductRepository;
import com.ezbuy.productservice.repository.repoTemplate.ProductCustomRepository;
import com.ezbuy.productservice.service.ProductService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ModelMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductCustomRepository productCustomRepository;
    private final ProductRepository productRepository;
    private final AuthClient authClient;

    private static final String SYNC_TYPE = "PRODUCT";
    private static final String ORGANIZATION = "ORGANIZATION";
    private static final String NOT_FOUND = "product.input.notFound";
    private static final String PRODUCT_ID = "product.organization.id";
    private static final String PRODUCT = "PRODUCT";
    private static final String COMMON_ERROR = "common.error";

    private Mono<Boolean> validateRequestCreateProduct(Product product, boolean isCreate) {
        product.trim();
        // check code trong
        if (DataUtil.isNullOrEmpty(product.getCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.empty"));
        }
        // check ten trong
        if (DataUtil.isNullOrEmpty(product.getName())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.name.empty"));
        }
        // check Thue GTGT trong
        if (DataUtil.isNullOrEmpty(product.getTaxRatio())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.tax.ratio.empty"));
        }
        // check max length code 15 ky tu
        if (product.getCode().length() > 15) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.length"));
        }
        // check max length ten 500 ky tu
        if (product.getName().length() > 500) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.name.length"));
        }
        // check max length don vi tinh 150 ky tu
        if (!DataUtil.isNullOrEmpty(product.getUnit()) && product.getUnit().length() > 150) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.unit.length"));
        }
        // check gia tri don gia nhap
        if (product.getPriceImport() != null
                && (product.getPriceImport() < 0D || product.getPriceImport() >= 10000000000D)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.price.import.invalid"));
        }
        // check gia tri don gia ban
        if (product.getPriceExport() != null
                && (product.getPriceExport() < 0D || product.getPriceExport() >= 10000000000D)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.price.export.invalid"));
        }
        // check gia tri chiet khau
        if (product.getDiscount() != null && (product.getDiscount() < 0D || product.getDiscount() >= 10000000000D)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.discount.invalid"));
        }
        // check gia tri Thue GTGT hop le
        if (!TAX_RATIO_LIST.contains(product.getTaxRatio())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.tax.ratio.invalid"));
        }
        // check gia tri Ti le & theo doanh thu hop le
        if (!DataUtil.isNullOrEmpty(product.getRevenueRatio())
                && !REVENUE_RATIO_LIST.contains(DataUtil.safeToString(product.getRevenueRatio()))) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.revenue.ratio.invalid"));
        }
        // check chiet khau khong duoc < don gia ban
        if (!DataUtil.isNullOrEmpty(product.getPriceExport())
                && !DataUtil.isNullOrEmpty(product.getDiscount())
                && product.getPriceExport() < product.getDiscount()) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "product.error.price.export.smaller.than.discount"));
        }
        // check ma hang hoa da ton tai
        return productRepository
                .findFirstByCode(product.getCode())
                .switchIfEmpty(Mono.just(new Product()))
                .flatMap(productDb -> {
                    if (isCreate && !DataUtil.isNullOrEmpty(productDb.getCode())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.exist"));
                    }
                    if (!isCreate
                            && !DataUtil.isNullOrEmpty(productDb.getCode())
                            && !DataUtil.safeEqual(product.getId(), productDb.getId())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.exist"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    public Mono<ProductSearchResult> searchProduct(SearchProductRequest request, String organizationId) {
        if (request.getPageIndex() == null) {
            request.setPageIndex(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }
        return Mono.zip(
                        productCustomRepository.countProduct(request, organizationId),
                        productCustomRepository
                                .searchProduct(request, organizationId)
                                .collectList())
                .flatMap(searchResult -> Mono.just(ProductSearchResult.builder()
                        .total(searchResult.getT1())
                        .pageIndex(request.getPageIndex())
                        .pageSize(request.getPageSize())
                        .dataList(searchResult.getT2())
                        .build()));
    }

    private CreateSyncHistoryRequest createSyncHistory(String action, String organizationId, Optional<String> opIdNo) {
        CreateSyncHistoryRequest request = new CreateSyncHistoryRequest();
        request.setOrgId(organizationId);
        request.setIdNo(opIdNo.isEmpty() ? null : opIdNo.get());
        request.setAction(action);
        request.setServiceType("ALL_BY_ORG");
        request.setSyncType("ALL");
        request.setObjectType(PRODUCT);
        return request;
    }

    @Override
    @Transactional
    public Mono<DataResponse<Product>> handleCreateProduct(
            Product product, List<OrganizationUnit> unitList, String organizationId) {
        return Mono.zip(SecurityUtils.getCurrentUser(), validateRequestCreateProduct(product, true))
                .flatMap(userValidate -> {
                    String username = userValidate.getT1().getUsername();
                    // lay don vi dau tien
                    String unitName =
                            !DataUtil.isNullOrEmpty(unitList) ? unitList.get(0).getName() : StringUtils.EMPTY;
                    // set data insert
                    product.setId(UUID.randomUUID().toString());
                    product.setNew(true);
                    product.setLockStatus(LOCK_STATUS_UNLOCK);
                    product.setStatus(STATUS_ACTIVE);
                    product.setCreateUnit(unitName);
                    product.setCreateAt(LocalDateTime.now());
                    product.setUpdateAt(LocalDateTime.now());
                    product.setCreateBy(username);
                    product.setUpdateBy(username);
                    product.setOrganizationId(organizationId);
                    return productRepository
                            .save(product)
                            .map(rsp -> new DataResponse<>(AuthConstants.SUCCESS, rsp))
                            .onErrorReturn(new DataResponse<>(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                    Translator.toLocaleVi(COMMON_ERROR),
                                    new Product()))
                            .doOnError(throwable -> log.error("Save product error: {}", throwable.getMessage()));
                });
    }

    @Override
    public Mono<Product> detailProduct(String productId) {
        return productRepository
                .findFirstById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist")))
                .map(product -> product);
    }

    @Override
    public Mono<ResponseEntity<Resource>> getImportProductTemplate() {
        try {
            Resource resource = new ClassPathResource(IMPORT_PRODUCT_TEMPLATE_PATH);
            InputStream inputStream = resource.getInputStream();
            byte[] fileData = inputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", IMPORT_PRODUCT_TEMPLATE_NAME);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return Mono.just(ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileData.length)
                    .body(resource));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<ResponseEntity<byte[]>> downloadImportResult(List<ProductImportDTO> items) {
        try {
            Resource resource = new ClassPathResource(IMPORT_PRODUCT_RESULT_TEMPLATE_PATH);
            String fileName = IMPORT_PRODUCT_RESULT_TEMPLATE_NAME;
            // Process the Excel data and modify it if needed
            try (Workbook workbook = WorkbookFactory.create(resource.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                Row row = sheet.getRow(2);
                row.getCell(9).setCellValue("Kết quả");
                row.getCell(10).setCellValue("Chi tiết lỗi");
                // ... Process the data and modify the sheet if needed
                for (var i = 0; i < items.size(); i++) {
                    var item = items.get(i);
                    buildRowWithProduct(sheet, i, item);
                }
                // Create a new Excel file
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                workbook.close();
                // Return the new Excel file as a byte array
                byte[] byteArray = outputStream.toByteArray();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                return Mono.just(new ResponseEntity<>(byteArray, headers, HttpStatus.OK));
            }
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private void buildRowWithProduct(Sheet sheet, int i, ProductImportDTO item) {
        var row = sheet.createRow(i + 3);
        row.createCell(0).setCellValue(i + 1);
        row.createCell(1).setCellValue(Optional.ofNullable(item.getCode()).orElse(StringUtils.EMPTY));
        row.createCell(2).setCellValue(Optional.ofNullable(item.getName()).orElse(StringUtils.EMPTY));
        row.createCell(3)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getPriceImportStr()))
                        .orElse(StringUtils.EMPTY));
        row.createCell(4)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getPriceExportStr()))
                        .orElse(StringUtils.EMPTY));
        row.createCell(5)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getUnit()))
                        .orElse(StringUtils.EMPTY));
        row.createCell(6)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getTaxRatio()))
                        .orElse(StringUtils.EMPTY));
        row.createCell(7)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getDiscountStr()))
                        .orElse(StringUtils.EMPTY));
        row.createCell(8)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getRevenueRatioStr()))
                        .orElse(StringUtils.EMPTY));
        // Ket qua
        row.createCell(9)
                .setCellValue(Optional.ofNullable(item.isResult())
                        .map(x -> x.equals(true) ? Translator.toLocaleVi(SUCCESS) : Translator.toLocaleVi(FAIL))
                        .orElse(StringUtils.EMPTY));
        row.createCell(10)
                .setCellValue(Optional.ofNullable(item.isResult())
                        .map(x -> x.equals(true) ? StringUtils.EMPTY : item.getErrMsg())
                        .orElse(StringUtils.EMPTY));
    }

    @Override
    public Mono<ProductImportListDTO> validateImportProduct(FilePart filePart) {
        // validate file trong
        if (filePart == null || filePart.filename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be provided.");
        }
        // validate dinh dang file
        String filename = filePart.filename();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.type.invalid"));
        }
        Flux<ProductImportDTO> organizationUnitRequests = getCreateOrganizationUnitRequestFlux(filePart);
        Flux<ProductImportDTO> results = organizationUnitRequests.flatMap(this::validateImport);
        return results.collectList().flatMap(list -> {
            var total = list.size();
            var totalSucceed = list.stream().filter(ProductImportDTO::isResult).count();
            return Mono.just(new ProductImportListDTO(list, totalSucceed, total - totalSucceed));
        });
    }

    /**
     * validate import item
     *
     * @param request
     *            request
     * @return
     */
    private Mono<ProductImportDTO> validateImport(ProductImportDTO request) {
        return validateImportProduct(request)
                .map(validateOrg -> {
                    request.setErrMsg(StringUtils.EMPTY);
                    request.setResult(true);
                    return request;
                })
                .onErrorResume(error -> {
                    request.setErrMsg(Translator.toLocaleVi(error.getMessage()));
                    request.setResult(false);
                    return Mono.just(request);
                });
    }

    private Mono<Boolean> validateImportProduct(ProductImportDTO product) {
        product.trim();
        // check empty
        if (DataUtil.isNullOrEmpty(product.getCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.code.empty"));
        }
        if (DataUtil.isNullOrEmpty(product.getName())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.name.empty"));
        }
        if (DataUtil.isNullOrEmpty(product.getTaxRatio())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.tax.ratio.empty"));
        }
        // check gia tri
        if (product.getCode().length() > 15 || !product.getCode().matches(CHAR_AND_NUM_REGEX)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.code.invalid"));
        }
        if (product.getName().length() > 500 || !product.getName().matches(VIE_CHAR_AND_NUM_REGEX)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.name.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getPriceImportStr())
                && (product.getPriceImportStr().length() > 10
                        || !product.getPriceImportStr().matches(PRICE_REGEX))) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.price.import.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getUnit())
                && (product.getUnit().length() > 150 || !product.getUnit().matches(VIE_CHAR_REGEX))) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.unit.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getPriceExportStr())
                && (product.getPriceExportStr().length() > 10
                        || !product.getPriceExportStr().matches(PRICE_REGEX))) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.price.export.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getDiscountStr())
                && (product.getDiscountStr().length() > 10
                        || !product.getDiscountStr().matches(PRICE_REGEX))) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.discount.invalid"));
        }
        if (!TAX_RATIO_LIST.contains(product.getTaxRatio())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.import.error.tax.ratio.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getRevenueRatioStr())
                && !REVENUE_RATIO_LIST.contains(product.getRevenueRatioStr())) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "product.import.error.revenue.ratio.invalid"));
        }
        // check ton tai
        return productRepository
                .findFirstByCode(product.getCode())
                .switchIfEmpty(Mono.just(new Product()))
                .flatMap(productDb -> {
                    if (!DataUtil.isNullOrEmpty(productDb.getCode())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product.import.error.code.exist"));
                    }
                    return Mono.just(true);
                });
    }

    private Flux<ProductImportDTO> getCreateOrganizationUnitRequestFlux(FilePart filePart) {
        return filePart.content()
                .filter(buffer -> buffer.readableByteCount() > 0)
                .flatMap(buffer -> {
                    try (Workbook workbook = WorkbookFactory.create(buffer.asInputStream())) {
                        Sheet sheet = workbook.getSheetAt(0);
                        Iterator<Row> rowIterator = sheet.iterator();
                        // Skip the first row (header row)
                        if (rowIterator.hasNext()) {
                            rowIterator.next();
                            rowIterator.next();
                            Row row = rowIterator.next();

                            int i = 0;
                            DataFormatter formatter = new DataFormatter();

                            String codeTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String nameTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String priceImportTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String priceExportTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String unitTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String taxRatioRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String discountTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String revenueRatioTemplateRow =
                                    DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));

                            if (!(Translator.toLocaleVi(ROW_TEMPLATE_NAME.CODE) + ROW_TEMPLATE_NAME.OBLIGATORY)
                                            .equals(DataUtil.safeTrim(codeTemplateRow))
                                    || !(Translator.toLocaleVi(ROW_TEMPLATE_NAME.NAME) + ROW_TEMPLATE_NAME.OBLIGATORY)
                                            .equals(DataUtil.safeTrim(nameTemplateRow))
                                    || !Translator.toLocaleVi(ROW_TEMPLATE_NAME.PRICE_IMPORT)
                                            .equals(DataUtil.safeTrim(priceImportTemplateRow))
                                    || !Translator.toLocaleVi(ROW_TEMPLATE_NAME.PRICE_EXPORT)
                                            .equals(DataUtil.safeTrim(priceExportTemplateRow))
                                    || !Translator.toLocaleVi(ROW_TEMPLATE_NAME.UNIT)
                                            .equals(DataUtil.safeTrim(unitTemplateRow))
                                    || !(Translator.toLocaleVi(ROW_TEMPLATE_NAME.TAX_RATIO)
                                                    + ROW_TEMPLATE_NAME.OBLIGATORY)
                                            .equals(DataUtil.safeTrim(taxRatioRow))
                                    || !Translator.toLocaleVi(ROW_TEMPLATE_NAME.DISCOUNT)
                                            .equals(DataUtil.safeTrim(discountTemplateRow))
                                    || !Translator.toLocaleVi(ROW_TEMPLATE_NAME.REVENUE_RATIO)
                                            .equals(DataUtil.safeTrim(revenueRatioTemplateRow))) {
                                return Mono.error(new BusinessException(
                                        String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                        Translator.toLocaleVi("product.import.error.template.invalid")));
                            }
                        }
                        List<ProductImportDTO> items = new ArrayList<>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            ProductImportDTO createProductRequest = readRowData(row);
                            items.add(createProductRequest);
                        }
                        // Validate size
                        if (items.size() > 200) {
                            return Flux.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("file.input.exceed", "200")));
                        }
                        return Flux.fromIterable(items);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(new BusinessException(
                                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                Translator.toLocaleVi("product.import.error.file.invalid")));
                    }
                });
    }

    private ProductImportDTO readRowData(Row row) {
        // unit
        int i = 0;
        DataFormatter formatter = new DataFormatter();
        ProductImportDTO productImportDTO = new ProductImportDTO();
        productImportDTO.setCode(this.getValueInCell(row, formatter, i++));
        productImportDTO.setName(this.getValueInCell(row, formatter, i++));
        productImportDTO.setPriceImport(
                DataUtil.safeToDouble(this.getValueInCell(row, formatter, i).replace(",", ""), null));
        productImportDTO.setPriceImportStr(DataUtil.safeToString(this.getValueInCell(row, formatter, i++), null));
        productImportDTO.setPriceExport(
                DataUtil.safeToDouble(this.getValueInCell(row, formatter, i).replace(",", ""), null));
        productImportDTO.setPriceExportStr(DataUtil.safeToString(this.getValueInCell(row, formatter, i++), null));
        productImportDTO.setUnit(this.getValueInCell(row, formatter, i++));
        productImportDTO.setTaxRatio(this.getValueInCell(row, formatter, i++));
        productImportDTO.setDiscount(
                DataUtil.safeToDouble(this.getValueInCell(row, formatter, i).replace(",", ""), null));
        productImportDTO.setDiscountStr(DataUtil.safeToString(this.getValueInCell(row, formatter, i++), null));
        productImportDTO.setRevenueRatio(DataUtil.safeToLong(this.getValueInCell(row, formatter, i), null));
        productImportDTO.setRevenueRatioStr(DataUtil.safeToString(this.getValueInCell(row, formatter, i), null));
        return productImportDTO;
    }

    private String getValueInCell(Row row, DataFormatter formatter, int i) {
        return Optional.ofNullable(row.getCell(i))
                .map(value -> formatter.formatCellValue(value).trim())
                .orElse(StringUtils.EMPTY);
    }

    @Override
    @Transactional
    public Mono<DataResponse<Product>> createProduct(Product product, String organizationId) {
        return SecurityUtils.getCurrentUser().flatMap(user -> authClient
                .findAllActiveOrganizationUnitsByOrganizationId(user.getId(), organizationId)
                .flatMap(unitList -> handleCreateProduct(product, unitList, organizationId)));
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> updateProduct(Product product) {
        return Mono.zip(validateRequestCreateProduct(product, false), SecurityUtils.getCurrentUser())
                .flatMap(validateUser -> productRepository
                        .findFirstById(product.getId())
                        .flatMap(productDb -> {
                            productDb.setCode(product.getCode());
                            productDb.setName(product.getName());
                            productDb.setPriceImport(product.getPriceImport());
                            productDb.setPriceExport(product.getPriceExport());
                            productDb.setUnit(product.getUnit());
                            productDb.setDiscount(product.getDiscount());
                            productDb.setTaxRatio(product.getTaxRatio());
                            productDb.setRevenueRatio(product.getRevenueRatio());
                            productDb.setUpdateAt(LocalDateTime.now());
                            productDb.setUpdateBy(validateUser.getT2().getUsername());
                            productDb.setNew(false);
                            productDb.setStatus(STATUS_ACTIVE);
                            return productRepository
                                    .save(productDb)
                                    .map(rsp -> DataResponse.success(true))
                                    .onErrorResume(throwable -> Mono.error(new BusinessException(
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                        }));
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> deleteProduct(String productId) {
        return Mono.zip(
                        productRepository
                                .findFirstByIdAndStatus(productId, STATUS_ACTIVE)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, "product.error.not.exist.or.inactive"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productUser -> {
                    Product product = productUser.getT1();
                    product.setNew(false);
                    product.setStatus(STATUS_ACTIVE);
                    product.setUpdateAt(LocalDateTime.now());
                    product.setUpdateBy(productUser.getT2().getUsername());
                    return productRepository
                            .save(product)
                            .map(rsp -> DataResponse.success(true))
                            .onErrorResume(error -> Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> lockProduct(LockProductRequest request) {
        return Mono.zip(
                        productRepository
                                .findFirstById(request.getProductId())
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productUser -> {
                    Product product = productUser.getT1();
                    product.setNew(false);
                    product.setLockStatus(
                            DataUtil.safeEqual(product.getLockStatus(), LOCK_STATUS_LOCK)
                                    ? LOCK_STATUS_UNLOCK
                                    : LOCK_STATUS_LOCK);
                    product.setUpdateAt(LocalDateTime.now());
                    product.setUpdateBy(productUser.getT2().getUsername());
                    return productRepository
                            .save(product)
                            .map(rsp -> DataResponse.success(true))
                            .onErrorResume(error -> Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    public Mono<ProductImportListDTO> importProduct(FilePart filePart, String organizationId) {
        // validate file trong
        if (filePart == null || filePart.filename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be provided.");
        }
        // validate dinh dang file
        String filename = filePart.filename();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be .xlsx or .xls format.");
        }
        Flux<ProductImportDTO> organizationUnitRequests = getCreateOrganizationUnitRequestFlux(filePart);
        Flux<ProductImportDTO> results = organizationUnitRequests.flatMap(this::validateImport);
        Flux<ProductImportDTO> productImportList =
                results.flatMap(productImport -> SecurityUtils.getCurrentUser().flatMap(user -> authClient
                        .findAllActiveOrganizationUnitsByOrganizationId(user.getId(), organizationId)
                        .flatMap(unitList -> {
                            if (productImport.isResult()) {
                                Product product = new Product();
                                ModelMapperFactory.getInstance().map(productImport, product);
                                return handleCreateProduct(product, unitList, organizationId)
                                        .map(result -> {
                                            productImport.setErrMsg(StringUtils.EMPTY);
                                            productImport.setResult(true);
                                            return productImport;
                                        })
                                        .onErrorResume(error -> {
                                            productImport.setErrMsg(Translator.toLocaleVi(error.getMessage()));
                                            productImport.setResult(false);
                                            return Mono.just(productImport);
                                        });
                            } else {
                                return Mono.just(productImport);
                            }
                        })));
        return productImportList.collectList().flatMap(list -> {
            var total = list.size();
            var totalSucceed = list.stream().filter(ProductImportDTO::isResult).count();
            return Mono.just(new ProductImportListDTO(list, totalSucceed, total - totalSucceed));
        });
    }

    @Override
    public Mono<Boolean> lockMultiProduct(LockMultiProductRequest request) {
        return Mono.zip(
                        productRepository
                                .findAllByIdIn(request.getProductIdList())
                                .collectList()
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productListUser -> {
                    List<Product> productDbList = productListUser.getT1();
                    productDbList.forEach(product -> {
                        product.setNew(false);
                        product.setLockStatus(
                                DataUtil.safeEqual(product.getLockStatus(), LOCK_STATUS_LOCK)
                                        ? LOCK_STATUS_UNLOCK
                                        : LOCK_STATUS_LOCK);
                        product.setUpdateAt(LocalDateTime.now());
                        product.setUpdateBy(productListUser.getT2().getUsername());
                    });
                    return productRepository
                            .saveAll(productDbList)
                            .collectList()
                            .map(rsp -> true)
                            .onErrorResume(error -> Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    public Mono<GetProductInfoResponse> getProductInfo(GetProductInfoRequest request) {
        // validate request
        String type = DataUtil.safeTrim(request.getType());
        if (DataUtil.isNullOrEmpty(type)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.type.not.empty"));
        }
        UUID organizationId = request.getOrganizationId();
        List<String> ids = request.getIds();
        if (DataUtil.isNullOrEmpty(ids) && DataUtil.isNullOrEmpty(organizationId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.ids.not.empty"));
        }
        if (!SYNC_TYPE.equals(type)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.type.invalid"));
        }
        Integer offset = request.getOffset();
        Integer limit = request.getLimit();
        if (offset != null && offset < 0) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("sync.history.params.pageIndex.invalid")));
        }
        if (limit != null && limit < 1) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("sync.history.params.pageSize.invalid")));
        }
        return productCustomRepository
                .getProductByIdAndOrganizationIdAndTransId(
                        ids, organizationId, offset, limit, request.getTransactionId())
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(
                        String.valueOf(HttpStatus.BAD_REQUEST.value()), Translator.toLocaleVi(NOT_FOUND, "Hàng hóa"))))
                .map(productList -> {
                    List<String> lstCustId =
                            productList.stream().map(Product::getId).collect(Collectors.toList());
                    GetProductInfoResponse getProductInfoResponse = new GetProductInfoResponse();
                    getProductInfoResponse.setRecords(productList);
                    getProductInfoResponse.setIds(lstCustId);
                    return getProductInfoResponse;
                });
    }

    // Ghi vao file excel
    private ByteArrayResource writeExcel(Workbook workbook) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook) {
            workbook.write(os);
            return new ByteArrayResource(os.toByteArray()) {
                @Override
                public String getFilename() {
                    return "BaoCao.xlsx";
                }
            };
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "export.error");
        }
    }

    // Convert dinh dang currency
    public static String formatCurrency(double price) {
        try {
            DecimalFormat formatter = new DecimalFormat("#,###.#######");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            dfs.setGroupingSeparator(',');
            formatter.setDecimalFormatSymbols(dfs);
            return formatter.format(price);
        } catch (Exception ex) {
            log.error("formatCurrency error: ", ex);
            return "";
        }
    }

    // set style va data vao file exel
    private Workbook writeReport(QueryReport queryReport) {
        try (InputStream templateInputStream =
                new ClassPathResource("template/template_export_report.xlsx").getInputStream()) {

            Workbook workbook = new XSSFWorkbook(templateInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // fill fromDate to column 5 row 1
            String fromDate = DataUtil.formatDate(queryReport.getFromDateParse(), Constants.DateTimePattern.DMY, "");
            Cell exportFromDateCell = sheet.getRow(1).getCell(5);
            exportFromDateCell.setCellValue(
                    exportFromDateCell.getStringCellValue().replace("${date}", fromDate));
            // fill toDate to column 6 row 1
            String toDate = DataUtil.formatDate(queryReport.getToDateParse(), Constants.DateTimePattern.DMY, "");
            Cell exportToDateCell = sheet.getRow(1).getCell(6);
            exportToDateCell.setCellValue(exportToDateCell.getStringCellValue().replace("${date}", toDate));
            int rowCount = 5; // bat dau fill du lieu tu dong thu 5
            int index = 1; // index bat dau tu 1
            // tao number format de dinh dang tien te
            Row row = sheet.createRow(rowCount);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.BLUE.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            writeRow(
                    row,
                    style,
                    0,
                    Arrays.asList(
                            String.valueOf(index),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getAccess())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getTotalProducts())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getNewProducts())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getTotalOrders())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getSuccessfulOrders())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getFailedOrders())),
                            formatCurrency(DataUtil.safeToDouble(queryReport.getTransactionValue()))));
            return workbook;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void writeRow(Row row, CellStyle cellStyle, int startIndex, List<String> rowData) {
        int cellIndex = startIndex;
        for (String data : rowData) {
            Cell cell = row.createCell(cellIndex++);
            cell.setCellValue(data);
            cell.setCellStyle(cellStyle);
        }
    }

    // xuat excel
    public Mono<Resource> exportReport(QueryReport request) {
        Workbook workbook = writeReport(request);
        return Mono.just(writeExcel(workbook));
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate == null
                ? LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(30))
                : fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate == null ? LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX)) : toDate.atTime(23, 59, 59);
    }
}
