package com.ezbuy.paymentservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, String> {

    @Query(value = "select ot.product_id, ot.order_id," +
            " ot.name, ot.description, ot.quantity, ot.price, ot.currency" +
            " from order_item ot " +
            " where ot.order_id in (:ids)" +
            " and ot.status = 1")
    Flux<OrderItem> findAllByOrderIds(List<String> ids);


    @Query(value = "select EXISTS(" +
            " select oi.id from `order` o" +
            " join order_item oi on o.id = oi.order_id" +
            " where oi.id = :orderItemId and o.customer_id = :userId " +
            " and o.status = 1 and oi.status = 1" +
            ")")
    Mono<String> checkOrderItemExist(String orderItemId, String userId);


    @Query(value = "update order_item set review_content = :content, update_at = NOW(), update_by = :updateBy" +
            " where id = :orderItemId and status = 1")
    Mono<Boolean> updateContent(String orderItemId, String content, String updateBy);

    @Query(value = "update order_item set state = :state, update_at = NOW(), update_by = :updateBy" +
            " where order_id = :orderCode and status = 1")
    Mono<Boolean> updateOrderItemByOrderCode(String orderCode, String updateBy, Integer state);

//    @Query(value = "select * from `order_item` oi " +
//            "where oi.order_id = :orderId")
//    Flux<OrderItem> findAllByOrderId(String orderId);
}
