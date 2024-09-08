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

import com.ezbuy.settingmodel.model.MarketPage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketPageRepository extends R2dbcRepository<MarketPage, UUID> {
    @Query(value = "select * from market_page where id = :id")
    Mono<MarketPage> getById(String id);

    @Query("select * from market_page order by create_at desc")
    Flux<MarketPage> findAllMarketPage();

    @Query("select * from market_page where service_id = :telecomServiceId")
    Flux<MarketPage> findByServiceId(String telecomServiceId);

    @Query("select * from market_page where service_alias = :serviceAlias")
    Flux<MarketPage> findByServiceAlias(String serviceAlias);

    @Query(
            value =
                    "update market_page set service_id = :serviceId, code = :code, name = :name, description = :description,"
                            + " status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP(), service_alias =:serviceAlias where id = :id")
    Mono<MarketPage> updateMarketPage(
            String serviceId,
            String code,
            String name,
            String description,
            Integer status,
            String user,
            String id,
            String serviceAlias);

    @Query(value = "select * from market_page where name = :name")
    Mono<MarketPage> findByName(String name);

    @Query("select * from market_page where status = 1")
    Flux<MarketPage> getAllActiveMarketPage();

    @Query(value = "select * from market_info where service_id in (:lstServiceId) and status = 1 ")
    Flux<MarketPage> getByServiceId(List<String> lstServiceId);

    // lay danh sach cau hinh trang theo danh sach alias
    @Query(value = "select * from market_info where service_alias in (:lstAlias) and status = 1 ")
    Flux<MarketPage> getByServiceAlias(List<String> lstAlias);
}
