package com.ezbuy.orderservice.repoTemplate;

import com.ezbuy.ordermodel.dto.OrderDetailDTO;
import com.ezbuy.ordermodel.dto.OrderSyncDTO;
import com.ezbuy.ordermodel.model.response.CustomerSubscriberSmeInfoDTO;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface OrderRepositoryTemplate {

    Flux<OrderDetailDTO> searchOrderDetail(String customerId, Integer state, int offset, int limit, String sort);

    Flux<OrderDetailDTO> findOneDetailById(String customerId, String orderId);


    Flux<Object> updateOrderBatch(List<OrderSyncDTO> orderList);

    Flux<Object> updateOrderItemBatch(List<OrderSyncDTO> orderList);

    Flux<CustomerSubscriberSmeInfoDTO> findCustomerSubscriberSmeInfoById(String id);

    Flux<String> updateOrderItemsState(Map<String, String> orderIds);

    Flux<OrderDetailDTO> searchOrderDetailV2(String customerId, Integer state, int offset, int limit, String sort, List<String> orderCodeList);
}
