package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.model.Area;
import com.ezbuy.settingservice.repository.AreaRepository;
import com.ezbuy.settingservice.service.AreaService;
import io.hoangtien2k3.commons.aop.cache.Cache2L;
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepositoory;

    @Override
    @Cache2L(durationInMinute = 720)
    public Mono<DataResponse<List<AreaDTO>>> getArea(String parentCode) {
        return getAreaData(parentCode).flatMap(areas -> {
            if (DataUtil.isNullOrEmpty(areas)) {
                return Mono.just(new DataResponse<>("success", new ArrayList<>()));
            }
            List<AreaDTO> areaDTOS = areas.stream()
                    .map(area -> AreaDTO.builder()
                            .areaCode(area.getAreaCode())
                            .status(area.getStatus())
                            .precinct(area.getPrecinct())
                            .dictrict(area.getDistrict())
                            .province(area.getProvince())
                            .name(area.getName())
                            .build())
                    .collect(Collectors.toList());
            return Mono.just(new DataResponse<>("success", areaDTOS));
        });
    }

    private Mono<List<Area>> getAreaData(String parentCode) {
        if (DataUtil.isNullOrEmpty(parentCode)) {
            return areaRepositoory.getProvince().collectList();
        } else {
            return areaRepositoory.getArea(DataUtil.safeTrim(parentCode)).collectList();
        }
    }

    @Override
    @Cache2L(durationInMinute = 720)
    public Mono<DataResponse<AreaDTO>> getArea(String province, String district, String precinct) {
        return areaRepositoory
                .getAreaName(province, district, precinct)
                .flatMap(areas -> Mono.just(new DataResponse<>("success", areas)));
    }
}
