package com.ezbuy.paymentservice.repoTemplate;

import com.ezbuy.paymentmodel.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentmodel.model.RequestBanking;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface RequestBankingRepositoryTemplate {

    Flux<RequestBanking> getRequestBankingByListOrderCode(UpdateOrderStateRequest request);

    Flux<Object> updateRequestBankingBatch(Map<String, Integer> request);

    Flux<Object> updateRequestBankingBatchForSync(List<RequestBankingSyncDTO> requestBankingSyncDTOList);
}
