package com.ezbuy.productservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.model.SyncHistory;
import com.ezbuy.sme.productmodel.model.SyncHistoryDetail;
import com.ezbuy.sme.productmodel.request.CreateSyncHistoryRequest;
import com.ezbuy.sme.productmodel.request.SyncCustomerInfoRequest;
import reactor.core.publisher.Mono;

public interface SyncHistoryService {
    /**
     * Tao tranId dong bo thong tin hang hoa
     * @param request
     * @return
     */
    Mono<DataResponse<SyncHistory>> createSyncHistoryTrans(CreateSyncHistoryRequest request);

    /**
     * Tao tranId dong bo thong tin hang hoa
     * @param detail
     * @return
     */
    Mono<DataResponse<SyncHistoryDetail>> createSyncHistoryDetail(SyncHistoryDetail detail);


}
