package com.ezbuy.productservice.repository;

import com.ezbuy.sme.productmodel.model.SyncHistoryDetail;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SyncHistoryDetailRepository extends R2dbcRepository<SyncHistoryDetail, UUID> {
}
