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
package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.dto.NewsContentDTO;
import com.ezbuy.settingmodel.model.NewsContent;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsContentRepository extends R2dbcRepository<NewsContent, String> {
    @Query(value = "select * from news_content where id = :id")
    Mono<NewsContent> getById(String id);

    @Query(
            value =
                    "update news_content set content = :content, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<NewsContent> updateNewsInfo(String id, String content, Integer status, String user);

    @Query("select * from news_content where news_info_id = :newsInfoId")
    Flux<NewsContentDTO> findByNewsInfoId(String newsInfoId);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
