package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.Telecom;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TelecomRepository extends R2dbcRepository<Telecom, UUID> {
    @Query("select id, name, origin_id, deploy_order_code, service_alias from telecom_service where origin_id = :originId")
    Mono<Telecom> getByOriginId(String originId);

    @Query("SELECT * FROM sme_product.telecom_service " +
            "WHERE ((:telecomServiceId IS NULL OR origin_id IN (" +
            "SELECT dst_service_id FROM sme_product.service_relationship WHERE type = 'related' AND status = 1 AND src_service_id = :telecomServiceId)) " +
            "AND (:telecomServiceAlias IS NULL OR service_alias IN (" +
            "SELECT dst_service_alias FROM sme_product.service_relationship WHERE type = 'related' AND status = 1 AND src_service_alias = :telecomServiceAlias)))")
    Flux<Telecom> geRelatedServiceIdAndServiceAlias(String telecomServiceId, String telecomServiceAlias);

    @Query("select * from telecom_service where status = 1 order by name")
    Flux<Telecom> getAllActive();

    @Query("select id, name, origin_id, deploy_order_code, service_alias, bccs_alias from telecom_service where service_alias = :telecomServiceAlias")
    Mono<Telecom> getByAlias(String telecomServiceAlias);
    @Query("select origin_id from telecom_service where service_alias = :telecomServiceAlias")
    Mono<String> getTelecomServiceId(String telecomServiceAlias);
    @Query("Select count(1) totalProducts from sme_product.telecom_service where status = 1")
    Mono<Integer> countAllProduct();

    @Query("select count(1) " +
            "from telecom_service " +
            "where date(create_at) = :dateReport " +
            "  and status = :status")
    Mono<Integer> countTelecomByDateReport(LocalDate dateReport, Integer status);

    @Query("select id, name, origin_id, deploy_order_code, service_alias, bccs_alias from telecom_service where service_alias in (:lstTelecomServiceAlias) ")
    Flux<Telecom> getByLstAlias(List<String> lstTelecomServiceAlias);
}
