package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.UserOtpRequest;
import com.ezbuy.authmodel.dto.response.SearchUserOtpResponse;
import reactor.core.publisher.Mono;

public interface UserOtpService {

    /**
     * Function : Get otp log list of user_otp
     *
     * @param request
     *            : search params
     * @return
     */
    Mono<SearchUserOtpResponse> search(UserOtpRequest request);
}
