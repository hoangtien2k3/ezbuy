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

import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.TreeDataDTO;
import com.ezbuy.settingmodel.dto.request.GetContentSectionRequest;
import com.ezbuy.settingmodel.model.ContentSection;
import com.ezbuy.settingmodel.request.ContentSectionRequest;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import com.ezbuy.settingmodel.response.SearchContentSectionResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface ContentSectionService {
    @Transactional
    Mono<SearchContentSectionResponse> search(SearchContentSectionRequest request);

    @Transactional
    Mono<ContentSectionDetailDTO> getContentSectionDetail(String id);

    Mono<List<ContentSection>> getAllCS();

    Mono<DataResponse<ContentSection>> getCS(String id);

    Mono<DataResponse<ContentSection>> createCS(ContentSectionRequest request);

    Mono<DataResponse<ContentSection>> updateCS(ContentSectionRequest request);

    Mono<List<ContentSection>> getAllActiveCS();

    Mono<DataResponse<List<ContentSection>>> getCSByServiceId(List<String> lstServiceId);

    Mono<DataResponse<List<ContentSection>>> getCSByServiceIdV2(List<String> lstAlias);

    Mono<DataResponse<List<ContentSection>>> getCSBySectionId(List<String> lstSectionId);

    Mono<List<TreeDataDTO>> getAllByTypeAndRefIdAndRefType(GetContentSectionRequest request);

    Mono<Boolean> delete(String id);
}
