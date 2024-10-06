package com.ezbuy.paymentservice.service;

import com.viettel.sme.framework.model.response.DataResponse;
import com.viettel.sme.ordermodel.dto.OrderFieldConfigDTO;
import com.viettel.sme.ordermodel.dto.request.GetOrderFieldConfigRequest;
import reactor.core.publisher.Mono;

public interface OrderFieldConfigService {

    Mono<DataResponse<OrderFieldConfigDTO>> getConfigByServiceTypeAndOrderType(GetOrderFieldConfigRequest request);
}
