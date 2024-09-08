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
package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import com.ezbuy.settingmodel.response.SearchingComponentResponse;
import com.ezbuy.settingservice.repositoryTemplate.ContentDisplayRepositoryTemplate;
import com.ezbuy.settingservice.service.ContentDisplayService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ContentDisplayServiceImpl extends BaseServiceHandler implements ContentDisplayService {
    private final ContentDisplayRepositoryTemplate contentDisplayRepositoryTemplate;

    @Override
    public Mono<SearchingComponentResponse> searchOriginComponents(ComponentPageRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);

        Flux<ContentDisplayDTO> components = contentDisplayRepositoryTemplate.getOriginComponent(request);
        Mono<Long> countMono = contentDisplayRepositoryTemplate.countComponents(request);
        return Mono.zip(components.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchingComponentResponse response = new SearchingComponentResponse();
            response.setContentDisplays(zip.getT1());
            response.setPagination(pagination);

            return response;
        });
    }

    @Override
    public Mono<ContentDisplayDTO> getDetails(String id) {
        return contentDisplayRepositoryTemplate.getContentWithParentId(id);
    }

    @Override
    public Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name) {
        return contentDisplayRepositoryTemplate.getOriginComponentDetails(name);
    }
}
