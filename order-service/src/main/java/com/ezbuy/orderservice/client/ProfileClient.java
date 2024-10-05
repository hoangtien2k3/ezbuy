package com.ezbuy.orderservice.client;

import com.ezbuy.sme.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.sme.ordermodel.dto.response.GetListProductOfferingRecordResponse;
import com.ezbuy.sme.ordermodel.dto.response.GetListServiceRecordResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProfileClient {
    /**
     * Goi SOAP API lay danh sach ho so dua vao ma dich vu
     * @param request
     * @return
     */
    Mono<Optional<GetListServiceRecordResponse>> getListServiceRecord(GetListServiceRecordRequest request);
    /**
     * Goi SOAP API lay danh sach ho so dua vao ma goi cuoc
     * @param request
     * @return
     */
    Mono<Optional<GetListProductOfferingRecordResponse>> getListProductOfferingRecordResponse(GetListProductOfferingRecordRequest request);
}
