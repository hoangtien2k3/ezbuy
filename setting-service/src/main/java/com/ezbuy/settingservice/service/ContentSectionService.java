package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.TreeDataDTO;
import com.ezbuy.settingmodel.dto.request.GetContentSectionRequest;
import com.ezbuy.settingmodel.model.ContentSection;
import com.ezbuy.settingmodel.request.ContentSectionRequest;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import com.ezbuy.settingmodel.response.SearchContentSectionResponse;
import com.reactify.model.response.DataResponse;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

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
