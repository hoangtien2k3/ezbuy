package com.ezbuy.cartservice.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import com.ezbuy.cartservice.domain.model.CartItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartItemRepository extends R2dbcRepository<CartItem, UUID> {
    @Query("""
            UPDATE cart_item
            SET status = 0,
                update_by = :user,
                update_at = CURRENT_TIMESTAMP()
            WHERE id = :cartItemId
            """)
    Mono<CartItem> deleteCartItem(String cartItemId, String user);

    @Query("""
            UPDATE cart_item
            SET status = 0,
                update_by = :user,
                update_at = CURRENT_TIMESTAMP()
            WHERE cart_id = :cartId
            """)
    Mono<CartItem> deleteAllByCartId(String cartId, String user);

    @Query("""
            SELECT *
            FROM cart_item
            WHERE id = :cartItemId
              AND status = 1
            """)
    Mono<CartItem> findById(String cartItemId);

    @Query("""
            SELECT *
            FROM cart_item
            WHERE cart_id = :cartId
              AND status = 1
            LIMIT 1
            """)
    Mono<CartItem> findByCartId(String cartId);

    @Query("""
            SELECT *
            FROM cart_item ct
            WHERE ct.cart_id = :cartId
              AND ct.product_id IN (:productList)
              AND ct.status = 1
            """)
    Flux<CartItem> findByCartIdAndProductId(String cartId, List<String> productList);

    @Query("""
            UPDATE cart_item
            SET quantity = quantity + 1,
                update_by = :user,
                update_at = CURRENT_TIMESTAMP()
            WHERE id IN (:cartItemIdList)
            """)
    Mono<CartItem> updateForAdd(List<String> cartItemIdList, String user);

    @Query("""
            UPDATE cart_item
            SET quantity = :quantity,
                update_by = :user,
                update_at = CURRENT_TIMESTAMP()
            WHERE id = :cartItemId
            """)
    Mono<CartItem> updateQuantity(String cartItemId, Long quantity, String user);

    @Query("""
            SELECT *
            FROM cart_item
            WHERE status = 1
              AND cart_id = :cartId
            LIMIT :pageSize
            """)
    Flux<CartItem> getAllListCartItem(String cartId, Integer pageSize);

    @Query("""
            SELECT COUNT(*)
            FROM cart_item
            WHERE status = 1
              AND cart_id = :cartId
            """)
    Mono<Integer> countQuantityItem(String cartId);

    @Query("""
            UPDATE cart_item
            SET status = 0,
                update_by = 'system',
                update_at = NOW()
            WHERE cart_id = :cartId
              AND status = 1
            """)
    Mono<Boolean> deleteByCartId(String cartId);

    @Query("""
            SELECT COUNT(b.id)
            FROM (
                SELECT telecom_service_id,
                       MAX(create_at) AS create_at
                FROM cart_item
                GROUP BY telecom_service_id
                ORDER BY create_at
            ) a
            LEFT JOIN cart_item b ON a.telecom_service_id = b.telecom_service_id
            LEFT JOIN cart ON b.cart_id = cart.id
            WHERE b.status = 1
              AND cart.user_id = :userId
            """)
    Mono<Long> getTotalRecordsByUserId(String userId);
}
