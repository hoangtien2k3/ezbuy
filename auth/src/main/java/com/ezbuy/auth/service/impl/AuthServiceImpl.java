package com.ezbuy.auth.service.impl;

import static com.ezbuy.auth.constants.AuthConstants.ClientName.*;
import static com.ezbuy.auth.constants.AuthConstants.SUCCESS;
import static com.ezbuy.framework.constants.Constants.ActionUser.SYSTEM;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ezbuy.auth.client.impl.KeyCloakClientImpl;
import jakarta.ws.rs.core.Response;

import org.apache.mina.util.Base64;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
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

import com.ezbuy.auth.client.KeyCloakClient;
import com.ezbuy.auth.client.NotiServiceClient;
import com.ezbuy.auth.config.KeycloakProvider;
import com.ezbuy.auth.constants.ActionLogType;
import com.ezbuy.auth.constants.AuthConstants;
import com.ezbuy.auth.constants.ErrorCode;
import com.ezbuy.auth.model.dto.AccessToken;
import com.ezbuy.auth.model.dto.KeycloakErrorResponse;
import com.ezbuy.auth.model.dto.NotiContentDTO;
import com.ezbuy.auth.model.dto.ReceiverDataDTO;
import com.ezbuy.auth.model.dto.request.*;
import com.ezbuy.auth.model.dto.response.GetActionLoginReportResponse;
import com.ezbuy.auth.model.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.auth.model.dto.response.IndividualDTO;
import com.ezbuy.auth.model.dto.response.Permission;
import com.ezbuy.auth.model.postgresql.*;
import com.ezbuy.auth.model.postgresql.TenantIdentify;
import com.ezbuy.auth.repository.ActionLogRepository;
import com.ezbuy.auth.repository.IndOrgPermissionRepo;
import com.ezbuy.auth.repository.IndividualRepository;
import com.ezbuy.auth.repository.OrganizationRepo;
import com.ezbuy.auth.repository.OtpRepository;
import com.ezbuy.auth.repository.UserCredentialRepo;
import com.ezbuy.auth.service.AuthService;
import com.ezbuy.auth.service.IdentifyService;
import com.ezbuy.framework.config.CipherManager;
import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.constants.Constants;
import com.ezbuy.framework.constants.Regex;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.UserDTO;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ObjectMapperUtil objectMapperUtil;
    // keycloak client
    private final KeyCloakClient keyCloakClient;
    private final KeycloakProvider kcProvider;
    // notification service client
    private final NotiServiceClient notiServiceClient;

    // repositories
    private final OtpRepository otpRepository;
    private final IndividualRepository individualRepository;
    private final IndOrgPermissionRepo indOrgPermissionRepo;
    private final OrganizationRepo organizationRepo;
    private final IdentifyService identifyService;
    private final UserCredentialRepo userCredentialRepo;
    //    private final OrganizationService organizationService;

    private final CipherManager cipherManager;
    private final ActionLogRepository actionLogRepository;

    private final KeycloakProvider keycloakProvider;
    private final KeyCloakClientImpl keyCloakClientImpl;

    @Value("${hashing-password.public-key}")
    private String publicKey;

    @Override
    public Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest) {
        return keyCloakClient
                .getToken(loginRequest)
                .onErrorResume(WebClientResponseException.class, this::handleKeyCloakError);
    }

    //    @Override
    //    public Mono<Optional<AccessToken>> getToken(ClientLogin clientLogin, ServerWebExchange serverWebExchange) {
    //        if (DataUtil.isNullOrEmpty(clientLogin.getOrganizationId())) {
    //            return getTokenWithClientLogin(clientLogin, serverWebExchange);
    //        } else {
    //            return blockLoginPartnerLicense(clientLogin.getOrganizationId(), clientLogin.getClientId())
    //                    .flatMap(hasValidData -> {
    //                        if (hasValidData) {
    //                            return getTokenWithClientLogin(clientLogin, serverWebExchange);
    //                        } else {
    //                            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
    //                                    "invalid.param"));
    //                        }
    //                    })
    //                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
    //                            "can.not.block")));
    //        }
    //    }

    private Mono<Optional<AccessToken>> getTokenWithClientLogin(
            ClientLogin clientLogin, ServerWebExchange serverWebExchange) {
        return keyCloakClient
                .getToken(clientLogin)
                .flatMap(accessToken -> {
                    if (HUB_SME.equals(clientLogin.getClientId()) && accessToken.isPresent()) {
                        // get user info
                        UserDTO userDTO = SecurityUtils.getUserByAccessToken(
                                accessToken.get().getToken());
                        if (userDTO != null) {
                            AppUtils.runHiddenStream(saveLog(
                                    userDTO.getId(),
                                    userDTO.getUsername(),
                                    ActionLogType.LOGIN,
                                    serverWebExchange.getRequest()));
                        }
                    }
                    return Mono.just(accessToken);
                })
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
        return SecurityUtils.getTokenUser().flatMap(userToken -> keyCloakClient.getPermissions(clientId, userToken));
    }

    @Override
    public Mono<List<Permission>> getOrgPermission(String clientId, String idNo, String orgId) {
        return SecurityUtils.getCurrentUser().flatMap(currentUser -> {
            if (DataUtil.isNullOrEmpty(idNo) && DataUtil.isNullOrEmpty(orgId)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "org.define.not.exist"));
            }
            if (DataUtil.isNullOrEmpty(orgId) && !DataUtil.isNullOrEmpty(idNo)) {
                return organizationRepo
                        .findOrganizationByIdentify(AuthConstants.TenantType.ORGANIZATION, idNo)
                        .flatMap(orgIdDb -> getPermission(clientId, orgIdDb, currentUser.getId()))
                        .switchIfEmpty(
                                Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.not.existed")));
            } else {
                return getPermission(clientId, orgId, currentUser.getId());
            }
        });
    }

    @Override
    public Mono<List<Permission>> getPermission(String clientId, String orgId, String userId) {
        PolicyEvaluationRequest policyEvaluationRequest = new PolicyEvaluationRequest();
        policyEvaluationRequest.setUserId(userId);
        policyEvaluationRequest.setEntitlements(false);
        return indOrgPermissionRepo.getOrgIds(userId).flatMap(orgNumber -> {
            log.info("org number {}", orgNumber);

            // orgNumber = 1 => 1 doanh nghiệp
            // orgNumber = 0 => owner đấu nối từ mbccs
            if (orgNumber < 2) {
                return getPermission(clientId);
            }
            return kcProvider
                    .getClient(clientId)
                    .flatMap(client -> {
                        var allPermissions = kcProvider
                                .getRealmResource()
                                .clients()
                                .get(client.getId())
                                .authorization()
                                .policies()
                                .evaluate(policyEvaluationRequest);
                        var getOrgPolicies = indOrgPermissionRepo.getAllByUserId(orgId, userId);
                        return getOrgPolicies
                                .collectList()
                                .flatMap(orgPolicies -> Mono.just(findOrgPermissions(allPermissions, orgPolicies)))
                                .switchIfEmpty(Mono.just(new ArrayList<>()))
                                .doOnError(err -> log.error("Get permissions error {}", err));
                    })
                    .switchIfEmpty(
                            Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "client.not.existed")));
        });
    }

    private List<Permission> findOrgPermissions(
            PolicyEvaluationResponse allPermissions, List<PermissionPolicy> orgPermissionPolicies) {
        List<Permission> permitPermissions = new ArrayList<>();
        var results = allPermissions.getResults();
        if (results == null) {
            return permitPermissions;
        }
        var orgPolicyIdSet = orgPermissionPolicies.stream()
                .map(PermissionPolicy::getPolicyId)
                .collect(Collectors.toCollection(HashSet::new));
        for (PolicyEvaluationResponse.EvaluationResultRepresentation pe : results) {
            var policies = pe.getPolicies();
            if (!pe.getStatus().equals(DecisionEffect.PERMIT) || policies == null || policies.isEmpty()) {
                continue;
            }
            for (PolicyEvaluationResponse.PolicyResultRepresentation policy : policies) {
                if (isOrgPermit(policy.getAssociatedPolicies(), orgPolicyIdSet)) {
                    var resource = pe.getResource();
                    Permission permission = new Permission();
                    permission.setRsName(resource.getName());
                    permission.setRsId(resource.getId());
                    permitPermissions.add(permission);
                    break;
                }
            }
        }
        return permitPermissions;
    }

    private boolean isOrgPermit(
            List<PolicyEvaluationResponse.PolicyResultRepresentation> associatedPolicies, Set<String> policyIds) {
        for (PolicyEvaluationResponse.PolicyResultRepresentation policy : associatedPolicies) {
            if (policyIds.contains(policy.getPolicy().getId()) && DecisionEffect.PERMIT.equals(policy.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private Mono handleKeyCloakError(WebClientResponseException err) {
        String bodyResponse = err.getResponseBodyAsString();
        KeycloakErrorResponse errorResponse =
                objectMapperUtil.convertStringToObject(bodyResponse, KeycloakErrorResponse.class);
        if (errorResponse == null) {
            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "token.error"));
        }
        String errorCode = errorResponse.getError();
        String errorDescription = errorResponse.getErrorDescription();
        if (!DataUtil.isNullOrEmpty(errorCode) && errorCode.equalsIgnoreCase(Constants.KeyCloakError.INVALID_GRANT)) {
            if (errorDescription != null && errorDescription.toUpperCase().contains(Constants.KeyCloakError.DISABLED)) {
                return Mono.error(new BusinessException(ErrorCode.AuthErrorCode.USER_DISABLED, "user.disabled"));
            } else if (errorDescription != null
                    && errorDescription.toUpperCase().contains(Constants.KeyCloakError.INVALID)) {
                return Mono.error(new BusinessException(ErrorCode.AuthErrorCode.INVALID, "user.invalid"));
            }
        }
        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorDescription));
    }

    //    @Override
    //    @Transactional
    //    public Mono<Individual> createAccount(CreateOrgAccount createOrgAccount) {
    //        // check account exit by using idNo
    //        if (!ValidateUtils.validateRegex(createOrgAccount.getEmail(), Regex.EMAIL_REGEX)) {
    //            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
    //                    AuthConstants.Message.EMAIL_INVALID));
    //        }
    //        if (!ValidateUtils.validateRegex(createOrgAccount.getPhone(), Regex.PHONE_REGEX)) {
    //            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
    //                    "dto.phone.number.invalid"));
    //        }
    //        validateRepresentative(createOrgAccount.getRepresentative());
    //        // validate identifies of org
    //        var standardizedOrgIdentifies = settingIdentifies(createOrgAccount.getIdentifies());
    //        createOrgAccount.setIdentifies(standardizedOrgIdentifies);
    //        // get primary identify
    //        String primaryIdentify = getPrimaryIdNo(standardizedOrgIdentifies);
    //        // check exist identify was trusted
    //        return identifyService.existedTrustedOrgIdentify(standardizedOrgIdentifies)
    //                .flatMap(check -> {
    //                    if (Boolean.TRUE.equals(check)) {
    //                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
    //                                "identify.trusted"));
    //                    }
    //                    return SecurityUtils.getCurrentUser().flatMap(currentUser ->
    //                            createNewAccount(createOrgAccount, primaryIdentify, currentUser.getUsername()));
    //                });
    //
    //    }
    //
    //    private Mono<Individual> createNewAccount(CreateOrgAccount createOrgAccount, String username, String
    // createUser) {
    //        // check exist username
    //        if (isExistedUsername(username)) {
    //            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "username.existed"));
    //        }
    //        // create new user
    //        String individualId = UUID.randomUUID().toString();
    //        String password = PasswordGenerator.generateCommonLangPassword();
    //        String email = createOrgAccount.getEmail();
    //        String userId = createUserKeycloak(username, password, email);
    //        // create new user profile
    //        Individual individual = new Individual(createOrgAccount.getRepresentative());
    //        individual.setId(individualId);
    //        individual.setNew(true);
    //        individual.setStatus(Constants.Activation.ACTIVE);
    //        individual.setUserId(userId);
    //        individual.setUsername(username);
    //        individual.setEmail(email);
    //        individual.setPhone(createOrgAccount.getPhone());
    //        individual.setName(createOrgAccount.getName());
    //        individual.setPasswordChange(false);
    //        individual.setCreateBy(createUser);
    //        individual.setUpdateBy(createUser);
    //
    //        // save password into table user_credential
    //        String hashedPassword = cipherManager.encrypt(password, publicKey);
    //        UserCredential userCredential = UserCredential.builder()
    //                .id(UUID.randomUUID().toString())
    //                .userId(userId)
    //                .username(username)
    //                .createBy(username)
    //                .updateBy(username)
    //                .pwdChanged(AuthConstants.STATUS_INACTIVE)
    //                .hashPwd(hashedPassword)
    //                .isNew(true)
    //                .status(Constants.STATUS.ACTIVE)
    //                .build();
    //
    //        var saveIndividual = individualRepository.save(individual);
    //        var saveUserCredential = userCredentialRep.save(userCredential);
    //
    //        return Mono.zip(saveIndividual, saveUserCredential)
    //                .flatMap(tuple2 -> {
    //                    CreateOrganizationRequest organizationDTO = buildOrgDTO(createOrgAccount);
    //                    return organizationService.createOrganization(organizationDTO, SYSTEM, individualId, true)
    //                            .flatMap(org -> {
    //                                // sending mail for user
    //                                CreateNotificationDTO notification = createNotificationDTO(password,
    //                                        Translator.toLocaleVi("email.title.signup.success"),
    //                                        Constants.TemplateMail.SIGN_UP_PASSWORD,
    //                                        ReceiverDataDTO.builder().email(createOrgAccount.getEmail()).build(),
    // username);
    //                                return notiServiceClient.insertTransmission(notification)
    //                                        .flatMap(rs -> {
    //                                            if (rs != null && rs.isPresent() &&
    //                                                    DataUtil.isNullOrEmpty(rs.get().getErrorCode())
    //                                                    && !DataUtil.isNullOrEmpty(rs.get().getMessage())) {
    //                                                log.info("Send email success to {}",
    //                                                        createOrgAccount.getEmail());
    //                                                return Mono.just(tuple2.getT1());
    //                                            }
    //                                            return Mono.error(new
    //                                                    BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
    // "client.noti.service.call.error"));
    //                                        });
    //                            });
    //                }).onErrorResume(error -> {
    //                    kcProvider.getRealmResource().users().delete(userId);
    //                    return Mono.error(error);
    //                });
    //    }

    private void validateRepresentative(IndividualDTO individualDTO) {
        if (DataUtil.isNullOrEmpty(individualDTO.getName())) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "individual.representative.name.null");
        }
        List<TenantIdentify> identifies = individualDTO.getIdentifies();
        if (identifies == null || identifies.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "individual.representative.identify.null");
        }
    }

    private String getPrimaryIdNo(List<TenantIdentify> identifies) {
        for (TenantIdentify identify : identifies) {
            if (Constants.Activation.ACTIVE.equals(identify.getPrimaryIdentify())) {
                return identify.getIdNo();
            }
        }
        return null;
    }

    private List<TenantIdentify> settingIdentifies(List<TenantIdentify> identifies) {
        if (identifies == null || identifies.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "danh.sach.giay.to.khong.hop.le");
        }
        List<TenantIdentify> resultIdentifies = new ArrayList<>();
        int MSTCount = 0;
        int GPDKCount = 0;
        for (TenantIdentify identify : identifies) {
            validateIdentifyOrg(identify);
            String idType = identify.getIdType();
            if (AuthConstants.IDType.MST.equalsIgnoreCase(idType)) {
                MSTCount += 1;
            } else if (AuthConstants.IDType.GPKD.equalsIgnoreCase(idType)) {
                GPDKCount += 1;
            }
            resultIdentifies.add(identify);
        }
        if (MSTCount > 1) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "identify.mst.over.size");
        }
        if (GPDKCount > 1) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "identify.gpkd.over.size");
        }
        if (MSTCount == 0 && GPDKCount == 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "indentify.invalid");
        }
        String primaryIdentifyType = MSTCount == 1 ? AuthConstants.IDType.MST : AuthConstants.IDType.GPKD;
        for (TenantIdentify identify : resultIdentifies) {
            if (identify.getIdType().equals(primaryIdentifyType)) {
                identify.setPrimaryIdentify(Constants.Activation.ACTIVE);
            } else {
                identify.setPrimaryIdentify(Constants.Activation.INACTIVE);
            }
            identify.setTrustStatus(Constants.Activation.ACTIVE);
        }
        return resultIdentifies;
    }

    private void validateIdentifyOrg(TenantIdentify tenantIdentify) {
        if (DataUtil.isNullOrEmpty(tenantIdentify.getIdType())) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "identify.id.type.invalid");
        }
        if (DataUtil.isNullOrEmpty(tenantIdentify.getIdNo())) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "identify.id.no.invalid");
        }
    }

    private Map<String, String> validateAndSetPrimaryIdentifies(List<TenantIdentify> identifies) {
        Map<String, String> identifyMap = new HashMap<>();
        if (identifies == null || identifies.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "danh.sach.giay.to.khong.hop.le");
        }
        for (TenantIdentify identify : identifies) {
            String idType = identify.getIdType();
            String idNo = identify.getIdNo();
            if (AuthConstants.IDType.MST.equalsIgnoreCase(idType)
                    || AuthConstants.IDType.GPKD.equalsIgnoreCase(idType)) {
                String existedValue = identifyMap.get(idType);
                if (existedValue != null) {
                    throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "khong.xac.dinh.duoc.so.giay.to");
                }
                identifyMap.put(idType, idNo);
            } else {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "loai.giay.to.khong.duoc.ho.tro");
            }
        }
        // validate just only: MST & GPKD
        if (identifyMap.keySet().size() == 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "danh.sach.giay.to.khong.hop.le");
        }
        return identifyMap;
    }

    private CreateOrganizationRequest buildOrgDTO(CreateOrgAccount createOrgAccount) {
        CreateOrganizationRequest createOrganizationRequest = new CreateOrganizationRequest();
        createOrganizationRequest.setName(createOrgAccount.getName());
        createOrganizationRequest.setEmail(createOrgAccount.getEmail());
        createOrganizationRequest.setPhone(createOrgAccount.getPhone());
        createOrganizationRequest.setFoundingDate(createOrgAccount.getFoundingDate());
        createOrganizationRequest.setIdentifies(createOrgAccount.getIdentifies());
        createOrganizationRequest.setRepresentative(createOrgAccount.getRepresentative());
        return createOrganizationRequest;
    }

    @Override
    @Transactional
    public Mono<UserOtp> signUp(SignupRequest signupRequest) {
        String requestEmail = DataUtil.safeTrim(signupRequest.getEmail());
        if (!ValidateUtils.validateRegex(requestEmail, Regex.EMAIL_REGEX)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, AuthConstants.Message.EMAIL_INVALID));
        }
        if (isExistedEmail(requestEmail)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "signup.email.exist"));
        }
        String otpValue = generateOtpValue();
        CreateNotificationDTO createNotificationDTO = createNotificationDTO(
                otpValue,
                Translator.toLocaleVi("email.title.signup"),
                Constants.TemplateMail.SIGN_UP,
                ReceiverDataDTO.builder().email(requestEmail).build(),
                null);
        return otpRepository.currentTimeDB().flatMap(localDateTime -> {
            //            UUID id = UUID.randomUUID();
            UserOtp otp = UserOtp.builder()
                    //                    .id(id.toString())
                    .otp(otpValue)
                    .createBy(SYSTEM)
                    .updateBy(SYSTEM)
                    .email(requestEmail)
                    .tries(0)
                    .expTime(localDateTime.plusMinutes(Constants.Otp.EXP_MINUTE))
                    .type(Constants.Otp.REGISTER)
                    .build();
            AppUtils.runHiddenStream(otpRepository.disableOtp(requestEmail, Constants.Otp.REGISTER, SYSTEM));
            AppUtils.runHiddenStream(generateOtp(otp));
            return notiServiceClient
                    .insertTransmission(createNotificationDTO)
                    .flatMap(objects -> {
                        if (objects.isPresent()
                                && (DataUtil.isNullOrEmpty(objects.get().getErrorCode())
                                && !DataUtil.isNullOrEmpty(objects.get().getMessage()))) {
                            return Mono.just(otp);
                        }
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS,
                                (objects.isPresent()) ? objects.get().getMessage() : "params.invalid"));
                    })
                    .onErrorResume(throwable -> Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
        });
    }

    private CreateNotificationDTO createNotificationDTO(
            String subTitle, String title, String template, ReceiverDataDTO data, String externalData) {
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
        createNotificationDTO.setSender(SYSTEM);
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
        List<ReceiverDataDTO> list = new ArrayList<>();
        list.add(data);
        createNotificationDTO.setReceiverList(list);
        return createNotificationDTO;
    }

    @Override
    public Mono<UserOtp> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String userName = DataUtil.safeTrim(forgotPasswordRequest.getUsername());
        if (DataUtil.isNullOrEmpty(userName)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "individual.not.found"));
        }
        if (isExistedUsername(userName)) {
            UUID id = UUID.randomUUID();
            String otpValue = generateOtpValue();
            List<UserRepresentation> listUser =
                    kcProvider.getRealmResource().users().search(userName, true);
            UserRepresentation user = listUser.getFirst();
            String requestEmail = user.getEmail();
            CreateNotificationDTO createNotificationDTO = createNotificationDTO(
                    otpValue,
                    Translator.toLocaleVi("email.title.forgot.password"),
                    Constants.TemplateMail.FORGOT_PASSWORD,
                    ReceiverDataDTO.builder()
                            .userId(user.getId())
                            .email(requestEmail)
                            .build(),
                    null);
            return otpRepository
                    .currentTimeDB()
                    .flatMap(time -> {
                        UserOtp otpBuild = UserOtp.builder()
                                .id(id.toString())
                                .otp(otpValue)
                                .createBy(SYSTEM)
                                .updateBy(SYSTEM)
                                .tries(0)
                                .email(requestEmail)
                                .expTime(time.plusMinutes(Constants.Otp.EXP_MINUTE))
                                .type(Constants.Otp.FORGOT_PASSWORD)
                                .build();
                        AppUtils.runHiddenStream(
                                otpRepository.disableOtp(requestEmail, Constants.Otp.FORGOT_PASSWORD, SYSTEM));
                        AppUtils.runHiddenStream(generateOtp(otpBuild));
                        return notiServiceClient
                                .insertTransmission(createNotificationDTO)
                                .flatMap(objects -> {
                                    if (objects.isPresent()
                                            && (DataUtil.isNullOrEmpty(
                                            objects.get().getErrorCode())
                                            && !DataUtil.isNullOrEmpty(
                                            objects.get().getMessage()))) {
                                        return Mono.just(otpBuild);
                                    }
                                    return Mono.error(new BusinessException(
                                            CommonErrorCode.INVALID_PARAMS,
                                            (objects.isPresent())
                                                    ? objects.get().getMessage()
                                                    : "params.invalid"));
                                });
                    })
                    .onErrorResume(throwable -> Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
        } else {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "forgot.pass.email"));
        }
    }

    private Mono<UserOtp> generateOtp(UserOtp otp) {
        assert otp.getId() != null;
        return otpRepository
                .findById(UUID.fromString(otp.getId()))
                .flatMap(otpRepository::save)
                .switchIfEmpty(otpRepository.save(otp));
    }

    private boolean isExistedEmail(String email) {
        return !kcProvider.getRealmResource().users().searchByEmail(email, true).isEmpty();
    }

    private Integer countAccountByEmail(String email) {
        return kcProvider.getRealmResource().users().searchByEmail(email, true).size();
    }

    private boolean isExistedUsername(String username) {
        return !kcProvider.getRealmResource().users().search(username, true).isEmpty();
    }

    private String createUserKeycloak(String username, String password, String email) {
        // create new keycloak's user
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
                .get(Constants.RoleName.USER)
                .toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(userRealmRole));
        // get and add role 'user' for ezbuy-client
        String clientIdOfHub = kcProvider
                .getRealmResource()
                .clients()
                .findByClientId(EZBUY_CLIENT)
                .getFirst()
                .getId();
        RoleRepresentation adminRoleWebclient = kcProvider
                .getRealmResource()
                .clients()
                .get(clientIdOfHub)
                .roles()
                .get(Constants.RoleName.USER)
                .toRepresentation();
        userResource.roles().clientLevel(clientIdOfHub).add(Collections.singletonList(adminRoleWebclient));
        return userId;
    }

    public void checkAssignedRoles() {
        Keycloak keycloak = keycloakProvider.getInstance();
        String serviceAccountUserId = keycloak.realm(keycloakProvider.getRealm())
                .clients()
                .findByClientId(keycloakProvider.getClientID())
                .getFirst()
                .getClientId();

        List<RoleRepresentation> assignedRealmRoles = keycloak.realm(keycloakProvider.getRealm())
                .users()
                .get(serviceAccountUserId)
                .roles()
                .realmLevel()
                .listEffective();

        assignedRealmRoles.forEach(role -> System.out.println("Assigned role: " + role.getName()));
    }

    @Override
    public Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange) {
        String requestEmail = DataUtil.safeTrim(resetPasswordRequest.getEmail());
        String requestOtp = DataUtil.safeTrim(resetPasswordRequest.getOtp());
        //        if (!ValidateUtils.validateRegex(requestEmail, Regex.EMAIL_REGEX)) {
        //            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.email.invalid"));
        //        }
        if (!requestOtp.matches(Regex.OTP_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.otp.invalid"));
        }
        if (!resetPasswordRequest.getPassword().matches(Regex.PASSWORD_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        }
        if (!resetPasswordRequest.getPassword().matches(Regex.UTF8_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        }

        List<UserRepresentation> listUser =
                kcProvider.getRealmResource().users().search(requestEmail, true);
        if (DataUtil.isNullOrEmpty(listUser)) {
            return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.not.found"));
        }
        UserRepresentation user = listUser.getFirst();
        return otpRepository
                .confirmOtp(user.getEmail(), Constants.Otp.FORGOT_PASSWORD, requestOtp, 1)
                .flatMap(result -> {
                    if (Boolean.FALSE.equals(result)) {
                        return Mono.error(new BusinessException(ErrorCode.OtpErrorCode.OTP_NOT_MATCH, "otp.not.match"));
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
                                UserCredential userCredential = new UserCredential();

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
                    var saveDisableOtp = AppUtils.insertData(
                            otpRepository.disableOtp(user.getEmail(), Constants.Otp.FORGOT_PASSWORD, SYSTEM));

                    return Mono.zip(saveUserCredential, saveDisableOtp)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "update.user-credential-or-user-otp.error")))
                            .flatMap(tuple -> {
                                AppUtils.runHiddenStream(saveLog(
                                        user.getId(),
                                        requestEmail,
                                        ActionLogType.FORGOT_PASSWORD,
                                        serverWebExchange.getRequest()));
                                return Mono.just(new DataResponse<>("reset.password.success", null));
                            });
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "otp.not.found")));
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
                    var updateIndividual = individualRepository
                            .findByUsername(tokenUser.getUsername())
                            .flatMap(individual -> {
                                individual.setPasswordChange(true);
                                individual.setNew(false);
                                return individualRepository.save(individual);
                            })
                            .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("individual.not.exits"))));
                    // update user credential
                    var updateCredential = userCredentialRepo
                            .getUserCredentialByUserName(tokenUser.getUsername(), Constants.STATUS.ACTIVE)
                            .collectList()
                            .flatMap(userCredentials -> {
                                UserCredential userCredential = new UserCredential();
                                String hashedPassword = cipherManager.encrypt(request.getNewPassword(), publicKey);
                                if (DataUtil.isNullOrEmpty(userCredentials)) {
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
                    var getTokenInfo = keyCloakClient
                            .getToken(loginRequest)
                            .thenReturn(tokenUser)
                            .onErrorMap(throwable -> new BusinessException(
                                    CommonErrorCode.BAD_REQUEST,
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
                                ActionLogType.CHANGE_PASSWORD,
                                serverWebExchange.getRequest()));
                    });
                });
    }

    @Override
    @Transactional
    public Mono<Individual> confirmOtpAndCreateUser(CreateAccount createAccount) {
        String otp = createAccount.getOtp();
        String email = createAccount.getEmail();
        String password = createAccount.getPassword();
        String system = createAccount.getSystem();
        if (!ValidateUtils.validateRegex(email, Regex.EMAIL_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.email.invalid"));
        }
        //        if (!password.matches(Regex.PASSWORD_REGEX)) {
        //            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dto.password.invalid"));
        //        }
        return otpRepository.confirmOtp(email, Constants.Otp.REGISTER, otp, 1).flatMap(isConfirmOtp -> {
            if (!isConfirmOtp) {
                return Mono.error(new BusinessException(ErrorCode.OtpErrorCode.OTP_NOT_MATCH, "otp.not.match"));
            }
            if (isExistedEmail(email)) {
                return Mono.error(new BusinessException(ErrorCode.AuthErrorCode.USER_EXISTED, "signup.email.exist"));
            }
            return createUserInKeyCloak(email, password, email, system);
        });
    }

    private Mono<Individual> createUserInKeyCloak(String username, String password, String email, String system) {
        // create new user
        String userId = createUserKeycloak(username, password, email);
        // save password into table user_credential
        String hashedPassword = cipherManager.encrypt(password, publicKey);
        // bo sung danh dau khong can doi mat khau cho tk tao tu hub
        int pwdChanged = AuthConstants.System.SME_HUB.equals(system)
                ? AuthConstants.STATUS_ACTIVE
                : AuthConstants.STATUS_INACTIVE;
        UserCredential userCredential = UserCredential.builder()
                .userId(userId)
                .username(username)
                .createBy(username)
                .updateBy(username)
                .pwdChanged(pwdChanged)
                .hashPwd(hashedPassword)
                .isNew(true)
                .status(Constants.STATUS.ACTIVE)
                .build();
        Individual individual = Individual.builder()
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
                        .disableOtp(username, Constants.Otp.REGISTER, SYSTEM)
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
                .get(Constants.RoleName.USER)
                .getRoleUserMembers();
        for (UserRepresentation u : set) {
            if (Boolean.TRUE.equals(u.isEnabled())) {
                list.add(u.getId());
            }
        }
        return Mono.just(list);
    }

    @Override
    @Transactional
    public Mono<Void> createUserTestPerformence(int startIndex, int numAccount) {
        for (int i = 0; i < numAccount; i++) {
            String email = "hoangtien2k3" + startIndex + "@gmail.com";
            String password = "tienha@!@#";
            createUserInKeyCloak(email, password, email, EMPTY);
            startIndex++;
        }
        return Mono.empty();
    }

    @Override
    public Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String username) {
        if (DataUtil.isNullOrEmpty(username)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "username.required"));
        }
        return userCredentialRepo
                .getUserCredentialByUserName(username, Constants.STATUS.ACTIVE)
                .collectList()
                .flatMap(rs -> {
                    if (rs.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "user.not.found"));
                    }
                    return Mono.just(GetTwoWayPasswordResponse.builder()
                            .password(rs.getFirst().getHashPwd())
                            .build());
                });
    }

    private Mono<Boolean> saveLog(String userId, String username, String type, ServerHttpRequest request) {
        ActionLog log = new ActionLog();
        log.setId(UUID.randomUUID().toString());
        log.setUserId(userId);
        log.setUsername(username);
        log.setType(type);
        log.setIp(RequestUtils.getIpAddress(request));
        return actionLogRepository.save(log).map(rs -> true);
    }

    @Override
    public Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request) {
        if (DataUtil.isNullOrEmpty(request.getDateReport())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dateReport.not.empty"));
        }
        return actionLogRepository
                .countLoginInOneDay(request.getDateReport(), ActionLogType.LOGIN)
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "action-log.login.not.found")))
                .flatMap(rs -> Mono.just(
                        GetActionLoginReportResponse.builder().loginCount(rs).build()));
    }

    @Override
    public Mono<DataResponse> confirmOTP(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        String otp = DataUtil.safeTrim(confirmOTPRequest.getOtp());
        if (DataUtil.isNullOrEmpty(otp)) {
            return Mono.error(new BusinessException(ErrorCode.OtpErrorCode.OTP_EMPTY, "dto.otp.not.empty"));
        }
        String email = DataUtil.safeTrim(confirmOTPRequest.getEmail());
        String type = DataUtil.safeTrim(confirmOTPRequest.getType());
        return otpRepository.confirmOtp(email, type, otp, 1).flatMap(result -> {
            if (Boolean.FALSE.equals(result)) {
                return Mono.error(new BusinessException(
                        ErrorCode.OtpErrorCode.OTP_NOT_MATCH, Translator.toLocaleVi("otp.not.match")));
            }
            return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
        });
    }

    @Override
    public Mono<DataResponse<String>> generateOtp(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange) {
        String email = DataUtil.safeTrim(confirmOTPRequest.getEmail());
        String type = DataUtil.safeTrim(confirmOTPRequest.getType());
        String otpValue = generateOtpValue();
        return otpRepository.currentTimeDB().flatMap(localDateTime -> {
            UserOtp otp = UserOtp.builder()
                    .type(type)
                    .email(email)
                    .otp(otpValue)
                    .expTime(localDateTime.plusMinutes(Constants.Otp.EXP_OTP_AM_MINUTE))
                    .tries(0)
                    .status(1)
                    .createBy(Constants.RoleName.SYSTEM)
                    .updateBy(Constants.RoleName.SYSTEM)
                    .newOtp(true)
                    .build();
            var disableOtpMono = AppUtils.insertData(otpRepository.disableOtp(email, type, Constants.RoleName.SYSTEM));
            var saveOtpMono = AppUtils.insertData(otpRepository.save(otp));
            return Mono.zip(disableOtpMono, saveOtpMono)
                    .map(tuple -> new DataResponse<>(SUCCESS, otpValue))
                    .onErrorResume(throwable -> Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, throwable.getMessage())));
        });
    }

    private String encodeBase64String(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return EMPTY;
        }
        try {
            Base64 base64 = new Base64();
            // com.nimbusds.jose.util.Base64.encode(rawHmac);
            return new String(base64.encode(input.getBytes()));
        } catch (Exception e) {
            return EMPTY;
        }
    }

    private Mono<DataResponse> validateConfig(Optional<DataResponse> rs) {
        if (rs.isEmpty()) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "settingClient.not.found"));
        }
        DataResponse dataResponse = rs.get();
        if (!DataUtil.isNullOrEmpty(dataResponse.getErrorCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "settingClient.not.found"));
        }
        return Mono.just(dataResponse);
    }

    // generate otp value random
    private String generateOtpValue() {
        Random random = new SecureRandom();
        return new DecimalFormat("000000").format(random.nextInt(999999));
    }
}
