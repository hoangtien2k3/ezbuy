package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.PartnerLicenseKey;
import com.ezbuy.settingmodel.model.OptionSetValue;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerLicenseKeyRepository extends R2dbcRepository<PartnerLicenseKey, UUID> {

    @Query(value = "select * from partner_license_key where user_id = :userId and organization_id = :organizationId and service_alias in (:lstServiceAlias) and status = 1")
    Flux<PartnerLicenseKey> getlstAliasKeyExsit(String userId, String organizationId, List<String> lstServiceAlias);


    @Query(value = "select option_set_value.* from sme_setting.option_set o inner join sme_setting.option_set_value v\n" +
            "    on o.id = v.option_set_id where o.code ='PARTNER_LICENSE_KEY_CODE'\n" +
            "                                and v.code in (:lstServiceAlias) and o.status = 1\n" +
            "                                and v.status = 1")
    Mono<List<OptionSetValue>> getLstAcronym(List<String> lstServiceAlias);

    @Query(value = "select * from partner_license_key where license_key = :licenseKey")
    Mono<PartnerLicenseKey> findByLicenseKey(String licenseKey);
    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

}
