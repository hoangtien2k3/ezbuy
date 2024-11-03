package com.ezbuy.paymentservice.service;

import com.ezbuy.ordermodel.dto.OrderFieldConfigDTO;
import com.ezbuy.ordermodel.dto.request.GetOrderFieldConfigRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderFieldConfigService {

    Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request);
}
