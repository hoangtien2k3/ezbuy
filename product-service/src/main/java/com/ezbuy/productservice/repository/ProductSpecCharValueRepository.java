package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.dto.ProductSpecCharValueDTO;
import com.ezbuy.productmodel.model.ProductSpecCharValue;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * author: duclv
 */
public interface ProductSpecCharValueRepository extends R2dbcRepository<ProductSpecCharValue, String> {

    /**
     * @param productSpecCharId
     * @return
     */
    @Query(
            "select ps.* from product_spec_char_value ps where ps.product_spec_char_id = :productSpecCharId and ps.status = 1 and (:state is null or state = :state) order by display_order ")
    Flux<ProductSpecCharValueDTO> findByProductSpecCharId(String productSpecCharId, Boolean state);
}
