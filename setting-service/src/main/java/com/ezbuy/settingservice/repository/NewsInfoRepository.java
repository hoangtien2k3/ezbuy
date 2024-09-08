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

import com.ezbuy.settingmodel.model.NewsInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NewsInfoRepository extends R2dbcRepository<NewsInfo, UUID> {
    @Query(value = "select * from news_info where id = :id")
    Mono<NewsInfo> getById(String id);

    @Query(
            value =
                    "update news_info set code = :code, title = :title, display_order = :displayOrder, status = :status, group_news_id = :groupNewsId, state = :state, summary = :summary, update_by = :user, update_at = CURRENT_TIMESTAMP(), navigator_url = :navigatorUrl where id = :id")
    Mono<NewsInfo> updateNewsInfo(
            String id,
            String code,
            String title,
            Integer displayOrder,
            Integer status,
            String groupNewsId,
            String state,
            String summary,
            String user,
            String navigatorUrl);

    @Query("select * from news_info where code = :code")
    Mono<NewsInfo> findByCode(String code);

    @Query("select * from news_info where display_order = :displayOrder and group_news_id = :groupNewsId")
    Mono<NewsInfo> findByGroupOrder(Integer groupOrder, String groupNewsId);

    @Query("select * from news_info where status = 1 ORDER BY name")
    Flux<NewsInfo> getAllNewsInfoActive();

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
