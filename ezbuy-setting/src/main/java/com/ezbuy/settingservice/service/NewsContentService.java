package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.NewsContentDTO;
import com.ezbuy.settingservice.model.entity.NewsContent;
import com.ezbuy.settingservice.model.dto.request.CreateNewsContentRequest;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface NewsContentService {
    Mono<DataResponse<NewsContent>> createNewsContent(CreateNewsContentRequest request);

    Mono<DataResponse<NewsContent>> editNewsContent(String id, CreateNewsContentRequest request);

    Mono<List<NewsContentDTO>> findNewsInfoByNewsInfo(String newsInfoId);
}
