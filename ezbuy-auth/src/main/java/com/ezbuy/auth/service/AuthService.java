package com.ezbuy.auth.service;

import com.ezbuy.authmodel.dto.AccessToken;
import com.ezbuy.authmodel.dto.request.*;
import com.ezbuy.authmodel.dto.response.GetActionLoginReportResponse;
import com.ezbuy.authmodel.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.authmodel.dto.response.Permission;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.UserOtp;
import com.ezbuy.core.model.response.DataResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest);

    Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin);

    Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest);

    Mono<Boolean> logout(LogoutRequest logoutRequest);

    Mono<List<Permission>> getPermission(String clientId);

    Mono<List<Permission>> getOrgPermission(String clientId, String idNo, String orgId);

    Mono<List<Permission>> getPermission(String clientId, String orgId, String userId);

    Mono<UserOtp> signUp(SignupRequest signupRequest);

    Mono<UserOtp> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange);

    Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange);

    Mono<Individual> confirmOtpAndCreateUser(CreateAccount createAccount);

    Mono<List<String>> getAllUserId();

    Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String request);

    Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request);

    Mono<DataResponse<Map<String, String>>> confirmOTP(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);

    Mono<DataResponse<String>> generateOtp(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);
}
