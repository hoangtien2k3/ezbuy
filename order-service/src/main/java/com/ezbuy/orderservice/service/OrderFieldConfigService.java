package com.ezbuy.orderservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.dto.OrderFieldConfigDTO;
import com.ezbuy.sme.ordermodel.dto.request.GetOrderFieldConfigRequest;
import reactor.core.publisher.Mono;

public interface OrderFieldConfigService {

    Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request);
}
