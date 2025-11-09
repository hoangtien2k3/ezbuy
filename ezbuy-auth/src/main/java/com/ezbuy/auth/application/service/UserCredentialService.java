package com.ezbuy.auth.application.service;

import com.ezbuy.auth.domain.model.entity.UserCredentialEntity;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface UserCredentialService {
    /**
     * lay thong tin user dang nhap
     *
     * @param UserId
     *            id user
     * @return
     */
    Mono<Optional<UserCredentialEntity>> getByUserId(String UserId);

    /**
     * lay thong tin user dang nhap
     *
     * @param
     * @return
     */
    Mono<Optional<UserCredentialEntity>> getCurrentUserCredential();
}
