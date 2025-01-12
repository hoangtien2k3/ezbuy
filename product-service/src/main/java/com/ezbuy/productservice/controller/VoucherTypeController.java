package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productmodel.dto.request.SearchVoucherTypeRequest;
import com.ezbuy.productmodel.dto.request.VoucherTypeRequest;
import com.ezbuy.productmodel.dto.response.SearchVoucherTypeResponse;
import com.ezbuy.productmodel.model.VoucherType;
import com.ezbuy.productservice.service.VoucherTypeService;
import com.reactify.constants.MessageConstant;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlPaths.VoucherType.PREFIX)
public class VoucherTypeController {
    private final VoucherTypeService voucherTypeService ;

    /**
     * Ham lay danh sach voucher type hieu luc
     *
     * @return
     */
    @GetMapping(value = UrlPaths.VoucherType.ALL_ACTIVE)
    public Mono<DataResponse> getAllVoucherTypeActive() {
        return voucherTypeService.getAllVoucherTypeActive().map(rs -> rs);
    }
    @GetMapping(value = UrlPaths.VoucherType.LIST)
    public Mono<DataResponse> getAll() {
        return voucherTypeService.getAll().map(result -> new DataResponse(Translator.toLocaleVi(MessageConstant.SUCCESS), result));
    }

    @PostMapping(UrlPaths.VoucherType.CREATE)
    public Mono<DataResponse<VoucherType>> create(@RequestBody VoucherTypeRequest request) {
        return voucherTypeService.create(request);
    }

    @PutMapping(value = UrlPaths.VoucherType.UPDATE)
    public Mono<DataResponse<VoucherType>> update(@PathVariable String id, @Valid @RequestBody VoucherTypeRequest request) {
        return voucherTypeService.update(id, request);
    }

    @GetMapping(value=UrlPaths.VoucherType.SEARCH)
    public Mono<SearchVoucherTypeResponse> search(SearchVoucherTypeRequest request) {
        return voucherTypeService.search(request);
    }

    @PutMapping(value = UrlPaths.VoucherType.DELETE)
    public Mono<DataResponse<VoucherType>> deleteSetting(@PathVariable String id) {
        return voucherTypeService.delete(id);
    }

    /**
     * Ham lay voucher type theo voucher code
     *
     * @return
     */
    @GetMapping(value = UrlPaths.VoucherType.FIND_BY_VOUCHER_CODE)
    public Mono<DataResponse> findVoucherTypeByVoucherCode(@RequestParam String code) {
        return voucherTypeService.findVoucherTypeByVoucherCode(code).map(rs -> rs);
    }
}
