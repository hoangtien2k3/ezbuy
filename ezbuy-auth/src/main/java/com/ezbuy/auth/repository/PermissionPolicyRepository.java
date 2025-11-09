package com.ezbuy.auth.repository;

import com.ezbuy.authmodel.model.PermissionPolicy;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PermissionPolicyRepository extends R2dbcRepository<PermissionPolicy, UUID> {
    @Query(value = "UPDATE permission_policy pp SET STATE =:state  WHERE id = :id")
    void deleteIndividualPermission(String id, Integer state);

    Mono<PermissionPolicy> getPermissionPolicyById(String id);
}
