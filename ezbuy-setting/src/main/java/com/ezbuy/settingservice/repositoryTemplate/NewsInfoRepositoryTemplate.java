package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.NewsDetailDTO;
import com.ezbuy.settingservice.model.dto.NewsInfoDTO;
import com.ezbuy.settingservice.model.dto.RelateNewsDTO;
import com.ezbuy.settingservice.model.dto.request.SearchNewsInfoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsInfoRepositoryTemplate {
    Flux<NewsInfoDTO> findNewsInfo(SearchNewsInfoRequest request);

    Mono<Long> countNewsInfo(SearchNewsInfoRequest request);

    Flux<NewsDetailDTO> getNewsDetailByNewsInfoId(String id);

    Flux<RelateNewsDTO> getRelateNewsByGroupNewsId(String id);
}
