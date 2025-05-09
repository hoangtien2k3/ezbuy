package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.CreateVoucherBatchRequest;
import com.ezbuy.productmodel.dto.request.VoucherBatchRequest;
import com.ezbuy.productmodel.dto.response.VoucherBatchSearchResponse;
import com.ezbuy.productmodel.model.VoucherBatch;
import com.ezbuy.productmodel.model.VoucherType;
import com.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface VoucherBatchService {

    // Lay ra toan bo ban ghi lo voucher
    Mono<List<VoucherBatch>> getAllVoucherBatch();

    // tao moi lo voucher
    Mono<DataResponse<VoucherBatch>> createVoucherBatch(CreateVoucherBatchRequest request);

    // cap nhat lo voucher
    Mono<DataResponse<Boolean>> updateVoucherBatch(String id, CreateVoucherBatchRequest request);

    // tim kiem lo voucher
    Mono<DataResponse<VoucherBatch>> getVoucherBatch(String id);

    // lay ra cac ban ghi loai voucher
    Mono<List<VoucherType>> getAllVoucherType();

    // tim kiem lo voiucher
    Mono<VoucherBatchSearchResponse> searchVoucherBatch(VoucherBatchRequest request);
}
