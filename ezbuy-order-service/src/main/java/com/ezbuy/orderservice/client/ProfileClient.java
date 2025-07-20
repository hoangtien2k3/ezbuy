package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.ordermodel.dto.response.GetListProductOfferingRecordResponse;
import com.ezbuy.ordermodel.dto.response.GetListServiceRecordResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface ProfileClient {
    /**
     * Goi SOAP API lay danh sach ho so dua vao ma dich vu
     *
     * @param request
     * @return
     */
    Mono<Optional<GetListServiceRecordResponse>> getListServiceRecord(GetListServiceRecordRequest request);
    /**
     * Goi SOAP API lay danh sach ho so dua vao ma goi cuoc
     *
     * @param request
     * @return
     */
    Mono<Optional<GetListProductOfferingRecordResponse>> getListProductOfferingRecordResponse(
            GetListProductOfferingRecordRequest request);
}
