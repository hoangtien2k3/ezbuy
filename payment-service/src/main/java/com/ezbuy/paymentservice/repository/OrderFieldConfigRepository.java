package com.ezbuy.paymentservice.repository;

import static com.ezbuy.paymentservice.repository.query.OrderFieldConfigQuery.queryFindConfigByOrderTypeAndTelecomServiceIds;

import com.ezbuy.ordermodel.model.OrderFieldConfig;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface OrderFieldConfigRepository extends R2dbcRepository<OrderFieldConfig, Long> {

    // chuyen tu truy van theo telecomsersiviceId thanh alias
    @Query(value = queryFindConfigByOrderTypeAndTelecomServiceIds)
    Flux<OrderFieldConfig> findByOrderTypeAndTelecomServiceIds(String orderType, List<String> lstServiceAlias);
}
