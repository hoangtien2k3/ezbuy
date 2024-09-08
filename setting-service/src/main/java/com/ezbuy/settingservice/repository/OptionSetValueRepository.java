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

import com.ezbuy.settingmodel.model.OptionSetValue;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OptionSetValueRepository extends R2dbcRepository<OptionSetValue, String> {
    @Query(
            value = "select ov.* " + " from option_set os inner join option_set_value ov on os.id = ov.option_set_id "
                    + " where os.code = :optionSetCode and ov.code = :optionValueCode "
                    + " and os.status = 1 and ov.status = 1 ")
    Mono<OptionSetValue> findByOptionSetCodeAndOptionValueCode(String optionSetCode, String optionValueCode);

    @Query(value = "select * from option_set_value where id = :id")
    Mono<OptionSetValue> getById(String id);

    @Query(
            value =
                    "update option_set_value set code = :code, value = :value, description = :description, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<OptionSetValue> updateOptionSetValue(
            String id, String code, String value, String description, Integer status, String user);

    @Query("select * from option_set_value where code = :code and option_set_id = :optionSetId")
    Mono<OptionSetValue> findByCodeAndOptionSetId(String code, Long optionSetId);

    @Query(value = "select * from option_set_value where option_set_id = :optionSetId")
    Mono<OptionSetValue> getByOptionSetId(Long optionSetId);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query(
            value =
                    "select * from option_set_value where option_set_id = (select id from option_set where code = :optionSetCode and status = 1) and status = 1;")
    Flux<OptionSetValue> findByOptionSetCode(String optionSetCode);

    @Query(
            value = "select * from option_set_value osv\n" + "join option_set os on osv.option_set_id = os.id\n"
                    + "where osv.status = 1 and os.status = 1 and os.code = :optionSetCode")
    Flux<OptionSetValue> findAllActiveOptionSetValueByOptionSetCode(String optionSetCode);
}
