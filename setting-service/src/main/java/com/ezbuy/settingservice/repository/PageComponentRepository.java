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

import com.ezbuy.settingmodel.model.PageComponent;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PageComponentRepository extends R2dbcRepository<PageComponent, String> {
    @Query("select * from page_component where page_id = :pageId and status = 1")
    Flux<PageComponent> findByPageId(String pageId);

    @Query(
            value =
                    "update page_component set status = 0, update_by=:user, update_at=CURRENT_TIMESTAMP()  where id in (:pageComponentIds) ")
    Flux<PageComponent> deleteOldComponent(List<String> pageComponentIds, String user);
}
