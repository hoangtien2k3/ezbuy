package com.ezbuy.orderservice.client;

import com.ezbuy.sme.ordermodel.dto.request.GetGroupsCAinfoRequest;
import com.ezbuy.sme.ordermodel.dto.sale.ResponseCM;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CmPortalClient {

    Mono<Optional<ResponseCM>> getGroupsInfo(GetGroupsCAinfoRequest request);

    Mono<Optional<ResponseCM>> getGroupsMembersInfo(GetGroupsCAinfoRequest request);
}
