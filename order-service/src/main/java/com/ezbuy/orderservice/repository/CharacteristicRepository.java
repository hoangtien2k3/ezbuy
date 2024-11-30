package com.ezbuy.orderservice.repository;

import java.util.UUID;

import com.ezbuy.ordermodel.model.Characteristic;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends R2dbcRepository<Characteristic, UUID> {}
