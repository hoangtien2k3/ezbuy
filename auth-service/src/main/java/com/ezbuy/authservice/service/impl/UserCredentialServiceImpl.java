package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.model.UserCredential;
import com.ezbuy.authservice.repository.UserCredentialRepository;
import com.ezbuy.authservice.service.UserCredentialService;
import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public Mono<Optional<UserCredential>> getByUserId(String userId) {
            return userCredentialRepository.findByUserId(userId)
            .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
            .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    @Override
    public Mono<Optional<UserCredential>> getCurrentUserCredential() {
        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> {
            String username = tokenUser.getUsername();
            return userCredentialRepository.findByUsername(username)
                    .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
        }).switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ACCESS_DENIED, "token.error")));
    }
}
