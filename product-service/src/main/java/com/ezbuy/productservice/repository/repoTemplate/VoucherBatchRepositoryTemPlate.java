package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.request.VoucherBatchRequest;
import com.ezbuy.productmodel.model.VoucherBatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherBatchRepositoryTemPlate {
    //tim kiem lo voucher
    Flux<VoucherBatch> queryVoucherBatch(VoucherBatchRequest request);

    //dem so luong bang ghi lo voucher
    Mono<Long> countVoucherBatch(VoucherBatchRequest request);
}
