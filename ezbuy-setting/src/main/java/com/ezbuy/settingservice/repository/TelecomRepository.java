package com.ezbuy.settingservice.repository;

import com.ezbuy.settingservice.model.entity.Telecom;
import com.ezbuy.settingservice.model.dto.response.ClientTelecom;
import com.ezbuy.settingservice.model.dto.response.TelecomClient;
import java.util.List;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TelecomRepository extends R2dbcRepository<Telecom, String> {
    @Query("Select * from telecom_service ts " + "where ts.status = 1 " + "and (:ids is null or ts.id in (:ids))")
    Flux<Telecom> getAll(List<String> ids);

    @Query("SELECT ts.* FROM telecom_service ts WHERE ts.id = :id")
    Mono<Telecom> getById(String id);

    @Query("Select * from telecom_service ts where ts.is_filter <>1 ORDER BY ts.name\n")
    Flux<Telecom> getNonFilterTelecom();

    @Modifying
    @Query(
            value =
                    "update telecom_service set status=:status, update_by=:user, update_at=CURRENT_TIMESTAMP() where id=:id")
    Mono<Telecom> updateStatus(String id, Integer status, String user);

    @Modifying
    @Query(value = "update telecom_service set is_filter = 1 where origin_id=:originId ")
    Flux<Telecom> updateIsFilter(String originId);

    // bo sung update theo alias
    @Modifying
    @Query(value = "update telecom_service set is_filter = 1 where service_alias=:serviceAlias ")
    Flux<Telecom> updateIsFilterByAlias(String serviceAlias);

    // neu serviceAlias null thi van truy van nhu luong cu
    // bo sung ham truy van theo alias
    @Query(
            "SELECT ts.* FROM telecom_service ts WHERE ts.origin_id = :originId and (ts.service_alias =:serviceAlias or :serviceAlias is null)")
    Flux<Telecom> getAllByOriginId(String originId, String serviceAlias);

    // bo sung ham truy van theo alias
    @Query("SELECT ts.* FROM telecom_service ts WHERE ts.service_alias = :serviceAlias")
    Flux<Telecom> getAllByServiceAlias(String serviceAlias);

    @Query("select * from telecom_service where status = 1 ORDER BY name")
    Flux<Telecom> getAllTelecomServiceActive();

    @Query(
            "select admin_role,client_id,service_alias,service_alias as alias from telecom_service where status =1 and origin_id =:originId")
    Mono<TelecomClient> findClientInfoByOriginId(String originId);

    // Ham lay roleAdmin va clientId cua dich vu theo serviceAlias
    @Query(
            "select admin_role,client_id,service_alias from telecom_service where status =1 and service_alias =:serviceAlias")
    Mono<TelecomClient> findClientInfoByServiceAlias(String serviceAlias);

    @Query("select * from telecom_service")
    Flux<Telecom> getAllTelecomService();

    @Query("Select * from telecom_service ts " + "where ts.status = 1 " + "and ts.origin_id in (:lstOriginId)")
    Flux<Telecom> findTelecomByLstOriginId(List<String> lstOriginId);

    @Query("select service_alias from telecom_service where status =1 and client_code =:clientCode")
    Mono<ClientTelecom> getAliasByClientCode(String clientCode);

    @Query("select client_code from telecom_service where client_code =:clientCode")
    Mono<String> checkExistClientCode(String clientCode);
}
