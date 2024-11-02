package com.ezbuy.paymentservice.repoTemplate;

import com.ezbuy.paymentmodel.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentmodel.model.RequestBanking;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Flux;

public interface RequestBankingRepositoryTemplate {

    Flux<RequestBanking> getRequestBankingByListOrderCode(UpdateOrderStateRequest request);

    Flux<Object> updateRequestBankingBatch(Map<String, Integer> request);

    Flux<Object> updateRequestBankingBatchForSync(List<RequestBankingSyncDTO> requestBankingSyncDTOList);
}
