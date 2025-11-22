package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.ContentSectionDetailDTO;
import com.ezbuy.settingservice.model.dto.TreeDataDTO;
import com.ezbuy.settingservice.model.dto.request.GetContentSectionRequest;
import com.ezbuy.settingservice.model.entity.ContentSection;
import com.ezbuy.settingservice.model.dto.request.ContentSectionRequest;
import com.ezbuy.settingservice.model.dto.request.SearchContentSectionRequest;
import com.ezbuy.settingservice.model.dto.response.SearchContentSectionResponse;
import com.ezbuy.core.model.response.DataResponse;
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
