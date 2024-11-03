package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.model.SyncHistory;
import com.ezbuy.productmodel.model.SyncHistoryDetail;
import com.ezbuy.productmodel.request.CreateSyncHistoryRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface SyncHistoryService {
    /**
     * Tao tranId dong bo thong tin hang hoa
     *
     * @param request
     * @return
     */
    Mono<DataResponse<SyncHistory>> createSyncHistoryTrans(CreateSyncHistoryRequest request);

    /**
     * Tao tranId dong bo thong tin hang hoa
     *
     * @param detail
     * @return
     */
    Mono<DataResponse<SyncHistoryDetail>> createSyncHistoryDetail(SyncHistoryDetail detail);
}
