package com.ezbuy.cartservice.repository;

import com.ezbuy.cartmodel.model.CartItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartItemRepository extends R2dbcRepository<CartItem, UUID> {

    @Query(
            value =
                    "update cart_item set status = 0, update_by=:user, update_at = CURRENT_TIMESTAMP() where id = :cartItemId")
    Mono<CartItem> deleteCartItem(String cartItemId, String user);

    @Query(
            value =
                    "update cart_item set status = 0, update_by=:user, update_at = CURRENT_TIMESTAMP()  where cart_id =:cartId")
    Mono<CartItem> deleteAllByCartId(String cartId, String user);

    @Query(value = "select * from cart_item where id =:cartItemId and status = 1")
    Mono<CartItem> findById(String cartItemId);

    @Query(value = "select * from cart_item where cart_id =:cartId and status = 1 limit 1")
    Mono<CartItem> findByCartId(String cartId);

    @Query(
            value =
                    "select * from cart_item ct where ct.cart_id =:cartId and ct.product_id in (:productList) and ct.status = 1")
    Flux<CartItem> findByCartIdAndProductId(String cartId, List<String> productList);

    @Query(
            value =
                    "update cart_item set quantity =quantity+1, update_by=:user, update_at = CURRENT_TIMESTAMP() where id in(:cartItemIdList)")
    Mono<CartItem> updateForAdd(List<String> cartItemIdList, String user);

    @Query(
            value =
                    "update cart_item set quantity=:quantity, update_by=:user, update_at=CURRENT_TIMESTAMP() where id=:cartItemId")
    Mono<CartItem> updateQuantity(String cartItemId, Long quantity, String user);

    @Query(value = "select * from cart_item where status=1 and cart_id=:cartId limit :pageSize")
    Flux<CartItem> getAllListCartItem(String cartId, Integer pageSize);

    @Query(value = "select count(*) from cart_item where status=1 and cart_id=:cartId")
    Mono<Integer> countQuantityItem(String cartId);

    @Query(
            value =
                    "UPDATE cart_item SET status = 0,update_by = 'system',update_at = NOW() where cart_id = :cartId and status = 1")
    Mono<Boolean> deleteByCartId(String cartId);

    @Query(
            value =
                    "SELECT count(b.id)  FROM (  SELECT telecom_service_id, max(create_at) create_at FROM cart_item GROUP BY telecom_service_id ORDER BY create_at) a LEFT JOIN cart_item b ON a.telecom_service_id = b.telecom_service_id LEFT JOIN cart ON b.cart_id = cart.id WHERE b.status = 1 AND cart.user_id = :userId ")
    Mono<Long> getTotalRecordsByUserId(String userId);
}
