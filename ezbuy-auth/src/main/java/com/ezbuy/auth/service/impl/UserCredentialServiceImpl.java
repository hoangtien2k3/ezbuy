package com.ezbuy.auth.service.impl;

import com.ezbuy.auth.model.entity.UserCredentialEntity;
import com.ezbuy.auth.repository.UserCredentialRepository;
import com.ezbuy.auth.service.UserCredentialService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.util.SecurityUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;

    @Override
    public Mono<Optional<UserCredentialEntity>> getByUserId(String userId) {
        return userCredentialRepository
                .findByUserId(userId)
                .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    @Override
    public Mono<Optional<UserCredentialEntity>> getCurrentUserCredential() {
        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> {
                    String username = tokenUser.getUsername();
                    return userCredentialRepository
                            .findByUsername(username)
                            .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                            .switchIfEmpty(Mono.error(
                                    new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ACCESS_DENIED, "token.error")));
    }
}
