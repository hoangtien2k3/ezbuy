package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.Characteristic;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends R2dbcRepository<Characteristic, UUID> {}
