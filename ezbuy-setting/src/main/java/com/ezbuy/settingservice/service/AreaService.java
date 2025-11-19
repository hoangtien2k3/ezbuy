package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface AreaService {
    Mono<DataResponse<List<AreaDTO>>> getArea(String parentCode);

    Mono<DataResponse<AreaDTO>> getArea(String province, String dictrict, String precinct);
}
