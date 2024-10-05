package com.ezbuy.orderservice.service.impl;

import com.ezbuy.sme.framework.constants.MessageConstant;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.ordermodel.constants.UrlPaths;
import com.ezbuy.sme.ordermodel.dto.request.GetGroupsCAinfoRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.sme.ordermodel.dto.response.GetListProductOfferingRecordResponse;
import com.ezbuy.sme.ordermodel.dto.response.GetListServiceRecordResponse;
import com.ezbuy.orderservice.client.ProfileClient;
import com.ezbuy.orderservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
    private final ProfileClient profileClient;

    @Override
    public Mono<Optional<GetListProductOfferingRecordResponse>> getListProductOfferingRecord(GetListProductOfferingRecordRequest request) {
        return profileClient.getListProductOfferingRecordResponse(request)
                .doOnError(err -> {
                    log.error("Handle Profile Error", err);
                });
    }

    @Override
    public Mono<Optional<GetListServiceRecordResponse>> getListServiceRecord(GetListServiceRecordRequest request) {
        return profileClient.getListServiceRecord(request)
                .doOnError(err -> {
                    log.error("Handle Profile Error", err);
                });
    }
}
