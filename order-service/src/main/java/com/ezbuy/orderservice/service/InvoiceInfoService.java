package com.ezbuy.orderservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.dto.request.CreateInvoiceInfoRequest;
import com.ezbuy.sme.ordermodel.dto.request.UpdateInvoiceInfoRequest;
import com.ezbuy.sme.ordermodel.model.InvoiceInfo;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InvoiceInfoService {
    /**
     * lay thong tin xuat hoa don
     * @param userId id user
     * @param organizationId id doanh nghiep
     * @return
     */
    Mono<Optional<InvoiceInfo>> getByUserIdAndOrganizationId(String userId, String organizationId);

    /**
     * tao moi thong tin Xuat hoa don
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfo>> createInvoiceInfo(CreateInvoiceInfoRequest request);

    /**
     * cap nhat thong tin xuat hoa don
     * @param id
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfo>> updateInvoiceInfo(String id, UpdateInvoiceInfoRequest request);
}
