package com.ezbuy.paymentservice.repoTemplate;

import java.util.List;
import java.util.Map;

import com.ezbuy.paymentservice.model.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentservice.model.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentservice.model.entity.RequestBanking;
import reactor.core.publisher.Flux;

public interface RequestBankingRepositoryTemplate {

    Flux<RequestBanking> getRequestBankingByListOrderCode(UpdateOrderStateRequest request);

    Flux<Object> updateRequestBankingBatch(Map<String, Integer> request);

    Flux<Object> updateRequestBankingBatchForSync(List<RequestBankingSyncDTO> requestBankingSyncDTOList);
}
