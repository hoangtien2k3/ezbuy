package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authmodel.dto.AccessToken;
import com.ezbuy.authmodel.dto.request.*;
import com.ezbuy.authmodel.dto.response.GetActionLoginReportResponse;
import com.ezbuy.authmodel.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.authmodel.dto.response.Permission;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.UserOtp;
import com.ezbuy.authservice.service.AuthService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Auth.PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = UrlPaths.Auth.LOGIN)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        return authService
                .getToken(loginRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(value = UrlPaths.Auth.GET_TOKEN_FROM_AUTHORIZATION_CODE)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> getTokenFromAuthorizationCode(
            @Valid @RequestBody ProviderLogin providerLogin) {
        return authService
                .getToken(providerLogin)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(value = UrlPaths.Auth.GET_TOKEN_FROM_PROVIDER_CODE)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> getTokenFromAuthorizationProviderCode(
            @Valid @RequestBody ClientLogin clientLogin) {
        return authService.getToken(clientLogin).map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping(value = UrlPaths.Auth.GET_PERMISSION)
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getPermission(
            @RequestParam(name = "clientId") String clientId) {
        return authService
                .getPermission(clientId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping(value = UrlPaths.Auth.GET_ORG_PERMISSION)
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getOrgPermission(
            @RequestParam(name = "clientId", defaultValue = "web-client") String clientId,
            @RequestParam(name = "organizationId", required = false) String organizationId,
            @RequestParam(name = "idNo", required = false) String idNo) {
        return authService
                .getOrgPermission(clientId, idNo, organizationId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(value = UrlPaths.Auth.REFRESH_TOKEN)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService
                .refreshToken(refreshTokenRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(value = UrlPaths.Auth.LOGOUT)
    public Mono<ResponseEntity<DataResponse<Boolean>>> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        return authService.logout(logoutRequest).map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(value = UrlPaths.Auth.SIGNUP)
    public Mono<DataResponse<UserOtp>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signUp(signupRequest).map(su -> new DataResponse<>("otp.success", null));
    }

    @PostMapping(value = UrlPaths.Auth.FORGOT_PASSWORD)
    public Mono<DataResponse<UserOtp>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest).map(su -> new DataResponse<>("otp.success", null));
    }

    @PostMapping(UrlPaths.Auth.CHANGE_PASSWORD)
    public Mono<DataResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, ServerWebExchange serverWebExchange) {
        return authService.changePassword(request, serverWebExchange).thenReturn(DataResponse.success(null));
    }

    @PostMapping(UrlPaths.Auth.RESET_PASSWORD)
    public Mono<DataResponse<UserOtp>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange) {
        return authService
                .resetPassword(resetPasswordRequest, serverWebExchange)
                .map(success -> new DataResponse<>(DataUtil.safeToString(success.getMessage()), null));
    }

    @PostMapping(UrlPaths.Auth.CONFIRM_OTP_FOR_CREATE_ACCOUNT)
    public Mono<ResponseEntity<DataResponse<Individual>>> confirmOtpAndCreateAccount(
            @Valid @RequestBody CreateAccount createAccount) {
        return authService
                .confirmOtpAndCreateUser(createAccount)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(value = UrlPaths.Auth.GET_ALL_USERID)
    public Mono<List<String>> getAllUserId() {
        return authService.getAllUserId();
    }

    @PostMapping(UrlPaths.Auth.CREATE_TEST_PERFORMANCE_USER)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<Void>>> confirmOtpAndCreateAccount(
            @RequestParam(name = "startIndex") Integer startIndex,
            @RequestParam(name = "numAccount") Integer numAccount) {
        return authService
                .createUserTestPerformence(startIndex, numAccount)
                .then(Mono.fromCallable(() -> ResponseEntity.ok(new DataResponse<>("success", null))));
    }

    @GetMapping(UrlPaths.Auth.GET_TWO_WAY_PASSWORD)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<GetTwoWayPasswordResponse>>> getTwoWayPassword(
            @RequestParam(name = "username", required = false) String username) {
        return authService
                .getTwoWayPassword(username)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping(value = UrlPaths.Auth.ACTION_LOGIN)
    public Mono<ResponseEntity<DataResponse<GetActionLoginReportResponse>>> getActionLoginReport(
            @RequestBody GetActionLoginReportRequest request) {
        return authService
                .getActionLoginReport(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping(UrlPaths.Auth.CONFIRM_OTP)
    public Mono<DataResponse<Map<String, String>>> confirmOTP(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService
                .confirmOTP(confirmOTPRequest, serverWebExchange)
                .map(success -> new DataResponse<>(DataUtil.safeToString(success.getMessage()), success.getData()));
    }

    @PostMapping(UrlPaths.Auth.GENERATE_OTP)
    public Mono<DataResponse<String>> generateOtp(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService.generateOtp(confirmOTPRequest, serverWebExchange);
    }
}
