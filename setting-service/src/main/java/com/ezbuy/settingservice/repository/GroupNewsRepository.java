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

import com.ezbuy.settingmodel.model.GroupNews;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GroupNewsRepository extends R2dbcRepository<GroupNews, UUID> {
    @Query(value = "select * from group_news where id = :id")
    Mono<GroupNews> getById(String id);

    @Query(
            value =
                    "update group_news set code = :code, name = :name, display_order = :displayOrder, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<GroupNews> updateGroupNews(
            String id, String code, String name, Integer displayOrder, Integer status, String user);

    @Query("select * from group_news where code = :code")
    Mono<GroupNews> findByCode(String code);

    @Query("select * from group_news where display_order = :displayOrder")
    Mono<GroupNews> findByDisplayOrder(Integer groupOrder);

    @Query("select * from group_news ORDER BY name")
    Flux<GroupNews> getAllGroupNews();

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
