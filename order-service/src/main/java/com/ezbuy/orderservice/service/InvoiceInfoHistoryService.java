package com.ezbuy.orderservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.dto.request.CreateInvoiceInfoHistoryRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import com.ezbuy.sme.ordermodel.model.InvoiceInfoHistory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface InvoiceInfoHistoryService {

    /**
     * tao moi lich su Xuat hoa don
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfoHistory>> createInvoiceInfoHistory(CreateInvoiceInfoHistoryRequest request);

    /**
     * lay thong tin lich su xuat hoa don
     * @param request
     * @return
     */
    Mono<Optional<List<InvoiceInfoHistory>>> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request);


}
