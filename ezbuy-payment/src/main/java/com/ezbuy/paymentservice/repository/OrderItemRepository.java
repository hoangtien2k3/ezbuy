package com.ezbuy.paymentservice.repository;

import java.util.List;

import com.ezbuy.paymentservice.model.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, String> {
    @Query("""
            SELECT ot.product_id,
                   ot.order_id,
                   ot.name,
                   ot.description,
                   ot.quantity,
                   ot.price,
                   ot.currency
            FROM order_item ot
            WHERE ot.order_id IN (:ids)
              AND ot.status = 1
            """)
    Flux<OrderItem> findAllByOrderIds(List<String> ids);

    @Query("""
            SELECT EXISTS(
                SELECT oi.id
                FROM `order` o
                JOIN order_item oi ON o.id = oi.order_id
                WHERE oi.id = :orderItemId
                  AND o.customer_id = :userId
                  AND o.status = 1
                  AND oi.status = 1
            )
            """)
    Mono<String> checkOrderItemExist(String orderItemId, String userId);

    @Query("""
            UPDATE order_item
            SET review_content = :content,
                update_at = NOW(),
                update_by = :updateBy
            WHERE id = :orderItemId
              AND status = 1
            """)
    Mono<Boolean> updateContent(String orderItemId, String content, String updateBy);

    @Query("""
            UPDATE order_item
            SET state = :state,
                update_at = NOW(),
                update_by = :updateBy
            WHERE order_id = :orderCode
              AND status = 1
            """)
    Mono<Boolean> updateOrderItemByOrderCode(String orderCode, String updateBy, Integer state);
}
