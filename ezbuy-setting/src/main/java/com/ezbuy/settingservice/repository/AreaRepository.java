package com.ezbuy.settingservice.repository;

import com.ezbuy.settingservice.model.dto.AreaDTO;
import com.ezbuy.settingservice.model.entity.Area;
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
