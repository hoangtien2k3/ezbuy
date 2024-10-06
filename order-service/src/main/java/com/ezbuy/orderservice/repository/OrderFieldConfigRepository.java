package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.OrderFieldConfig;
import com.ezbuy.orderservice.repository.query.OrderFieldConfigQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OrderFieldConfigRepository extends R2dbcRepository<OrderFieldConfig, Long> {

    // chuyen original thanh alias
    @Query(value = OrderFieldConfigQuery.queryFindConfigByOrderTypeAndTelecomServiceIds)
    Flux<OrderFieldConfig> findByOrderTypeAndTelecomServiceIds(String orderType, List<String> lstAlias);
}
