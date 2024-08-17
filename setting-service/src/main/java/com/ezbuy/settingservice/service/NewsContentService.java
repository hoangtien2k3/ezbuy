package com.ezbuy.settingservice.service;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.dto.NewsContentDTO;
import com.ezbuy.settingmodel.model.NewsContent;
import com.ezbuy.settingmodel.request.CreateNewsContentRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NewsContentService {
    Mono<DataResponse<NewsContent>> createNewsContent(CreateNewsContentRequest request);
    Mono<DataResponse<NewsContent>> editNewsContent(String id, CreateNewsContentRequest request);
    Mono<List<NewsContentDTO>> findNewsInfoByNewsInfo(String newsInfoId);
}
