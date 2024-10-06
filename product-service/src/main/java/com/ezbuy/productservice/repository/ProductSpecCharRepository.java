package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.productmodel.dto.Temp;
import com.ezbuy.productmodel.model.ProductSpecChar;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * author: duclv
 */
public interface ProductSpecCharRepository extends R2dbcRepository<ProductSpecChar, String> {

    /**
     * @param telecomServiceAlis
     * @param status
     * @return
     */
    Flux<ProductSpecChar> findProductSpecCharsByTelecomServiceAliasAndStatus(String telecomServiceAlis, Integer status);

    @Query(value = "SELECT COUNT(1) FROM sme_product.product_spec_char psc WHERE psc.telecom_service_id = :id")
    Mono<Long> getTotalSubscriberByTelecomServiceId(String id);

    @Query(value = "SELECT psc.telecom_service_id as serviceid, COUNT(1) as total" +
            " FROM sme_product.product_spec_char psc " +
            " WHERE psc.telecom_service_id IN (:id) " +
            " GROUP BY psc.telecom_service_id")
    Flux<Temp> getTotalSubscriberByTelecomServiceId(List<String> id);

    @Query("select ps.* from product_spec_char ps where telecom_service_id = :telecomServiceId and status = 1 and (:state is null or state = :state) order by display_order ")
    Flux<ProductSpecCharAndValDTO> findByTelecomId(String telecomServiceId, Boolean state);

    @Query("select * from sme_product.product_spec_char where status='1'and telecom_service_id=:telecomServiceId ")
    Flux<ProductSpecCharAndValDTO> listCodeSpectCharValue(String telecomServiceId);

}
