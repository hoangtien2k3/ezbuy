package com.ezbuy.auth.service;

import com.ezbuy.auth.model.dto.AccessToken;
import com.ezbuy.auth.model.request.ChangePasswordRequest;
import com.ezbuy.auth.model.request.ConfirmOTPRequest;
import com.ezbuy.auth.model.request.CreateAccount;
import com.ezbuy.auth.model.request.ForgotPasswordRequest;
import com.ezbuy.auth.model.request.LoginRequest;
import com.ezbuy.auth.model.request.LogoutRequest;
import com.ezbuy.auth.model.request.ProviderLogin;
import com.ezbuy.auth.model.request.RefreshTokenRequest;
import com.ezbuy.auth.model.request.ResetPasswordRequest;
import com.ezbuy.auth.model.request.SignupRequest;
import com.ezbuy.auth.model.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.model.response.Permission;
import com.ezbuy.auth.model.entity.IndividualEntity;
import com.ezbuy.auth.model.entity.UserOtpEntity;
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

    Mono<UserOtpEntity> signUp(SignupRequest signupRequest);

    Mono<UserOtpEntity> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Mono<DataResponse<Object>> resetPassword(ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange);

    Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange);

    Mono<IndividualEntity> confirmOtpAndCreateUser(CreateAccount createAccount);

    Mono<List<String>> getAllUserId();

    Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String request);

    Mono<DataResponse<Map<String, String>>> confirmOTP(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);

    Mono<DataResponse<String>> generateOtp(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);
}
