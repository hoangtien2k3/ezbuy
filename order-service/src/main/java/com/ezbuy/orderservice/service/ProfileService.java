package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.ordermodel.dto.response.GetListProductOfferingRecordResponse;
import com.ezbuy.ordermodel.dto.response.GetListServiceRecordResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProfileService {
    /**
     * Ham nay goi de tra ve danh sach ho so theo danh sach dich vu
     * dua vao telecomServiceAlias BCCS
     * @param request
     * @return danh sach ho so voi dich vu tuong ung
     */
    Mono<Optional<GetListServiceRecordResponse>> getListServiceRecord(GetListServiceRecordRequest request);
    /**
     * Ham nay goi de tra ve danh sach ho so theo ma goi cuoc productOfferingCode
     * @param request truyen vao danh sach ma goi cuoc
     * @return danh sach ho so voi ma goi cuoc tuong ung
     */
    Mono<Optional<GetListProductOfferingRecordResponse>> getListProductOfferingRecord(GetListProductOfferingRecordRequest request);
}
