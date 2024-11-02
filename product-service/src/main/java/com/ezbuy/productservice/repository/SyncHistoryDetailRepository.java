package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.SyncHistoryDetail;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncHistoryDetailRepository extends R2dbcRepository<SyncHistoryDetail, UUID> {}
