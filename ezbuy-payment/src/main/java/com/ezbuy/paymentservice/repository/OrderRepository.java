package com.ezbuy.paymentservice.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezbuy.paymentservice.model.OrderSyncDTO;
import com.ezbuy.paymentservice.model.dto.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {
    @Query("""
            SELECT o.id, o.order_code, o.create_at AS createAt, '1' AS type, o.id AS order_id
            FROM `order` o
            WHERE (:startTime IS NULL OR o.create_at >= :startTime)
              AND (:endTime IS NULL OR o.create_at < :endTime)
              AND o.state = :state
              AND o.status = 1
            
            UNION ALL
            
            SELECT oi.id, oi.order_code, oi.create_at AS createAt, '0' AS type, oi.order_id AS order_id
            FROM order_item oi
            WHERE (:startTime IS NULL OR oi.create_at >= :startTime)
              AND (:endTime IS NULL OR oi.create_at < :endTime)
              AND oi.state = :state
              AND oi.status = 1
            
            ORDER BY createAt
            LIMIT :limit OFFSET :offset
            """)
    Flux<OrderSyncDTO> findAllOrderByStateAndTime(
            int state, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);

    @Query("""
            SELECT COUNT(o.id)
            FROM `order` o
            WHERE o.customer_id = :customerId
              AND (:state IS NULL OR o.state = :state)
              AND o.status = 1
            """)
    Mono<Integer> countOrderHistory(String customerId, Integer state);

    @Query("""
            UPDATE `order`
            SET state = :state,
                update_at = NOW(),
                update_by = :updateBy
            WHERE id = :orderId AND status = 1
            """)
    Mono<Void> updateState(Integer state, String updateBy, String orderId);
}
