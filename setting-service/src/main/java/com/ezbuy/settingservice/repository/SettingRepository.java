package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.Setting;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SettingRepository extends R2dbcRepository<Setting, String> {
    @Query(value = "select s.value from setting s where s.code = :code and s.status = 1")
    Mono<String> findByCode(String code);

    @Query(value = "select * from sme_setting.setting order by create_at desc")
    Flux<Setting> findAllSetting();

    @Query(value = "select * from sme_setting.setting where id = :id")
    Mono<Setting> getById(String id);

    @Query(value = "select * from sme_setting.setting where status = 1")
    Flux<Setting> getAllActiveSetting();

    @Query(value = "select * from sme_setting.setting where code = :code")
    Mono<Setting> getByCode(String code);

    @Query(
            value = "update sme_setting.setting set code = :code, value = :value, description = :description,"
                    + "status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<Setting> updateSetting(String id, String code, String value, String description, Integer status, String user);

    @Query(
            value =
                    "update sme_setting.setting set status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<Setting> updateStatus(String id, Integer status, String user);
}
