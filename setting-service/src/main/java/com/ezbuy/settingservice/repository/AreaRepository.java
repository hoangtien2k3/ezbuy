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

import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.model.Area;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AreaRepository extends R2dbcRepository<Area, String> {
    @Query(value = "Select * from area where area.parent_code = :parentCode and area.status = 1")
    Flux<Area> getArea(String parentCode);

    @Query(value = "Select * from area where area.parent_code is null and area.status = 1")
    Flux<Area> getProvince();

    @Query(
            value = "select " + "(select name  from area where area_code = :precinct) as precinct_name, "
                    + "(select name  from area where area_code = :dictrict) as district_name, "
                    + "(select name  from area where area_code = :province) as province_name")
    Mono<AreaDTO> getAreaName(String province, String dictrict, String precinct);
}
