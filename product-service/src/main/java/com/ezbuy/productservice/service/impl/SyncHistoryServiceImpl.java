package com.ezbuy.productservice.service.impl;

import com.ezbuy.productservice.client.SyncClient;
import com.ezbuy.productservice.repository.SyncHistoryDetailRepository;
import com.ezbuy.productservice.repository.SyncHistoryRepository;
import com.ezbuy.productservice.service.SyncHistoryService;
import com.ezbuy.sme.framework.constants.CommonErrorCode;
import com.ezbuy.sme.framework.exception.BusinessException;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.SecurityUtils;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.productmodel.constants.Constants;
import com.ezbuy.sme.productmodel.model.SyncHistory;
import com.ezbuy.sme.productmodel.model.SyncHistoryDetail;
import com.ezbuy.sme.productmodel.request.CreateSyncHistoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ezbuy.sme.framework.constants.MessageConstant.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncHistoryServiceImpl implements SyncHistoryService {
    private final SyncHistoryRepository syncHistoryRepository;
    private final SyncClient syncClient;
    private final SyncHistoryDetailRepository syncHistoryDetailRepository;
    private static final Integer ACTIVE = 1;

    @Override
    public Mono<DataResponse<SyncHistory>> createSyncHistoryTrans(CreateSyncHistoryRequest request) {
        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> Mono.zip(syncClient.getProductSyncTransId(tokenUser.getUsername()),
                syncHistoryRepository.getSysDate()).flatMap(tuple -> {
            String id = UUID.randomUUID().toString();
            String syncTransid = tuple.getT1();
            LocalDateTime now = tuple.getT2();
            String userName = tokenUser.getUsername();
            SyncHistory syncHistory = SyncHistory.builder()
                    .id(id)
                    .orgId(request.getOrgId())
                    .idNo(request.getIdNo())
                    .syncTransId(syncTransid)
                    .action(request.getAction())
                    .serviceType(request.getServiceType())
                    .dstService(request.getDstService())
                    .syncType(request.getSyncType())
                    .objectType(request.getObjectType())
                    .state(Constants.SYNC_HISTORY_STATE.WAIT_SYNC)
                    .status(ACTIVE)
                    .retry(0)
                    .createAt(now)
                    .createBy(userName)
                    .updateAt(now)
                    .updateBy(userName)
                    .isNew(true)
                    .build();
            return syncHistoryRepository.save(syncHistory)
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sync.history.create.new")))
                    .flatMap(x -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), syncHistory)));
        }));
    }

    @Override
    public Mono<DataResponse<SyncHistoryDetail>> createSyncHistoryDetail(SyncHistoryDetail detail) {
        return syncHistoryDetailRepository.save(detail)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sync.history.create.new")))
                .flatMap(x -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), detail)));
    }
}
