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

import com.ezbuy.settingmodel.dto.PageDTO;
import com.ezbuy.settingmodel.model.Page;
import com.ezbuy.settingmodel.request.ChangePageStatusRequest;
import com.ezbuy.settingmodel.request.PageCreatingRequest;
import com.ezbuy.settingmodel.request.PagePolicyRequest;
import com.ezbuy.settingmodel.request.SearchingPageRequest;
import com.ezbuy.settingmodel.response.SearchingPageResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface PageService {
    Mono<DataResponse<PageDTO>> getPage(String code);

    Mono<SearchingPageResponse> searchPages(SearchingPageRequest request);

    Mono<PageDTO> getDetailPage(String pageId);

    Mono<DataResponse<Page>> createPage(PageCreatingRequest request);

    Mono<DataResponse<Page>> editPage(PageCreatingRequest request);

    Mono<DataResponse<String>> policyPage(PagePolicyRequest request);

    Mono<DataResponse<PageDTO>> changeStatus(ChangePageStatusRequest request);
}
