package com.ezbuy.orderservice.repoTemplate;

import com.ezbuy.sme.ordermodel.model.InvoiceInfoHistory;
import com.ezbuy.sme.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import reactor.core.publisher.Flux;


public interface InvoiceInfoHistoryRepositoryTemplate {

    Flux<InvoiceInfoHistory> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request);
}
