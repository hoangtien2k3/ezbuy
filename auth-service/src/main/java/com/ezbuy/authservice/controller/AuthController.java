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
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.Translator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Auth.PREFIX)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //    private final UserService userService;
    private static final String SUCCESS_MESSAGE = "common.success";

    @PostMapping(value = UrlPaths.Auth.LOGIN)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        return authService.getToken(loginRequest).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @PostMapping(value = UrlPaths.Auth.GET_TOKEN_FROM_AUTHORIZATION_CODE)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> getTokenFromAuthorizationCode(
            @Valid @RequestBody ProviderLogin providerLogin) {
        return authService
                .getToken(providerLogin)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @PostMapping(value = UrlPaths.Auth.GET_TOKEN_FROM_PROVIDER_CODE)
    public Mono<ResponseEntity<DataResponse>> getTokenFromAuthorizationProviderCode(
            @Valid @RequestBody ClientLogin clientLogin, ServerWebExchange serverWebExchange) {
        return authService.getToken(clientLogin).map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS_MESSAGE, rs)));
    }

    @GetMapping(value = UrlPaths.Auth.GET_PERMISSION)
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getPermission(
            @RequestParam(name = "clientId") String clientId) {
        return authService
                .getPermission(clientId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @GetMapping(value = UrlPaths.Auth.GET_ORG_PERMISSION)
    public Mono<ResponseEntity<DataResponse<List<Permission>>>> getOrgPermission(
            @RequestParam(name = "clientId", defaultValue = "web-client") String clientId,
            @RequestParam(name = "organizationId", required = false) String organizationId,
            @RequestParam(name = "idNo", required = false) String idNo) {
        return authService
                .getOrgPermission(clientId, idNo, organizationId)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @PostMapping(value = UrlPaths.Auth.REFRESH_TOKEN)
    public Mono<ResponseEntity<DataResponse<Optional<AccessToken>>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService
                .refreshToken(refreshTokenRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @PostMapping(value = UrlPaths.Auth.LOGOUT)
    public Mono<ResponseEntity<DataResponse<Boolean>>> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        return authService.logout(logoutRequest).map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    @PostMapping(value = UrlPaths.Auth.SIGNUP)
    public Mono<DataResponse<UserOtp>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService
                .signUp(signupRequest)
                .map(su -> new DataResponse<>(Translator.toLocaleVi("otp.success"), null));
    }

    //    @PostMapping(value = UrlPaths.Auth.CREATE_ORG_ACCOUNT)
    //    @PreAuthorize("hasAnyAuthority('system')")
    //    public Mono<DataResponse<Individual>> createOrgAccount(@Valid @RequestBody CreateOrgAccount signupRequest) {
    //        return authService.createAccount(signupRequest)
    //                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS_MESSAGE), rs));
    //    }
    //
    //    @GetMapping(value = UrlPaths.Auth.FIND_ORG_ACCOUNT)
    //    @PreAuthorize("hasAnyAuthority('system')")
    //    public Mono<DataResponse<Account>> findByIdNo(@RequestParam(name = "idNo") String idNo, @RequestParam(name =
    // "idType") String idType) {
    //        return userService.findUserByIdNo(idNo, idType)
    //                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS_MESSAGE), rs))
    //                .switchIfEmpty(Mono.just(new DataResponse<>(Translator.toLocale("account.not.exist"), null)));
    //    }

    //    @PostMapping(value = UrlPaths.Auth.ADD_SERVICE_ADMIN_ROLE)
    //    @PreAuthorize("hasAnyAuthority('system')")
    //    public Mono<DataResponse> addServiceAdminRole(@RequestBody AdminPermission adminPermission) {
    //        return authService.addServiceAdminPermission(adminPermission)
    //                .map(rs -> new DataResponse<>(Translator.toLocale("common.success"), rs));
    //    }

    @PostMapping(value = UrlPaths.Auth.FORGOT_PASSWORD)
    public Mono<DataResponse<UserOtp>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService
                .forgotPassword(forgotPasswordRequest)
                .map(su -> new DataResponse<>(Translator.toLocaleVi("otp.success"), null));
    }

    @PostMapping(UrlPaths.Auth.CHANGE_PASSWORD)
    public Mono<DataResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, ServerWebExchange serverWebExchange) {
        return authService
                .changePassword(request, serverWebExchange)
                .thenReturn(new DataResponse<>(Translator.toLocaleVi("success"), null));
    }

    @PostMapping(UrlPaths.Auth.RESET_PASSWORD)
    public Mono<DataResponse<UserOtp>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange) {
        return authService
                .resetPassword(resetPasswordRequest, serverWebExchange)
                .map(success -> new DataResponse<>(
                        Translator.toLocaleVi(DataUtil.safeToString(Translator.toLocaleVi(success.getMessage()))),
                        null));
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
                .then(Mono.fromCallable(
                        () -> ResponseEntity.ok(new DataResponse<>(Translator.toLocaleVi("success"), null))));
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
            GetActionLoginReportRequest request) {
        return authService
                .getActionLoginReport(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>(SUCCESS_MESSAGE, rs)));
    }

    //        @GetMapping(UrlPaths.Auth.BLOCK_LOGIN)
    //        public Mono<ResponseEntity<DataResponse>> blockLoginPartnerLicense(@RequestParam(name = "organizationId",
    //     required = true) String organizationId, @RequestParam(name = "clientCode") String clientCode) {
    //            return authService.blockLoginPartnerLicense(organizationId, clientCode)
    //                    .map(rs -> ResponseEntity.ok(new DataResponse(Translator.toLocaleVi("success"), rs)));
    //        }

    @PostMapping(UrlPaths.Auth.CONFIRM_OTP)
    public Mono<DataResponse> confirmOTP(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService
                .confirmOTP(confirmOTPRequest, serverWebExchange)
                .map(success -> new DataResponse<>(DataUtil.safeToString(success.getMessage()), null));
    }

    @PostMapping(UrlPaths.Auth.GENERATE_OTP)
    public Mono<DataResponse<String>> generateOtp(
            @Valid @RequestBody ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        return authService.generateOtp(confirmOTPRequest, serverWebExchange);
    }

    //    @PostMapping(UrlPaths.Auth.GET_CONTRACT)
    //    @PreAuthorize("hasAnyAuthority('user')")
    //    public Mono<ResponseEntity<DataResponse>> signBusinessAuth(@Valid @RequestBody SignBusinessAuthRequest
    // signBusinessAuthRequest) {
    //        return authService.signBusinessAuth(signBusinessAuthRequest)
    //                .map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS_MESSAGE, rs)));
    //    }
    //
    //    @PostMapping(UrlPaths.Auth.RECEIVE_SIGN_RESULT)
    //    public Mono<String> receiveSignResult(@Valid @RequestBody ReceiveSignDTO request) {
    //        return authService.receiveSignResult(request).map(rs -> rs);
    //    }
    //
    //    @GetMapping(UrlPaths.Auth.VIEW_BUSINESS_AUTH_CONTRACT)
    //    public Mono<ResponseEntity<DataResponse>> viewBusinessAuthContract(@RequestParam(name = "organiztionId")
    // String organiztionId) {
    //        return authService.viewBusinessAuthContract(organiztionId).map(rs -> ResponseEntity.ok(new
    // DataResponse(SUCCESS_MESSAGE, rs)));
    //    }
}
