package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.model.UserCredential;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserCredentialService {
    /**
     * lay thong tin user dang nhap
     * @param UserId id user
     * @return
     */
    Mono<Optional<UserCredential>> getByUserId(String UserId);

    /**
     * lay thong tin user dang nhap
     * @param
     * @return
     */
    Mono<Optional<UserCredential>> getCurrentUserCredential();
}
