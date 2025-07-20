package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoRequest;
import com.ezbuy.ordermodel.dto.request.UpdateInvoiceInfoRequest;
import com.ezbuy.ordermodel.model.InvoiceInfo;
import com.reactify.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface InvoiceInfoService {
    /**
     * lay thong tin xuat hoa don
     *
     * @param userId
     *            id user
     * @param organizationId
     *            id doanh nghiep
     * @return
     */
    Mono<Optional<InvoiceInfo>> getByUserIdAndOrganizationId(String userId, String organizationId);

    /**
     * tao moi thong tin Xuat hoa don
     *
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfo>> createInvoiceInfo(CreateInvoiceInfoRequest request);

    /**
     * cap nhat thong tin xuat hoa don
     *
     * @param id
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfo>> updateInvoiceInfo(String id, UpdateInvoiceInfoRequest request);
}
