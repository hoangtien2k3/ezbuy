package com.ezbuy.productservice.repository;

import com.ezbuy.sme.productmodel.model.ServiceRelationship;
import com.ezbuy.sme.productmodel.model.Subscriber;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ServiceRelationshipRepository extends R2dbcRepository<ServiceRelationship, String> {
    @Query(value = "select * from sme_product.service_relationship where type = 'related' and status = 1 and src_service_id = :telecomServiceId ")
    Flux<ServiceRelationship> getTelecomServiceRelated(String telecomServiceId);
}
