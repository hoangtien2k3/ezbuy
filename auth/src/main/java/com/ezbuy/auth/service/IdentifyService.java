package com.ezbuy.auth.service;

import java.util.List;

import com.ezbuy.auth.model.postgresql.TenantIdentify;

import reactor.core.publisher.Mono;

public interface IdentifyService {

    Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies);
}
