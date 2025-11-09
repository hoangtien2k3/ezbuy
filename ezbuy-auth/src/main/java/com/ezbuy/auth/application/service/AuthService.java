package com.ezbuy.auth.application.service;

import com.ezbuy.auth.application.dto.AccessToken;
import com.ezbuy.auth.application.dto.request.ChangePasswordRequest;
import com.ezbuy.auth.application.dto.request.ConfirmOTPRequest;
import com.ezbuy.auth.application.dto.request.CreateAccount;
import com.ezbuy.auth.application.dto.request.ForgotPasswordRequest;
import com.ezbuy.auth.application.dto.request.GetActionLoginReportRequest;
import com.ezbuy.auth.application.dto.request.LoginRequest;
import com.ezbuy.auth.application.dto.request.LogoutRequest;
import com.ezbuy.auth.application.dto.request.ProviderLogin;
import com.ezbuy.auth.application.dto.request.RefreshTokenRequest;
import com.ezbuy.auth.application.dto.request.ResetPasswordRequest;
import com.ezbuy.auth.application.dto.request.SignupRequest;
import com.ezbuy.auth.application.dto.response.GetActionLoginReportResponse;
import com.ezbuy.auth.application.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.application.dto.response.Permission;
import com.ezbuy.auth.domain.model.entity.IndividualEntity;
import com.ezbuy.auth.domain.model.entity.UserOtpEntity;
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

    Mono<UserOtpEntity> signUp(SignupRequest signupRequest);

    Mono<UserOtpEntity> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange);

    Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange);

    Mono<IndividualEntity> confirmOtpAndCreateUser(CreateAccount createAccount);

    Mono<List<String>> getAllUserId();

    Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String request);

    Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request);

    Mono<DataResponse<Map<String, String>>> confirmOTP(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);

    Mono<DataResponse<String>> generateOtp(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);
}
