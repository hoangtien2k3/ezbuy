package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.UploadImagesDTO;
import com.ezbuy.settingservice.model.dto.request.SearchImageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadImageRepositoryTemplate {
    Flux<UploadImagesDTO> queryList(SearchImageRequest request);

    Mono<Long> count(SearchImageRequest request);
}
