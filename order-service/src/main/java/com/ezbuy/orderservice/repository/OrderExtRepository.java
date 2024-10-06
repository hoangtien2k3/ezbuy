package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.OrderExt;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderExtRepository extends R2dbcRepository<OrderExt, UUID> {

    @Query(value = "select * from order_ext where order_id in (select id from `order` where order_code = :orderCode and status = 1) and code = :orderExtCode and status = 1")
    Flux<OrderExt> findByOrderCodeAndCodeAndStatus(String orderCode, String orderExtCode);

}
