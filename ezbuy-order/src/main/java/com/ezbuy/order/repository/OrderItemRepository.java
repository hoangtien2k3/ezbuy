package com.ezbuy.order.repository;

import com.ezbuy.order.model.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, String> {

    @Query(value = "select EXISTS(select oi.id\n" +
            "              from order o\n" +
            "                       join order_item oi on o.id = oi.order_id\n" +
            "              where oi.id = :orderItemId\n" +
            "                and o.customer_id = :userId\n" +
            "                and o.status = 1\n" +
            "                and oi.status = 1)")
    Mono<String> checkOrderItemExist(String orderItemId, String userId);

    @Query(value = "update order_item\n" +
            "set review_content = :content,\n" +
            "    update_at      = NOW(),\n" +
            "    update_by      = :updateBy\n" +
            "where id = :orderItemId\n" +
            "  and status = 1\n")
    Mono<Boolean> updateContent(String orderItemId, String content, String updateBy);
}
