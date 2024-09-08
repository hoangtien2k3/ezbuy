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

import com.ezbuy.settingmodel.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingmodel.model.TelecomServiceConfig;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface TelecomServiceConfigRep extends R2dbcRepository<TelecomServiceConfig, String> {
    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where telecom_service_id in (:telecomServiceIds) \n" + "  and ts.status = 1\n"
                            + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfig(String search, List<String> telecomServiceIds);

    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where origin_id in (:originalIds) \n" + "  and ts.status = 1\n" + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfig2(String search, List<String> originalIds);

    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where service_alias in (:lstServiceAlias) \n" + "  and ts.status = 1\n"
                            + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfigByAlias(String search, List<String> lstServiceAlias);
}
