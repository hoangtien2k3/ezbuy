package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.OrderBccsData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderBccsDataRepository extends R2dbcRepository<OrderBccsData, UUID> {

    @Query(value = "select * from order_bccs_data where status = :status and order_id = :orderId")
    Flux<OrderBccsData> findByOrderIdAndStatus(String orderId, Integer status);

}
