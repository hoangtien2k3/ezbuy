package com.ezbuy.auth.service.impl;

import com.ezbuy.auth.model.dto.request.ChangePasswordRequest;
import com.ezbuy.auth.model.dto.request.ConfirmOTPRequest;
import com.ezbuy.auth.model.dto.request.CreateAccount;
import com.ezbuy.auth.model.dto.request.CreateNotificationDTO;
import com.ezbuy.auth.model.dto.request.ForgotPasswordRequest;
import com.ezbuy.auth.model.dto.request.GetActionLoginReportRequest;
import com.ezbuy.auth.model.dto.request.LoginRequest;
import com.ezbuy.auth.model.dto.request.LogoutRequest;
import com.ezbuy.auth.model.dto.request.NotiContentDTO;
import com.ezbuy.auth.model.dto.request.ProviderLogin;
import com.ezbuy.auth.model.dto.request.ReceiverDataDTO;
import com.ezbuy.auth.model.dto.request.RefreshTokenRequest;
import com.ezbuy.auth.model.dto.request.ResetPasswordRequest;
import com.ezbuy.auth.model.dto.request.SignupRequest;
import com.ezbuy.auth.model.entity.ActionLogEntity;
import com.ezbuy.auth.model.entity.IndividualEntity;
import com.ezbuy.auth.model.entity.PermissionPolicyEntity;
import com.ezbuy.auth.model.entity.UserCredentialEntity;
import com.ezbuy.auth.model.entity.UserOtpEntity;
import com.ezbuy.auth.repository.ActionLogRepository;
import com.ezbuy.auth.repository.IndOrgPermissionRepo;
import com.ezbuy.auth.repository.IndividualRepository;
import com.ezbuy.auth.repository.OrganizationRepo;
import com.ezbuy.auth.repository.OtpRepository;
import com.ezbuy.auth.repository.UserCredentialRepo;
import com.ezbuy.auth.constants.AuthConstants;
import com.ezbuy.auth.model.dto.AccessToken;
import com.ezbuy.auth.model.dto.KeycloakErrorResponse;
import com.ezbuy.auth.model.dto.response.GetActionLoginReportResponse;
import com.ezbuy.auth.model.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.model.dto.response.Permission;
import com.ezbuy.auth.client.KeyCloakClient;
import com.ezbuy.auth.client.NotiServiceClient;
import com.ezbuy.auth.config.KeycloakProvider;
import com.ezbuy.auth.service.AuthService;
import com.ezbuy.core.config.CipherManager;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.constants.Regex;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.*;
import jakarta.ws.rs.core.Response;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.DecisionEffect;
import org.keycloak.representations.idm.authorization.PolicyEvaluationRequest;
import org.keycloak.representations.idm.authorization.PolicyEvaluationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ObjectMapperUtil objectMapperUtil;
    private final KeyCloakClient keyCloakClient;
    private final KeycloakProvider kcProvider;
    private final NotiServiceClient notiServiceClient;
    private final OtpRepository otpRepository;
    private final IndividualRepository individualRepository;
    private final IndOrgPermissionRepo indOrgPermissionRepo;
    private final OrganizationRepo organizationRepo;
    private final UserCredentialRepo userCredentialRepo;
    private final CipherManager cipherManager;
    private final ActionLogRepository actionLogRepository;

    @Value("${hashing-password.public-key}")
    private String publicKey;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Override
    public Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest) {
        return keyCloakClient
                .getToken(loginRequest)
                .onErrorResume(WebClientResponseException.class, this::handleKeyCloakError);
    }

    @Override
    public Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin) {
        return keyCloakClient
                .getToken(providerLogin)
                .onErrorResume(WebClientResponseException.class, this::handleKeyCloakError);
    }

    @Override
    public Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return keyCloakClient
                .refreshToken(refreshTokenRequest)
                .onErrorResume(WebClientResponseException.class, this::handleKeyCloakError);
    }

    @Override
    public Mono<Boolean> logout(LogoutRequest logoutRequest) {
        return keyCloakClient.logout(logoutRequest);
    }

    @Override
    public Mono<List<Permission>> getPermission(String clientId) {
        return SecurityUtils
                .getTokenUser()
                .flatMap(userToken -> keyCloakClient.getPermissions(clientId, userToken));
    }

    @Override
    public Mono<List<Permission>> getOrgPermission(String clientId, String idNo, String orgId) {
        return SecurityUtils.getCurrentUser().flatMap(currentUser -> {
            if (DataUtil.isNullOrEmpty(idNo) && DataUtil.isNullOrEmpty(orgId)) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "org.define.not.exist"));
            }
            if (DataUtil.isNullOrEmpty(orgId) && !DataUtil.isNullOrEmpty(idNo)) {
                return organizationRepo
                        .findOrganizationByIdentify(AuthConstants.TenantType.ORGANIZATION, idNo)
                        .flatMap(orgIdDb -> getPermission(clientId, orgIdDb, currentUser.getId()))
                        .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "id.no.not.existed")));
            } else {
                return getPermission(clientId, orgId, currentUser.getId());
            }
        });
    }

    @Override
    public Mono<List<Permission>> getPermission(String clientId, String orgId, String userId) {
        return indOrgPermissionRepo.getOrgIds(userId).flatMap(orgNumber -> {
            log.debug("Organization number for user {}: {}", userId, orgNumber);
            if (orgNumber < 2) {
                return getPermission(clientId);
            }
            return kcProvider
                    .getClient(clientId)
                    .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "client.not.existed")))
                    .flatMap(client -> {
                        PolicyEvaluationRequest policyEvaluationRequest = new PolicyEvaluationRequest();
                        policyEvaluationRequest.setUserId(userId);
                        policyEvaluationRequest.setEntitlements(false);
                        Mono<PolicyEvaluationResponse> allPermissionsMono = Mono.fromCallable(() -> kcProvider
                                        .getRealmResource()
                                        .clients()
                                        .get(client.getId())
                                        .authorization()
                                        .policies()
                                        .evaluate(policyEvaluationRequest))
                                .subscribeOn(Schedulers.boundedElastic());
                        Mono<List<PermissionPolicyEntity>> orgPoliciesMono = indOrgPermissionRepo
                                .getAllByUserId(orgId, userId)
                                .collectList();
                        return Mono.zip(allPermissionsMono, orgPoliciesMono)
                                .map(tuple -> findOrgPermissions(tuple.getT1(), tuple.getT2()))
                                .defaultIfEmpty(Collections.emptyList())
                                .doOnError(err -> log.error("Error getting permissions for user {} and client {}: {}",
                                        userId,
                                        clientId,
                                        err.getMessage())
                                );
                    });
        });
    }

    private List<Permission> findOrgPermissions(
            PolicyEvaluationResponse allPermissions, List<PermissionPolicyEntity> orgPermissionPolicies) {
        if (allPermissions.getResults() == null) {
            return Collections.emptyList();
        }
        Set<String> orgPolicyIdSet = orgPermissionPolicies.stream()
                .map(PermissionPolicyEntity::getPolicyId)
                .collect(Collectors.toSet());
        return allPermissions.getResults().stream()
                .filter(pe -> DecisionEffect.PERMIT.equals(pe.getStatus())
                        && pe.getPolicies() != null
                        && !pe.getPolicies().isEmpty())
                .flatMap(pe -> pe.getPolicies().stream()
                        .filter(policy -> isOrgPermit(policy.getAssociatedPolicies(), orgPolicyIdSet))
                        .map(policy -> {
                            var resource = pe.getResource();
                            return new Permission(resource.getId(), resource.getName());
                        })
                        .limit(1))
                .collect(Collectors.toList());
    }

    private boolean isOrgPermit(
            List<PolicyEvaluationResponse.PolicyResultRepresentation> associatedPolicies, Set<String> policyIds) {
        return associatedPolicies.stream()
                .anyMatch(policy -> policyIds.contains(policy.getPolicy().getId())
                        && DecisionEffect.PERMIT.equals(policy.getStatus()));
    }

    @Override
    @Transactional
    public Mono<UserOtpEntity> signUp(SignupRequest signupRequest) {
        String requestEmail = DataUtil.safeTrim(signupRequest.getEmail());
        if (!ValidateUtils.validateRegex(requestEmail, Regex.EMAIL_REGEX)) {
            return Mono.error(
                    new BusinessException(ErrorCode.INVALID_PARAMS, AuthConstants.Message.EMAIL_INVALID));
        }
        if (isExistedEmail(requestEmail)) {
            return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "signup.email.exist"));
        }
        String otpValue = generateOtpValue();
        CreateNotificationDTO createNotificationDTO = createNotificationDTO(
                otpValue,
                Translator.toLocale("email.title.signup"),
                AuthConstants.TemplateMail.SIGN_UP,
                ReceiverDataDTO.builder().email(requestEmail).build(),
                null);
        return otpRepository.currentTimeDB().flatMap(localDateTime -> {
            UserOtpEntity otp = UserOtpEntity.builder()
                    .id(String.valueOf(UUID.randomUUID()))
                    .otp(otpValue)
                    .createBy(AuthConstants.RoleName.SYSTEM)
                    .updateBy(AuthConstants.RoleName.SYSTEM)
                    .email(requestEmail)
                    .tries(0)
                    .expTime(localDateTime.plusMinutes(AuthConstants.Otp.EXP_MINUTE))
                    .type(AuthConstants.Otp.REGISTER)
                    .build();
            AppUtils.runHiddenStream(
                    otpRepository.disableOtp(requestEmail, AuthConstants.Otp.REGISTER, AuthConstants.RoleName.SYSTEM));
            AppUtils.runHiddenStream(generateOtpAndSave(otp));
            return insertTransmission(createNotificationDTO, otp);
        });
    }

    private CreateNotificationDTO createNotificationDTO(
            String subTitle, String title, String template, ReceiverDataDTO data, String externalData) {
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
        createNotificationDTO.setSender(AuthConstants.RoleName.SYSTEM);
        createNotificationDTO.setSeverity(AuthConstants.Notification.SEVERITY);
        createNotificationDTO.setTemplateMail(template);
        createNotificationDTO.setNotiContentDTO(NotiContentDTO.builder()
                .title(title)
                .subTitle(subTitle)
                .externalData(externalData)
                .build());
        createNotificationDTO.setContentType(AuthConstants.Notification.CONTENT_TYPE);
        createNotificationDTO.setCategoryType(AuthConstants.Notification.CATEGORY_TYPE);
        createNotificationDTO.setChannelType(AuthConstants.Notification.CHANNEL_TYPE);
        createNotificationDTO.setReceiverList(List.of(data));
        return createNotificationDTO;
    }

    @Override
    public Mono<UserOtpEntity> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String userName = DataUtil.safeTrim(forgotPasswordRequest.getUsername());
        if (DataUtil.isNullOrEmpty(userName)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "individual.not.found"));
        }
        if (!isExistedUsername(userName)) {
            return Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "forgot.pass.email"));
        }
        String otpValue = generateOtpValue();
        List<UserRepresentation> listUser = kcProvider.getRealmResource().users().search(userName, true);
        UserRepresentation user = listUser.getFirst();
        String requestEmail = user.getEmail();
        CreateNotificationDTO createNotificationDTO = createNotificationDTO(
                otpValue,
                Translator.toLocaleVi("email.title.forgot.password"),
                AuthConstants.TemplateMail.FORGOT_PASSWORD,
                ReceiverDataDTO.builder()
                        .userId(user.getId())
                        .email(requestEmail)
                        .build(),
                null);
        return otpRepository
                .currentTimeDB()
                .flatMap(time -> {
                    UserOtpEntity otpBuild = UserOtpEntity.builder()
                            .id(String.valueOf(UUID.randomUUID()))
                            .otp(otpValue)
                            .createBy(AuthConstants.RoleName.SYSTEM)
                            .updateBy(AuthConstants.RoleName.SYSTEM)
                            .tries(0)
                            .status(Constants.Activation.ACTIVE)
                            .email(requestEmail)
                            .expTime(time.plusMinutes(AuthConstants.Otp.EXP_MINUTE))
                            .type(AuthConstants.Otp.FORGOT_PASSWORD)
                            .build();
                    AppUtils.runHiddenStream(otpRepository.disableOtp(
                            requestEmail, AuthConstants.Otp.FORGOT_PASSWORD, AuthConstants.RoleName.SYSTEM));
                    AppUtils.runHiddenStream(generateOtpAndSave(otpBuild));
                    return insertTransmission(createNotificationDTO, otpBuild);
                })
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
    }

    private Mono<UserOtpEntity> insertTransmission(CreateNotificationDTO createNotificationDTO, UserOtpEntity userOtp) {
        return notiServiceClient
                .insertTransmission(createNotificationDTO)
                .flatMap(objects -> {
                    if (objects.isPresent()
                            && com.ezbuy.auth.constants.ErrorCode.ResponseErrorCode.ERROR_CODE_SUCCESS.equals(
                                    objects.get().getErrorCode())
                            && !DataUtil.isNullOrEmpty(objects.get().getMessage())) {
                        return Mono.just(userOtp);
                    }
                    return Mono.error(new BusinessException(
                            ErrorCode.INVALID_PARAMS,
                            (objects.isPresent()) ? objects.get().getMessage() : "params.invalid"));
                })
                .onErrorResume(throwable -> Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
    }

    private Mono<UserOtpEntity> generateOtpAndSave(UserOtpEntity otp) {
        assert otp.getId() != null;
        return otpRepository
                .findById(otp.getId())
                .flatMap(otpRepository::save)
                .switchIfEmpty(otpRepository.save(otp.setAsNew()));
    }

    private boolean isExistedEmail(String email) {
        return !kcProvider.getRealmResource().users().searchByEmail(email, true).isEmpty();
    }

    private boolean isExistedUsername(String username) {
        return !kcProvider.getRealmResource().users().search(username, true).isEmpty();
    }

    private String createUserKeycloak(String username, String password, String email) {
        // create new keycloak user
        UsersResource usersResource = kcProvider.getRealmResource().users();
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(username);
        kcUser.setEmail(email);
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        Response response = usersResource.create(kcUser);
        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = usersResource.get(userId);
        // password settings
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        userResource.resetPassword(passwordCred);
        // get realm roles
        RoleRepresentation userRealmRole = kcProvider
                .getRealmResource()
                .roles()
                .get(AuthConstants.RoleName.USER)
                .toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(userRealmRole));
        // get and add role 'user' for ezbuy-client
        String clientIdOfEzbuy = kcProvider
                .getRealmResource()
                .clients()
                .findByClientId(clientId)
                .getFirst()
                .getId();
        RoleRepresentation adminRoleWebclient = kcProvider
                .getRealmResource()
                .clients()
                .get(clientIdOfEzbuy)
                .roles()
                .get(AuthConstants.RoleName.USER)
                .toRepresentation();
        userResource.roles().clientLevel(clientIdOfEzbuy).add(Collections.singletonList(adminRoleWebclient));
        return userId;
    }

    @Override
    public Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange) {
        String requestEmail = DataUtil.safeTrim(resetPasswordRequest.getEmail());
        String requestOtp = DataUtil.safeTrim(resetPasswordRequest.getOtp());
        if (!Regex.OTP_REGEX.matches(requestOtp)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dto.otp.invalid"));
        }
        if (!Regex.PASSWORD_REGEX.matches(resetPasswordRequest.getPassword())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        }
        if (!Regex.UTF8_REGEX.matches(resetPasswordRequest.getPassword())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        }

        List<UserRepresentation> listUser =
                kcProvider.getRealmResource().users().search(requestEmail, true);
        if (DataUtil.isNullOrEmpty(listUser)) {
            return Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.not.found"));
        }
        UserRepresentation user = listUser.getFirst();
        return otpRepository
                .confirmOtp(user.getEmail(), AuthConstants.Otp.FORGOT_PASSWORD, requestOtp, 1)
                .flatMap(result -> {
                    if (Boolean.FALSE.equals(result)) {
                        return Mono.error(new BusinessException(com.ezbuy.auth.constants.ErrorCode.OtpErrorCode.OTP_NOT_MATCH, "otp.not.match"));
                    }
                    UsersResource usersResource = kcProvider.getRealmResource().users();
                    UserResource userResource = usersResource.get(user.getId());
                    CredentialRepresentation passwordCred = new CredentialRepresentation();
                    passwordCred.setTemporary(false);
                    passwordCred.setType(CredentialRepresentation.PASSWORD);
                    passwordCred.setValue(resetPasswordRequest.getPassword());
                    userResource.resetPassword(passwordCred);

                    var saveUserCredential = userCredentialRepo
                            .getUserCredentialByUserName(requestEmail, Constants.STATUS.ACTIVE)
                            .collectList()
                            .flatMap(userCredentials -> {
                                String hashedPasswordHubSme =
                                        cipherManager.encrypt(resetPasswordRequest.getPassword(), publicKey);
                                UserCredentialEntity userCredential = new UserCredentialEntity();
                                if (userCredentials.isEmpty()) {
                                    userCredential.setId(String.valueOf(UUID.randomUUID()));
                                    userCredential.setUserId(user.getId());
                                    userCredential.setUsername(requestEmail);
                                    userCredential.setHashPwd(hashedPasswordHubSme);
                                    userCredential.setStatus(Constants.STATUS.ACTIVE);
                                    userCredential.setPwdChanged(AuthConstants.STATUS_ACTIVE);
                                    userCredential.setUpdateBy(requestEmail);
                                    userCredential.setCreateBy(requestEmail);
                                    userCredential.setNew(true);
                                } else {
                                    userCredential = userCredentials.getFirst();
                                    userCredential.setHashPwd(hashedPasswordHubSme);
                                    userCredential.setPwdChanged(AuthConstants.STATUS_ACTIVE);
                                    userCredential.setUpdateBy(requestEmail);
                                    userCredential.setNew(false);
                                }
                                return userCredentialRepo.save(userCredential);
                            });
                    var saveDisableOtp = AppUtils.insertData(otpRepository.disableOtp(
                            user.getEmail(), AuthConstants.Otp.FORGOT_PASSWORD, AuthConstants.RoleName.SYSTEM));
                    return Mono.zip(saveUserCredential, saveDisableOtp)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    ErrorCode.INTERNAL_SERVER_ERROR, "update.user-credential-or-user-otp.error")))
                            .flatMap(tuple -> {
                                AppUtils.runHiddenStream(saveLog(
                                        user.getId(),
                                        requestEmail,
                                        AuthConstants.FORGOT_PASSWORD,
                                        serverWebExchange.getRequest()));
                                return Mono.just(new DataResponse<>("reset.password.success", null));
                            });
                })
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "otp.not.found")));
    }

    @Override
    public Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange) {
        UsersResource usersResource = kcProvider.getRealmResource().users();
        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(tokenUser.getUsername());
                    loginRequest.setPassword(request.getOldPassword());
                    // update individual
                    Mono<IndividualEntity> updateIndividual = individualRepository
                            .findByUsername(tokenUser.getUsername())
                            .flatMap(individual -> {
                                individual.setPasswordChange(true);
                                individual.setNew(false);
                                return individualRepository.save(individual);
                            })
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    ErrorCode.NOT_FOUND, Translator.toLocaleVi("individual.not.exits"))));
                    // update user credential
                    Mono<UserCredentialEntity> updateCredential = userCredentialRepo
                            .getUserCredentialByUserName(tokenUser.getUsername(), Constants.STATUS.ACTIVE)
                            .collectList()
                            .flatMap(userCredentials -> {
                                UserCredentialEntity userCredential = new UserCredentialEntity();
                                String hashedPassword = cipherManager.encrypt(request.getNewPassword(), publicKey);
                                if (DataUtil.isNullOrEmpty(userCredentials)) {
                                    userCredential.setId(String.valueOf(UUID.randomUUID()));
                                    userCredential.setUserId(tokenUser.getId());
                                    userCredential.setUsername(tokenUser.getUsername());
                                    userCredential.setHashPwd(hashedPassword);
                                    userCredential.setStatus(Constants.STATUS.ACTIVE);
                                    userCredential.setPwdChanged(AuthConstants.STATUS_ACTIVE);
                                    userCredential.setUpdateBy(tokenUser.getUsername());
                                    userCredential.setCreateBy(tokenUser.getUsername());
                                    userCredential.setNew(true);
                                } else {
                                    userCredential = userCredentials.getFirst();
                                    userCredential.setHashPwd(hashedPassword);
                                    userCredential.setPwdChanged(AuthConstants.STATUS_ACTIVE);
                                    userCredential.setUpdateBy(tokenUser.getUsername());
                                    userCredential.setNew(false);
                                }
                                return userCredentialRepo.save(userCredential);
                            });
                    // get token info login
                    Mono<TokenUser> getTokenInfo = keyCloakClient
                            .getToken(loginRequest)
                            .thenReturn(tokenUser)
                            .onErrorMap(throwable -> new BusinessException(
                                    ErrorCode.BAD_REQUEST,
                                    Translator.toLocaleVi("change-pass.old-password.invalid")));
                    // zip mono
                    return Mono.zip(updateIndividual, getTokenInfo, updateCredential)
                            .map(Tuple2::getT2);
                })
                .flatMap(tokenUser -> {
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(OAuth2ParameterNames.PASSWORD);
                    credentialRepresentation.setValue(request.getNewPassword());
                    credentialRepresentation.setTemporary(false);
                    return Mono.fromRunnable(() -> {
                        usersResource.get(tokenUser.getId()).resetPassword(credentialRepresentation);
                        AppUtils.runHiddenStream(saveLog(
                                tokenUser.getId(),
                                tokenUser.getUsername(),
                                AuthConstants.CHANGE_PASSWORD,
                                serverWebExchange.getRequest())
                        );
                    });
                });
    }

    @Override
    @Transactional
    public Mono<IndividualEntity> confirmOtpAndCreateUser(CreateAccount createAccount) {
        String otp = createAccount.getOtp();
        String email = createAccount.getEmail();
        String password = createAccount.getPassword();
        String system = createAccount.getSystem();
        if (!ValidateUtils.validateRegex(email, Regex.EMAIL_REGEX)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dto.email.invalid"));
        }
        if (!password.matches(Regex.PASSWORD_REGEX)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        }
        return otpRepository
                .confirmOtp(email, AuthConstants.Otp.REGISTER, otp, 1)
                .flatMap(isConfirmOtp -> {
                    if (!isConfirmOtp) {
                        return Mono.error(new BusinessException(com.ezbuy.auth.constants.ErrorCode.OtpErrorCode.OTP_NOT_MATCH, "otp.not.match"));
                    }
                    if (isExistedEmail(email)) {
                        return Mono.error(new BusinessException(com.ezbuy.auth.constants.ErrorCode.AuthErrorCode.USER_EXISTED, "signup.email.exist"));
                    }
                    return createUserInKeyCloak(email, password, email, system);
                });
    }

    private Mono<IndividualEntity> createUserInKeyCloak(String username, String password, String email, String system) {
        String userId = createUserKeycloak(username, password, email);
        String hashedPassword = cipherManager.encrypt(password, publicKey);
        int pwdChanged = AuthConstants.System.EZBUY.equals(system) ? AuthConstants.STATUS_ACTIVE : AuthConstants.STATUS_INACTIVE;
        UserCredentialEntity userCredential = UserCredentialEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .userId(userId)
                .username(username)
                .createBy(username)
                .updateBy(username)
                .pwdChanged(pwdChanged)
                .hashPwd(hashedPassword)
                .isNew(true)
                .status(Constants.STATUS.ACTIVE)
                .build();
        IndividualEntity individual = IndividualEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .userId(userId)
                .username(username)
                .email(username)
                .createBy(username)
                .updateBy(username)
                .status(Constants.Activation.ACTIVE)
                .passwordChange(false)
                .isNew(true)
                .build();
        var saveIndividual = individualRepository.save(individual);
        var saveUserCredential = userCredentialRepo.save(userCredential);
        return Mono.zip(saveIndividual, saveUserCredential)
                .flatMap(rs -> otpRepository
                        .disableOtp(username, AuthConstants.Otp.REGISTER, AuthConstants.RoleName.SYSTEM)
                        .doOnError(err -> log.error("Disable OTP failed ", err))
                        .thenReturn(rs.getT1()))
                .onErrorResume(error -> {
                    kcProvider.getRealmResource().users().delete(userId);
                    return Mono.error(error);
                });
    }

    @Override
    public Mono<List<String>> getAllUserId() {
        List<String> list = new ArrayList<>();
        Set<UserRepresentation> set = kcProvider
                .getRealmResource()
                .roles()
                .get(AuthConstants.RoleName.USER)
                .getRoleUserMembers();
        for (UserRepresentation u : set) {
            if (Boolean.TRUE.equals(u.isEnabled())) {
                list.add(u.getId());
            }
        }
        return Mono.just(list);
    }

    @Override
    public Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String username) {
        if (DataUtil.isNullOrEmpty(username)) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "username.required"));
        }
        return userCredentialRepo
                .getUserCredentialByUserName(username, Constants.STATUS.ACTIVE)
                .collectList()
                .flatMap(rs -> {
                    if (rs.isEmpty()) {
                        return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "user.not.found"));
                    }
                    return Mono.just(GetTwoWayPasswordResponse.builder()
                            .password(rs.getFirst().getHashPwd())
                            .build());
                });
    }

    private Mono<Boolean> saveLog(String userId, String username, String type, ServerHttpRequest request) {
        ActionLogEntity log = new ActionLogEntity();
        log.setId(UUID.randomUUID().toString());
        log.setUserId(userId);
        log.setUsername(username);
        log.setType(type);
        log.setCreateAt(LocalDateTime.now());
        log.setIp(RequestUtils.getIpAddress(request));
        return actionLogRepository.save(log).map(rs -> true);
    }

    @Override
    public Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request) {
        if (DataUtil.isNullOrEmpty(request.getDateReport())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "dateReport.not.empty"));
        }
        return actionLogRepository
                .countLoginInOneDay(request.getDateReport(), AuthConstants.LOGIN)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PARAMS, "action-log.login.not.found")))
                .flatMap(rs -> Mono.just(GetActionLoginReportResponse.builder().loginCount(rs).build()));
    }

    @Override
    public Mono<DataResponse<Map<String, String>>> confirmOTP(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        String otp = DataUtil.safeTrim(confirmOTPRequest.getOtp());
        if (DataUtil.isNullOrEmpty(otp)) {
            return Mono.error(new BusinessException(com.ezbuy.auth.constants.ErrorCode.OtpErrorCode.OTP_EMPTY, "dto.otp.not.empty"));
        }
        String email = DataUtil.safeTrim(confirmOTPRequest.getEmail());
        String type = DataUtil.safeTrim(confirmOTPRequest.getType());
        return otpRepository.confirmOtp(email, type, otp, 1).flatMap(result -> {
            if (Boolean.FALSE.equals(result)) {
                return Mono.error(new BusinessException(com.ezbuy.auth.constants.ErrorCode.OtpErrorCode.OTP_NOT_MATCH, "otp.not.match"));
            }
            return Mono.just(new DataResponse<>("success", Map.of("message", "otp.success.match")));
        });
    }

    @Override
    public Mono<DataResponse<String>> generateOtp(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        String email = DataUtil.safeTrim(confirmOTPRequest.getEmail());
        String type = DataUtil.safeTrim(confirmOTPRequest.getType());
        String otpValue = generateOtpValue();
        return otpRepository.currentTimeDB().flatMap(localDateTime -> {
            UUID id = UUID.randomUUID();
            UserOtpEntity otp = UserOtpEntity.builder()
                    .id(id.toString())
                    .type(type)
                    .email(email)
                    .otp(otpValue)
                    .expTime(localDateTime.plusMinutes(AuthConstants.Otp.EXP_OTP_AM_MINUTE))
                    .tries(0)
                    .status(1)
                    .createBy(AuthConstants.RoleName.SYSTEM)
                    .updateBy(AuthConstants.RoleName.SYSTEM)
                    .newOtp(true)
                    .build();
            var disableOtpMono = AppUtils.insertData(otpRepository.disableOtp(email, type, AuthConstants.RoleName.SYSTEM));
            var saveOtpMono = AppUtils.insertData(otpRepository.save(otp));
            return Mono.zip(disableOtpMono, saveOtpMono)
                    .map(tuple -> new DataResponse<>("success", otpValue))
                    .onErrorResume(throwable -> Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, throwable.getMessage())));
        });
    }

    // generate otp value random
    private String generateOtpValue() {
        Random random = new SecureRandom();
        return new DecimalFormat("000000").format(random.nextInt(999999));
    }

    private Mono<Optional<AccessToken>> handleKeyCloakError(WebClientResponseException err) {
        return Mono.error(mapKeycloakError(err));
    }

    private BusinessException mapKeycloakError(WebClientResponseException err) {
        String body = err.getResponseBodyAsString();
        KeycloakErrorResponse errorResponse = objectMapperUtil.convertStringToObject(body, KeycloakErrorResponse.class);
        if (Objects.isNull(errorResponse)) {
            return new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "token.error");
        }
        String errorCode = errorResponse.getError();
        String errorDescription = errorResponse.getErrorDescription();
        if (Constants.KeyCloakError.INVALID_GRANT.equalsIgnoreCase(errorCode)) {
            String upperCaseDescription = Objects.isNull(errorDescription) ? "" : errorDescription.toUpperCase();
            if (Constants.KeyCloakError.DISABLED.contains(upperCaseDescription)) {
                return new BusinessException(com.ezbuy.auth.constants.ErrorCode.AuthErrorCode.USER_DISABLED, "user.disabled");
            }
            if (Constants.KeyCloakError.INVALID.contains(upperCaseDescription)) {
                return new BusinessException(com.ezbuy.auth.constants.ErrorCode.AuthErrorCode.INVALID, "user.invalid");
            }
        }
        return new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, errorDescription);
    }

}
