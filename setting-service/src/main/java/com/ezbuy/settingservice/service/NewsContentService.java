package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.NewsContentDTO;
import com.ezbuy.settingmodel.model.NewsContent;
import com.ezbuy.settingmodel.request.CreateNewsContentRequest;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface NewsContentService {
    Mono<DataResponse<NewsContent>> createNewsContent(CreateNewsContentRequest request);

    Mono<DataResponse<NewsContent>> editNewsContent(String id, CreateNewsContentRequest request);

    Mono<List<NewsContentDTO>> findNewsInfoByNewsInfo(String newsInfoId);
}
