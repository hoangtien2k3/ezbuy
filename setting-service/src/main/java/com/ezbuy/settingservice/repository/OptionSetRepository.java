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

import com.ezbuy.settingmodel.model.OptionSet;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OptionSetRepository extends R2dbcRepository<OptionSet, String> {
    @Query(value = "select * from option_set where id = :id")
    Mono<OptionSet> getById(String id);

    @Query(
            value =
                    "update option_set set code = :code, description = :description, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<OptionSet> updateOptionSet(String id, String code, String description, Integer status, String user);

    @Query("select * from option_set where code = :code")
    Mono<OptionSet> findByCode(String code);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
