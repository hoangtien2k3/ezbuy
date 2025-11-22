package com.ezbuy.productservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ezbuy.productservice.model.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<Product, UUID> {

    Mono<Product> findFirstByCode(String code);

    Mono<Product> findFirstById(String id);

    Mono<Product> findFirstByIdAndStatus(String id, Integer status);

    Flux<Product> findAllByIdIn(List<String> productIdList);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
