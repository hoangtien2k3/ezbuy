package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.MarketSectionDTO;
import com.ezbuy.settingmodel.model.MarketSection;
import com.ezbuy.settingmodel.request.CreateMarketSectionRequest;
import com.ezbuy.settingmodel.request.MarketSectionSearchRequest;
import com.ezbuy.settingmodel.response.MarketSectionSearchResponse;
import com.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketSectionService {
    Flux<MarketSection> getMarketSection(String pageCode, String serviceId);

    Flux<MarketSection> getMarketSectionV2(String pageCode, String serviceAlias);

    Mono<MarketSection> findById(String sectionId);

    Mono<MarketSectionSearchResponse> searchMarketSection(MarketSectionSearchRequest request);

    Mono<MarketSectionDTO> findMarketSectionById(String id);

    Mono<DataResponse<MarketSection>> createMarketSection(CreateMarketSectionRequest request);

    Mono<DataResponse<MarketSection>> editMarketSection(String id, CreateMarketSectionRequest request);

    Mono<DataResponse<MarketSection>> deleteMarketSection(String id);

    Mono<List<MarketSection>> getAllActiveMarketSections();

    Mono<List<MarketSection>> getAllMarketSections();

    Mono<List<MarketSection>> getAllActiveMarketSectionsRT();

    Mono<MarketSection> findByContentSectionId(String id);
}
