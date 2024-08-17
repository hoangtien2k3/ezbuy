package com.ezbuy.settingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.model.CustType;
import com.ezbuy.settingservice.service.CustTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.CustType.PREFIX)
public class CustTypeController {
    private final CustTypeService custTypeService;

    @GetMapping(UrlPaths.CustType.GET_ALL_ACTIVE)
    public Mono<DataResponse<List<CustType>>> getAllCustTypeActive() {
        return this.custTypeService.getAllCustTypeActive()
                .map(rs -> new DataResponse<>("success", rs));
    }
}
