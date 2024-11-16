package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.AreaDTO;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AreaService {
    Mono<DataResponse<List<AreaDTO>>> getArea(String parentCode);

    Mono<DataResponse<AreaDTO>> getArea(String province, String dictrict, String precinct);
}
