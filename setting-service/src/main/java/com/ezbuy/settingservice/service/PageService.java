package com.ezbuy.settingservice.service;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.dto.PageDTO;
import com.ezbuy.settingmodel.model.Page;
import com.ezbuy.settingmodel.request.ChangePageStatusRequest;
import com.ezbuy.settingmodel.request.PageCreatingRequest;
import com.ezbuy.settingmodel.request.PagePolicyRequest;
import com.ezbuy.settingmodel.request.SearchingPageRequest;
import com.ezbuy.settingmodel.response.SearchingPageResponse;
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
