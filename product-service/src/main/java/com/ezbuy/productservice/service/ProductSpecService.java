package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.GetServiceConnectDTO;
import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.productmodel.dto.UpdateAccountServiceInfoDTO;
import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productmodel.request.FilterCreatingRequest;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActive;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActiveByAlias;
import com.ezbuy.productmodel.request.GetListSubscriberActive;
import java.util.List;

import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface ProductSpecService {

    Mono<DataResponse> getFilterTemplate(String telecomServiceAlias);

    Mono<DataResponse> syncFilterTemplate();

    Mono<DataResponse> getFilterDetail(String telecomServiceId, Boolean isPreview);

    Mono<DataResponse> createFilter(FilterCreatingRequest request);

    Mono<DataResponse> getListSubscriberActive(FilterGetListSubscriberActive request);

    Mono<DataResponse> getListSubscriberActiveByAlias(FilterGetListSubscriberActiveByAlias request);

    /**
     * Ham lay danh sach thue bao hoat dong theo list IdNo va List Alias
     *
     * @param request
     * @return
     */
    Mono<DataResponse> getListSubscriberActiveByListIdNoAndListAlias(GetListSubscriberActive request);

    /**
     * ham dong bo thong tin dich vu cua account
     *
     * @param request
     * @return
     */
    Mono<DataResponse> syncSubscriberOrder(UpdateAccountServiceInfoDTO request);

    /**
     * ham kiem tra dich vu da thuc hien dau noi chua
     *
     * @param request
     * @return
     */
    Mono<DataResponse<List<Subscriber>>> getTelecomServiceConnect(GetServiceConnectDTO request);

    /**
     * ham lay danh sach dich vu lien quan
     *
     * @param telecomServiceId,
     *            telecomServiceAlias
     * @return
     */
    Mono<DataResponse<List<Telecom>>> getTelecomServiceRelated(String telecomServiceId, String telecomServiceAlias);

    Mono<DataResponse> createFilterDetail(ProductSpecCharAndValDTO request);

    Mono<DataResponse> editFilter(ProductSpecCharAndValDTO request);

    Mono<DataResponse> deleteFilter(String telecomServiceId, String filterId);
}
