package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.NewsDetailDTO;
import com.ezbuy.settingmodel.dto.RelateNewsDTO;
import com.ezbuy.settingmodel.dto.request.SearchNewsInfoRequest;
import com.ezbuy.settingmodel.model.NewsInfo;
import com.ezbuy.settingmodel.request.CreateNewsInfoRequest;
import com.ezbuy.settingmodel.response.SearchNewsInfoResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface NewsInfoService {
    Mono<DataResponse<NewsInfo>> createNewsInfo(CreateNewsInfoRequest request);

    Mono<DataResponse<NewsInfo>> editNewsInfo(String id, CreateNewsInfoRequest request);

    Mono<SearchNewsInfoResponse> findNewsInfo(SearchNewsInfoRequest request);

    Mono<List<NewsInfo>> getAllNewsInfoActive();

    Mono<DataResponse<NewsDetailDTO>> getNewsDetailByNewsInfoId(String id);

    Mono<DataResponse<List<RelateNewsDTO>>> getRelateNewsByGroupNewsId(String id);
}
