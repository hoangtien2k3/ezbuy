/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingmodel.dto.request.GetServiceConfigRequest;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.request.PageTelecomRequest;
import com.ezbuy.settingmodel.request.StatusLockingRequest;
import com.ezbuy.settingmodel.request.TelecomSearchingRequest;
import com.ezbuy.settingmodel.response.*;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

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
