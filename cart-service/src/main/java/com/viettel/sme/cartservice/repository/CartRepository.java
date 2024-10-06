package com.viettel.sme.cartservice.repository;

import com.ezbuy.cartmodel.model.Cart;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository extends R2dbcRepository<Cart, UUID> {

    @Query(value = "select * from cart where user_id =:userId and status=1")
    Mono<Cart> findByUserId(String userId);

    @Query(value = "select * from cart where user_id =:userId and status=1")
    Flux<Cart> findByUserIdFlux(String userId);

    @Query(value = "select * from cart where id =:id and status=1")
    Mono<Cart> findById(String id);

    @Query(value = "SELECT id FROM cart WHERE user_id = :userId AND status =1")
    Mono<String> getIdByUserID(String userId);
}
