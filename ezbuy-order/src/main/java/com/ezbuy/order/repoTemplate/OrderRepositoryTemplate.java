package com.ezbuy.order.repoTemplate;

import com.ezbuy.order.dto.OrderDetailDTO;
import reactor.core.publisher.Flux;

public interface OrderRepositoryTemplate {

    Flux<OrderDetailDTO> searchOrderDetail(String customerId, Integer state, int offset, int limit, String sort);

    Flux<OrderDetailDTO> findOneDetailById(String customerId, String orderId);
}
