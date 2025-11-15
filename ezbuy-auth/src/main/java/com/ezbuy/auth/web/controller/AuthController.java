package com.ezbuy.auth.web.controller;

import com.ezbuy.auth.application.dto.request.ChangePasswordRequest;
import com.ezbuy.auth.application.dto.request.ClientLogin;
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
import com.ezbuy.auth.application.dto.AccessToken;
import com.ezbuy.auth.application.dto.response.GetActionLoginReportResponse;
import com.ezbuy.auth.application.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.application.dto.response.Permission;
import com.ezbuy.auth.domain.model.entity.IndividualEntity;
import com.ezbuy.auth.domain.model.entity.UserOtpEntity;
import com.ezbuy.auth.application.service.AuthService;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
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
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        return authService
                .getToken(loginRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/provider-code")
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> getTokenFromAuthorizationCode(
            @Valid @RequestBody ProviderLogin providerLogin) {
        return authService
                .getToken(providerLogin)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/client-code")
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> getTokenFromAuthorizationProviderCode(
            @Valid @RequestBody ClientLogin clientLogin) {
        return authService.getToken(clientLogin).map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping("/permissions")
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getPermission(
            @RequestParam(name = "clientId") String clientId) {
        return authService
                .getPermission(clientId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping("/org-permissions")
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getOrgPermission(
            @RequestParam(name = "clientId", defaultValue = "ezbuyClient") String clientId,
            @RequestParam(name = "organizationId", required = false) String organizationId,
            @RequestParam(name = "idNo", required = false) String idNo) {
        return authService
                .getOrgPermission(clientId, idNo, organizationId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService
                .refreshToken(refreshTokenRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<DataResponse<Boolean>>> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        return authService.logout(logoutRequest).map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/signup")
    public Mono<DataResponse<UserOtpEntity>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signUp(signupRequest).map(su -> new DataResponse<>("otp.success", null));
    }

    @PostMapping("/forgot-password")
    public Mono<DataResponse<UserOtpEntity>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest).map(su -> new DataResponse<>("otp.success", null));
    }

    @PostMapping("/change-password")
    public Mono<DataResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, ServerWebExchange serverWebExchange) {
        return authService.changePassword(request, serverWebExchange).thenReturn(DataResponse.success(null));
    }

    @PostMapping("/reset-password")
    public Mono<DataResponse<UserOtpEntity>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange) {
        return authService
                .resetPassword(resetPasswordRequest, serverWebExchange)
                .map(success -> new DataResponse<>(DataUtil.safeToString(success.getMessage()), null));
    }

    @PostMapping("/confirm-create")
    public Mono<ResponseEntity<DataResponse<IndividualEntity>>> confirmOtpAndCreateAccount(
            @Valid @RequestBody CreateAccount createAccount) {
        return authService
                .confirmOtpAndCreateUser(createAccount)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping("/get-all")
    public Mono<List<String>> getAllUserId() {
        return authService.getAllUserId();
    }

    @GetMapping("/two-way-password")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<GetTwoWayPasswordResponse>>> getTwoWayPassword(
            @RequestParam(name = "username", required = false) String username) {
        return authService
                .getTwoWayPassword(username)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @GetMapping("/action-login")
    public Mono<ResponseEntity<DataResponse<GetActionLoginReportResponse>>> getActionLoginReport(
            @RequestBody GetActionLoginReportRequest request) {
        return authService
                .getActionLoginReport(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }

    @PostMapping("/confirm-otp")
    public Mono<DataResponse<Map<String, String>>> confirmOTP(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService
                .confirmOTP(confirmOTPRequest, serverWebExchange)
                .map(success -> new DataResponse<>(DataUtil.safeToString(success.getMessage()), success.getData()));
    }

    @PostMapping("/generate-otp")
    public Mono<DataResponse<String>> generateOtp(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService.generateOtp(confirmOTPRequest, serverWebExchange);
    }
}
