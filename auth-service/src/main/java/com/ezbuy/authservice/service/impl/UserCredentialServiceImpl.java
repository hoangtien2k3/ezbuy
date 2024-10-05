package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.model.UserCredential;
import com.ezbuy.authservice.repository.UserCredentialRepository;
import com.ezbuy.authservice.service.UserCredentialService;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.SecurityUtils;
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
    public Mono<Optional<UserCredential>> getByUserId(String userId) {
        return userCredentialRepository
                .findByUserId(userId)
                .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    @Override
    public Mono<Optional<UserCredential>> getCurrentUserCredential() {
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
