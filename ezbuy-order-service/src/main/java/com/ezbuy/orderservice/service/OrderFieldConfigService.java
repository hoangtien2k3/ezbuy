package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.OrderFieldConfigDTO;
import com.ezbuy.ordermodel.dto.request.GetOrderFieldConfigRequest;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderFieldConfigService {

    Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request);
}
