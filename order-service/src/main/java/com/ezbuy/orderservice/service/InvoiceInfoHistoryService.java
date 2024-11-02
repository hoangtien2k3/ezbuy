package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.model.InvoiceInfoHistory;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface InvoiceInfoHistoryService {

    /**
     * tao moi lich su Xuat hoa don
     *
     * @param request
     * @return
     */
    Mono<DataResponse<InvoiceInfoHistory>> createInvoiceInfoHistory(CreateInvoiceInfoHistoryRequest request);

    /**
     * lay thong tin lich su xuat hoa don
     *
     * @param request
     * @return
     */
    Mono<Optional<List<InvoiceInfoHistory>>> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request);
}
