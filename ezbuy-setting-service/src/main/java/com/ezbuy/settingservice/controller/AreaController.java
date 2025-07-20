package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingservice.service.AreaService;
import com.reactify.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.Area.PREFIX)
public class AreaController {
    private final AreaService areaService;

    @GetMapping()
    public Mono<DataResponse<List<AreaDTO>>> getArea(@RequestParam("parent_code") String parentCode) {
        return this.areaService.getArea(parentCode);
    }

    @GetMapping("/get-area-name")
    public Mono<DataResponse<AreaDTO>> getAreaName(
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("precinct") String precinct) {
        return this.areaService.getArea(province, district, precinct);
    }
}
