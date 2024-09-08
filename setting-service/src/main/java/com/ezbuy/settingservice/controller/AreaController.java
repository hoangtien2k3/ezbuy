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
package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingservice.service.AreaService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.Area.PREFIX)
public class AreaController {
    private final AreaService areaService;

    @GetMapping()
    public Mono<DataResponse<List<AreaDTO>>> getArea(String parentCode) {
        return this.areaService.getArea(parentCode);
    }

    @GetMapping("/get-area-name")
    public Mono<DataResponse<AreaDTO>> getAreaName(String province, String district, String precinct) {
        return this.areaService.getArea(province, district, precinct);
    }
}
