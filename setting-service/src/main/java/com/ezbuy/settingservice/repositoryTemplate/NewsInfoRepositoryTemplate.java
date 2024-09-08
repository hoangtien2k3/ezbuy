/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.NewsDetailDTO;
import com.ezbuy.settingmodel.dto.NewsInfoDTO;
import com.ezbuy.settingmodel.dto.RelateNewsDTO;
import com.ezbuy.settingmodel.dto.request.SearchNewsInfoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsInfoRepositoryTemplate {
    Flux<NewsInfoDTO> findNewsInfo(SearchNewsInfoRequest request);

    Mono<Long> countNewsInfo(SearchNewsInfoRequest request);

    Flux<NewsDetailDTO> getNewsDetailByNewsInfoId(String id);

    Flux<RelateNewsDTO> getRelateNewsByGroupNewsId(String id);
}
