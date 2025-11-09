package com.ezbuy.orderservice.controller;

import com.ezbuy.ordermodel.constants.UrlPaths;
import com.ezbuy.ordermodel.dto.OrderFieldConfigDTO;
import com.ezbuy.ordermodel.dto.request.GetOrderFieldConfigRequest;
import com.ezbuy.orderservice.service.OrderFieldConfigService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlPaths.OrderFieldConfig.PRE_FIX)
public class OrderFieldController {

    private final OrderFieldConfigService orderFieldConfigService;

    /**
     * Order-007 Ham bo sung alias PYCXXX/LuongToanTrinhScontract
     *
     * @param request
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<OrderFieldConfigDTO>>> getConfigByServiceTypeAndOrderType(
            GetOrderFieldConfigRequest request) {
        return orderFieldConfigService
                .getConfigByServiceTypeAndOrderType(request)
                .map(ResponseEntity::ok);
    }
}
