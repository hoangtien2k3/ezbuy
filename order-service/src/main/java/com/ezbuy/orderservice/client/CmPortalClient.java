package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.request.GetGroupsCAinfoRequest;
import com.ezbuy.ordermodel.dto.sale.ResponseCM;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface CmPortalClient {

    Mono<Optional<ResponseCM>> getGroupsInfo(GetGroupsCAinfoRequest request);

    Mono<Optional<ResponseCM>> getGroupsMembersInfo(GetGroupsCAinfoRequest request);
}
