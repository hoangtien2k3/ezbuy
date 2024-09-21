package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.request.SearchImageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadImageRepositoryTemplate {
    Flux<UploadImagesDTO> queryList(SearchImageRequest request);

    Mono<Long> count(SearchImageRequest request);
}
