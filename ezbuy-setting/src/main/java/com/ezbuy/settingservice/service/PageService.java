package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.PageDTO;
import com.ezbuy.settingservice.model.entity.Page;
import com.ezbuy.settingservice.model.dto.request.ChangePageStatusRequest;
import com.ezbuy.settingservice.model.dto.request.PageCreatingRequest;
import com.ezbuy.settingservice.model.dto.request.PagePolicyRequest;
import com.ezbuy.settingservice.model.dto.request.SearchingPageRequest;
import com.ezbuy.settingservice.model.dto.response.SearchingPageResponse;
import com.ezbuy.core.model.response.DataResponse;
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
