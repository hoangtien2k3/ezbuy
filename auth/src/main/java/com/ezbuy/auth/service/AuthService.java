package com.ezbuy.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.server.ServerWebExchange;

import com.ezbuy.auth.model.dto.AccessToken;
import com.ezbuy.auth.model.dto.request.*;
import com.ezbuy.auth.model.dto.response.GetActionLoginReportResponse;
import com.ezbuy.auth.model.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.model.dto.response.Permission;
import com.ezbuy.auth.model.postgresql.Individual;
import com.ezbuy.auth.model.postgresql.UserOtp;
import com.ezbuy.framework.model.response.DataResponse;

import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest);

    //    Mono<Optional<AccessToken>> getToken(ClientLogin clientLogin, ServerWebExchange serverWebExchange);

    Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin);

    Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest);

    Mono<Boolean> logout(LogoutRequest logoutRequest);

    Mono<List<Permission>> getPermission(String clientId);

    Mono<List<Permission>> getOrgPermission(String clientId, String idNo, String orgId);

    Mono<List<Permission>> getPermission(String clientId, String orgId, String userId);

    Mono<UserOtp> signUp(SignupRequest signupRequest);

    //    Mono<Individual> createAccount(CreateOrgAccount createOrgAccount);

    Mono<UserOtp> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange);

    Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange);

    Mono<Individual> confirmOtpAndCreateUser(CreateAccount createAccount);

    Mono<List<String>> getAllUserId();

    Mono<Void> createUserTestPerformence(int startIndex, int numAccount);

    Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String request);

    Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request);

    /**
     * Xac nhan OTP trong bang sme_user.user_otp dung thong tin va con han
     * @param confirmOTPRequest
     * @param serverWebExchange
     * @return
     */
    Mono<DataResponse> confirmOTP(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);

    /**
     * Ham sinh ma OTP trong bang sme_user.user_otp
     * @param confirmOTPRequest
     * @param serverWebExchange
     * @return
     */
    Mono<DataResponse<String>> generateOtp(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);
}
