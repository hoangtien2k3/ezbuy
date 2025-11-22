package com.ezbuy.order.repository;

import java.util.UUID;

import com.ezbuy.order.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {

    @Query(value = "select count(*)\n" +
            "from (select order.id, order.state\n" +
            "      from order\n" +
            "      where order_code is not null\n" +
            "        and customer_id = :customerId\n" +
            "        and status = 1\n" +
            "      union\n" +
            "      select order_item.id, order_item.state\n" +
            "      from order_item\n" +
            "               left join order o on o.id = order_item.order_id\n" +
            "      where order_item.order_code is not null\n" +
            "        and o.status = 1\n" +
            "        and order_item.status = 1\n" +
            "        and customer_id = :customerId) orderAll\n" +
            "where (:state is null or orderAll.state = :state)\n")
    Mono<Integer> countOrderHistory(String customerId, Integer state);
}
