package com.ezbuy.productservice.repository;

import com.ezbuy.sme.productmodel.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<Product, UUID> {
    Mono<Product> findFirstByCode(String code);

    Mono<Product> findFirstById(String id);

    Mono<Product> findFirstByIdAndStatus(String id, Integer status);

    /**
     * Ham lay danh sach hang hoa theo danh sach id hang hoa
     * @param productIdList
     * @return
     */
    Flux<Product> findAllByIdIn(List<String> productIdList);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
