package com.ezbuy.auth.service;

import com.ezbuy.auth.model.postgresql.TenantIdentify;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IdentifyService {

    Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies);

}
