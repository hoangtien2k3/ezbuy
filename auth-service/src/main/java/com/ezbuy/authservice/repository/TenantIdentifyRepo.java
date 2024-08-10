package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.TenantIdentify;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TenantIdentifyRepo extends R2dbcRepository<TenantIdentify, String> {

    @Query(
            value = "select exists(\n" + "    select id\n"
                    + "    from sme_user.tenant_identify\n"
                    + "    where status =1 and trust_status =1 and id_type =:idType and id_no =:idNo and type =:type\n"
                    + ")")
    Mono<Boolean> existsTrustedByIdNoAndType(String idNo, String idType, String type);
}
