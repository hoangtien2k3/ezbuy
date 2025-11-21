package com.ezbuy.paymentservice.service;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.paymentservice.model.dto.GetOrderFieldConfigRequest;
import com.ezbuy.paymentservice.model.dto.OrderFieldConfigDTO;
import reactor.core.publisher.Mono;

public interface OrderFieldConfigService {

    Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request);
}
