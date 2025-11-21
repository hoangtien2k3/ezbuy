package com.ezbuy.paymentservice.repository;

import java.util.List;

import com.ezbuy.paymentservice.model.dto.OrderFieldConfig;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface OrderFieldConfigRepository extends R2dbcRepository<OrderFieldConfig, Long> {
    @Query("""
            SELECT ofc.*
            FROM order_field_config ofc
            JOIN order_type o ON ofc.order_type_id = o.id
            WHERE o.alias = :orderType
              AND o.status = 1
              AND ofc.service_alias IN (:lstServiceAlias)
              AND ofc.status = 1
            """)
    Flux<OrderFieldConfig> findByOrderTypeAndTelecomServiceIds(String orderType, List<String> lstServiceAlias);
}
