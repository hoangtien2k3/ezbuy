package com.ezbuy.authservice.repotemplate;

import com.ezbuy.authmodel.dto.request.UserOtpRequest;
import com.ezbuy.authmodel.model.UserOtp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserOtpRepositoryTemplate {

    /**
     * Function : Get otp log list of user_otp
     *
     * @param request
     *            : search params
     * @return
     */
    Flux<UserOtp> search(UserOtpRequest request);

    /**
     * Function : Get record total
     *
     * @param request
     *            : search params
     * @return
     */
    Mono<Long> count(UserOtpRequest request);
}
