package com.ezbuy.productservice.repository;

import com.ezbuy.sme.productmodel.model.ActiveTelecom;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ActiveTelecomRepository extends R2dbcRepository<ActiveTelecom, String> {

    // bo sung them dieu kien alias
    @Query(value = "select count(id) from active_telecom where telecom_service_id = :telecomServiceId and id_no = :idNo and (alias = :alias or alias is null)")
    Mono<Long> count(Long telecomServiceId, String idNo, String alias);

}
