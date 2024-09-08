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
import com.ezbuy.settingmodel.model.CustType;
import com.ezbuy.settingservice.service.CustTypeService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.CustType.PREFIX)
public class CustTypeController {
    private final CustTypeService custTypeService;

    @GetMapping(UrlPaths.CustType.GET_ALL_ACTIVE)
    public Mono<DataResponse<List<CustType>>> getAllCustTypeActive() {
        return this.custTypeService.getAllCustTypeActive().map(rs -> new DataResponse<>("success", rs));
    }
}
