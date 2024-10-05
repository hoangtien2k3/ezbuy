package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.sme.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.sme.productmodel.dto.ProductSpecCharValueDTO;
import com.ezbuy.sme.productmodel.model.ProductSpecCharAndValue;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProductSpecRepo {

    Flux<ProductSpecCharAndValue> findByTelecomServiceAliasIncludeValue(String telecomServiceAlis);

    Flux<ProductSpecCharAndValDTO> findAllActive();

    Flux<Object> updateBatchProductSpecCharAndVal(List<ProductSpecCharAndValDTO> productSpecCharAndValList);

    Flux<Object> updateBatchProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList);

    Flux<Object> deleteBatchProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList);

    Flux<Object> deleteBatchProductSpecCharAndVal(List<ProductSpecCharAndValDTO> productSpecCharAndValList);

    Flux<Object> updateProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList);
    Flux<Object> updateProductSpecCharValueNew(List<ProductSpecCharValueDTO> productSpecCharValueList);
    Flux<Object> updateProductSpecChar(List<ProductSpecCharAndValDTO> productSpecCharAndValDTOS);
    Flux<Object> updateProductSpecCharNew(List<ProductSpecCharAndValDTO> productSpecCharAndValDTOS);

    Flux<Object> deleteProductSpecChar(String productSpecCharId);
}
