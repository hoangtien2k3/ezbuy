package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.model.TenantIdentify;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service interface for handling identification of trusted organizations.
 */
public interface IdentifyService {

    /**
     * Checks if the given list of tenant identifies contains any trusted organization identifies.
     *
     * @param identifies the list of tenant identifies to check
     * @return a Mono emitting a Boolean indicating whether any trusted organization identifies exist
     */
    Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies);
}
