package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingmodel.dto.request.GetServiceConfigRequest;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.request.PageTelecomRequest;
import com.ezbuy.settingmodel.request.StatusLockingRequest;
import com.ezbuy.settingmodel.request.TelecomSearchingRequest;
import com.ezbuy.settingmodel.response.*;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TelecomService {
    Mono<DataResponse<List<TelecomDTO>>> getTelecomService(
            List<String> ids, List<String> serviceAliases, List<String> originIds);

    Mono<TelecomPagingResponse> searchTelecomService(TelecomSearchingRequest request);

    Mono<List<Telecom>> getNonFilterTelecom();

    Mono<DataResponse<Telecom>> updateStatus(StatusLockingRequest params);

    Mono<DataResponse> initFilter(String originId);

    Mono<DataResponse> initFilterV2(String serviceAlias);

    Mono<DataResponse<List<Telecom>>> getByOriginId(String originId, String serviceAlias);

    Mono<DataResponse<List<Telecom>>> getByServiceAlias(String serviceAlias);

    Mono<DataResponse<PageResponse>> getPageTelecomService(PageTelecomRequest request);

    Mono<DataResponse<List<String>>> getServiceTypes();

    Mono<List<TelecomResponse>> getAllTelecomServiceActive();

    Mono<TelecomClient> getAdminRoleOfService(String originId);

    Mono<TelecomClient> getAdminRoleOfServiceByServiceAlias(String serviceAlias);

    Mono<DataResponse<List<TelecomServiceConfigDTO>>> getTelecomServiceConfig(
            List<String> telecomServiceIds, List<String> originalIds, String syncType);

    Mono<DataResponse<List<TelecomServiceConfigDTO>>> getTelecomServiceConfigV2(GetServiceConfigRequest request);

    Mono<DataResponse<List<Telecom>>> getAllTelecomServiceIdAndCode();

    Mono<DataResponse<List<Telecom>>> getTelecomByLstOriginId(List<String> lstOriginId);

    /**
     * Ham lay Alias theo clientCode
     *
     * @param clientCode
     * @return
     */
    Mono<ClientTelecom> getAliasByClientCode(String clientCode);
}
