package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.ServiceGroup;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceGroupRepository extends R2dbcRepository<ServiceGroup, String> {
    @Query("select * from service_group where id = :serviceGroupId")
    Mono<ServiceGroup> getById(String serviceGroupId);

    @Query(
            value =
                    "update service_group set code = :groupCode, name = :name, display_order = :displayOrder, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :serviceGroupId")
    Mono<ServiceGroup> updateServiceGroup(
            String serviceGroupId, String code, String name, Integer displayOrder, Integer status, String user);

    @Query("select * from service_group where code = :code")
    Mono<ServiceGroup> getByGroupCode(String code);

    @Query("select * from service_group where display_order = :displayOrder")
    Mono<ServiceGroup> getByGroupOrder(Integer groupOrder);

    @Query("select * from service_group where status = 1 ORDER BY name")
    Flux<ServiceGroup> getAllTelecomServiceActive();

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
