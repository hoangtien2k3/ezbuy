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
package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.ActionLog;
import java.time.LocalDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ActionLogRepository extends R2dbcRepository<ActionLog, String> {

    /**
     * Counts the number of login actions in a single day.
     *
     * @param dateReport
     *            the date for which to count login actions
     * @param type
     *            the type of action to count
     * @return a Mono emitting the count of login actions for the specified date and
     *         type
     */
    @Query("select count(*) " + "from action_log " + "where date(create_at) =: dateReport " + "  and type = :type")
    Mono<Integer> countLoginInOneDay(LocalDate dateReport, String type);
}
