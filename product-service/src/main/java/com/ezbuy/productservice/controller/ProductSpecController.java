package com.ezbuy.productservice.controller;

import com.ezbuy.productservice.service.ProductSpecService;
import com.ezbuy.sme.framework.constants.MessageConstant;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.productmodel.dto.GetServiceConnectDTO;
import com.ezbuy.sme.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.sme.productmodel.dto.UpdateAccountServiceInfoDTO;
import com.ezbuy.sme.productmodel.model.Subscriber;
import com.ezbuy.sme.productmodel.request.FilterCreatingRequest;
import com.ezbuy.sme.productmodel.request.FilterGetListSubscriberActive;
import com.ezbuy.sme.productmodel.request.FilterGetListSubscriberActiveByAlias;
import com.ezbuy.sme.productmodel.request.GetListSubscriberActive;
import com.ezbuy.sme.settingmodel.model.Telecom;
import com.ezbuy.sme.settingmodel.request.MarketPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(DEFAULT_V1_PREFIX)
public class ProductSpecController {

    private final ProductSpecService productSpecService;

    public ProductSpecController(ProductSpecService productSpecService) {
        this.productSpecService = productSpecService;
    }

    /**
     * get filter for filter product template
     *
     * @return
     */
    @PostMapping(value = SYNC_FILTER_TEMPLATE)
    @PreAuthorize("hasAnyAuthority('admin','system')")
    public Mono<DataResponse> getFilterTemplate() {
        return productSpecService.syncFilterTemplate();
    }

    @GetMapping(value = GET_DETAIL)
    public Mono<DataResponse> getFilterDetail(@RequestParam(name = "telecomServiceId") String telecomServiceId) {
        return productSpecService.getFilterDetail(telecomServiceId, null);
    }

    @GetMapping(value = PREVIEW_FILTER)
    public Mono<DataResponse> previewFilterDetail(@RequestParam(name = "telecomServiceId") String telecomServiceId) {
        return productSpecService.getFilterDetail(telecomServiceId, true);
    }

    @PostMapping(value = FILTER_TEMPLATE)
    public Mono<DataResponse> createFilter(@Valid @RequestBody FilterCreatingRequest request) {
        return productSpecService.createFilter(request);
    }

    @PostMapping(value = GET_LIST_SUBSCRIBER_ACTIVE)
    public Mono<DataResponse> getListSubscriberActive(@Valid @RequestBody FilterGetListSubscriberActive request) {
        return productSpecService.getListSubscriberActive(request);
    }

    @PostMapping(value = GET_LIST_SUBSCRIBER_ACTIVE_BY_ALIAS)
    public Mono<DataResponse> getListSubscriberActiveByAlias(@Valid @RequestBody FilterGetListSubscriberActiveByAlias request) {
        return productSpecService.getListSubscriberActiveByAlias(request);
    }

    @PostMapping(value = GET_LIST_SUBSCRIBER_ACTIVE_BY_ALIAS_AND_LIST_IDNO)
    public Mono<DataResponse> getListSubscribersActive(@Valid @RequestBody GetListSubscriberActive request) {
        return productSpecService.getListSubscriberActiveByListIdNoAndListAlias(request);
    }
    @PostMapping(value = SYNC_SUBSCRIBER_ORDER)
    public Mono<DataResponse> syncSubscriberOrder(@RequestBody UpdateAccountServiceInfoDTO request) {
        return productSpecService.syncSubscriberOrder(request);
    }

    @PostMapping(value = GET_TELECOM_SERVICE_CONNECT)
    public Mono<DataResponse<List<Subscriber>>> getTelecomServiceConnect(@RequestBody GetServiceConnectDTO request) {
        return productSpecService.getTelecomServiceConnect(request);
    }

    @GetMapping(value = GET_TELECOM_SERVICE_RELATED)
    public Mono<DataResponse<List<Telecom>>> getTelecomServiceRelated(@RequestParam(name = "telecomServiceId", required = false) String telecomServiceId, @RequestParam(name = "telecomServiceAlias", required = false) String telecomServiceAlias) {
        return productSpecService.getTelecomServiceRelated(telecomServiceId, telecomServiceAlias);
    }

    @PostMapping(value = CREATE_FILTER)
    public Mono<DataResponse> createFilterDetail(@Valid @RequestBody ProductSpecCharAndValDTO request) {
        return productSpecService.createFilterDetail(request);
    }

    @PutMapping(value = EDIT_FILTER)
    public Mono<DataResponse> editFilter(@Valid @RequestBody ProductSpecCharAndValDTO request) {
        return productSpecService.editFilter(request);
    }
    @PostMapping(value = DELETE_FILTER)
    public Mono<DataResponse> deleteFilter(@RequestParam(name = "telecomServiceId") String telecomServiceId,
                                           @RequestParam(name = "filterId") String filterId) {
        return productSpecService.deleteFilter(telecomServiceId, filterId);
    }
}
