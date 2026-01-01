package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.ActionLogEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends R2dbcRepository<ActionLogEntity, String> {

}
