package com.ezbuy.settingservice.repository;

import com.ezbuy.settingservice.model.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingservice.model.entity.TelecomServiceConfig;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface TelecomServiceConfigRep extends R2dbcRepository<TelecomServiceConfig, String> {
    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where telecom_service_id in (:telecomServiceIds) \n" + "  and ts.status = 1\n"
                            + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfig(String search, List<String> telecomServiceIds);

    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where origin_id in (:originalIds) \n" + "  and ts.status = 1\n" + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfig2(String search, List<String> originalIds);

    @Query(
            value =
                    "select telecom_service_id, JSON_EXTRACT(config, :search) as json_config, ts.name, tsc.client_id, ts.origin_id as original_id, ts.service_alias as alias\n"
                            + "from telecom_service_config tsc \n"
                            + "         inner join telecom_service ts on tsc.telecom_service_id = ts.id\n"
                            + "where service_alias in (:lstServiceAlias) \n" + "  and ts.status = 1\n"
                            + "  and tsc.status = 1")
    Flux<TelecomServiceConfigDTO> getTelecomServiceConfigByAlias(String search, List<String> lstServiceAlias);
}
