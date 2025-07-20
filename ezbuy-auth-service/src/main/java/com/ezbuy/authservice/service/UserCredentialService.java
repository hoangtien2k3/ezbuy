package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.model.UserCredential;
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
    Mono<Optional<UserCredential>> getByUserId(String UserId);

    /**
     * lay thong tin user dang nhap
     *
     * @param
     * @return
     */
    Mono<Optional<UserCredential>> getCurrentUserCredential();
}
