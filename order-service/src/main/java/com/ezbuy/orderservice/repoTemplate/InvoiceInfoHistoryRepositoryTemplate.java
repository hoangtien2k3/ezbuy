package com.ezbuy.orderservice.repoTemplate;

import com.ezbuy.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.model.InvoiceInfoHistory;
import reactor.core.publisher.Flux;

public interface InvoiceInfoHistoryRepositoryTemplate {

    Flux<InvoiceInfoHistory> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request);
}
