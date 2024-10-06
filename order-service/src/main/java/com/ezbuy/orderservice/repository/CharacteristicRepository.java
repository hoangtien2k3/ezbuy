package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.dto.pricing.Characteristic;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacteristicRepository extends R2dbcRepository<Characteristic, UUID> {

}
