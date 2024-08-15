//package com.ezbuy.authservice.service.impl;
//
//import com.ezbuy.authmodel.constants.AuthConstants;
//import com.ezbuy.authmodel.dto.AreaDTO;
//import com.ezbuy.authmodel.dto.PaginationDTO;
//import com.ezbuy.authmodel.dto.UserProfileDTO;
//import com.ezbuy.authmodel.dto.request.QueryUserRequest;
//import com.ezbuy.authmodel.dto.request.SignHashRequest;
//import com.ezbuy.authmodel.dto.request.UpdateUserRequest;
//import com.ezbuy.authmodel.dto.response.*;
//import com.ezbuy.authmodel.model.Individual;
//import com.ezbuy.authmodel.model.TenantIdentify;
//import com.ezbuy.authmodel.model.UserProfile;
//import com.ezbuy.authservice.config.KeycloakProvider;
//import com.ezbuy.authservice.repository.IndividualRepository;
//import com.ezbuy.authservice.repository.TenantIdentifyRepo;
//import com.ezbuy.authservice.repository.UserRepository;
//import com.ezbuy.authservice.service.IdentifyService;
//import com.ezbuy.authservice.service.UserService;
//import com.ezbuy.framework.constants.CommonErrorCode;
//import com.ezbuy.framework.constants.Constants;
//import com.ezbuy.framework.constants.Regex;
//import com.ezbuy.framework.exception.BusinessException;
//import com.ezbuy.framework.model.response.DataResponse;
//import com.ezbuy.framework.utils.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.RoleRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.cert.X509Certificate;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.keycloak.util.JsonSerialization.mapper;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
//
//    private final UserRepository userRepository;
//    private final IndividualRepository individualRepository;
//    private final TenantIdentifyRepo tenantIdentifyRepo;
//    private final KeycloakProvider kcProvider;
////    private final MySignClient mySignClient;
////    private final SettingClient settingClient;
//    private final IdentifyService identifyService;
//    private final MinioUtils minioUtils;
////    private final TaxBranchRepository taxBranchRepository;
////    private final IdTypeRepository idTypeRepository;
////    private final MySignProperties mySignProperties;
////    private final ProductClient productClient;
//
//    @Value("${keycloak.realm}")
//    public String realm;
//
//    @Value("${minio.bucket}")
//    public String mySignBucket;
//
//    @Override
//    public Mono<Optional<UserProfile>> getUserProfile() {
//        return SecurityUtils.getCurrentUser()
//                .flatMap(currentUser -> userRepository.findById(currentUser.getId())
//                        .flatMap(user -> Mono.just(Optional.ofNullable(user)))
//                        .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found"))));
//    }
//
//    @Override
//    public Mono<UserProfile> update(UpdateUserRequest u) {
//        if (!ValidateUtils.validateRegexV2(DataUtil.safeTrim(u.getPhone()), Regex.PHONE_REGEX)) {
//            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "user.phone.invalid"));
//        }
//        if (!ValidateUtils.validateRegex(DataUtil.safeTrim(u.getTaxCode()), Regex.TAX_CODE_REGEX)) {
//            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "user.taxCode.invalid"));
//        }
//        return
//                Mono.zip(SecurityUtils.getCurrentUser(), userRepository.currentTimeDb())
//                        .flatMap(currentUser -> userRepository.findById(currentUser.getT1().getId())
//                                .map(userProfile -> {
//                                    userProfile.setCompanyName(DataUtil.safeTrim(u.getCompanyName()));
//                                    userProfile.setTaxCode(DataUtil.safeTrim(u.getTaxCode()));
//                                    userProfile.setTaxDepartment(DataUtil.safeTrim(u.getTaxDepartment()));
//                                    userProfile.setRepresentative(DataUtil.safeTrim(u.getRepresentative()));
//                                    userProfile.setFoundingDate(u.getFoundingDate());
//                                    userProfile.setBusinessType(DataUtil.safeTrim(u.getBusinessType()));
//                                    userProfile.setProvinceCode(DataUtil.safeTrim(u.getProvinceCode()));
//                                    userProfile.setDistrictCode(DataUtil.safeTrim(u.getDistrictCode()));
//                                    userProfile.setPrecinctCode(DataUtil.safeTrim(u.getPrecinctCode()));
//                                    userProfile.setPhone(DataUtil.safeTrim(u.getPhone()));
//                                    userProfile.setUpdateAt(currentUser.getT2());
//                                    userProfile.setUpdateBy(currentUser.getT1().getUsername());
//                                    return userProfile;
//                                }).flatMap(userRepository::save))
//                        .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "query.user.not.found")));
//    }
//
//    @Override
//    public Flux<UserContact> getUserContacts(List<UUID> userIds) {
//        if (userIds == null) {
//            return Flux.just(null);
//        }
//        Set<UUID> unixUserIds = new HashSet<>(userIds);
//        if (unixUserIds.size() > Constants.ArrayLimit.COMMON) {
//            return Flux.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "array.limit"));
//        }
//        UsersResource usersResource = kcProvider.getRealmResource().users();
//        return Flux.fromIterable(unixUserIds)
//                .map(userId -> mappingUserContract(userId, usersResource));
//    }
//
//    private UserContact mappingUserContract(UUID userId, UsersResource usersResource) {
//        try {
//            String email = usersResource.get(userId.toString()).toRepresentation().getEmail();
//            return new UserContact(userId, email);
//        } catch (Exception ex) {
//            log.error("Get UserResource error {}", userId, ex);
//        }
//        return new UserContact(userId, null);
//    }
//
//    @Override
//    public Mono<Optional<UserProfile>> getUserById(String id) {
//        return userRepository.findById(id)
//                .flatMap(user -> Mono.just(Optional.ofNullable(user)))
//                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
//    }
//
////    @Override
////    public Mono<List<CredentialResponse>> getCredentialsList(String idNo) {
////        if (DataUtil.isNullOrEmpty(idNo)) {
////            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.invalid"));
////        }
////        return getMySignUserId(idNo)
////                .map(userId -> MySignCredentialsListRequest.builder()
////                        .clientId(mySignProperties.getClientId())
////                        .clientSecret(mySignProperties.getClientSecret())
////                        .profileId(mySignProperties.getProfileId())
////                        .userId(userId)
////                        .certificates("chain")
////                        .certInfo(true)
////                        .authInfo(true)
////                        .build())
////                .flatMap(mySignClient::getCredentialsList)
////                .map(mySignCredentials -> mySignCredentials.stream()
////                        .filter(item -> item.getCert() != null)
////                        .map(mySignCredential -> {
////                            CredentialResponse credentialResponse = new CredentialResponse();
////                            credentialResponse.setCredentialId(mySignCredential.getCredentialId());
////                            credentialResponse.setCertificate(DataUtil.isNullOrEmpty(mySignCredential.getCert().getCertificates())
////                                    ? null : mySignCredential.getCert().getCertificates().get(0));
////                            credentialResponse.setStatus(mySignCredential.getCert().getStatus());
////                            credentialResponse.setSubjectDN(mySignCredential.getCert().getSubjectDN());
////                            credentialResponse.setSerialNumber(mySignCredential.getCert().getSerialNumber());
////                            credentialResponse.setValidFrom(mySignCredential.getCert().getValidFrom());
////                            credentialResponse.setValidTo(mySignCredential.getCert().getValidTo());
////
////                            return credentialResponse;
////                        }).collect(Collectors.toList()));
////    }
//
////    @Override
////    public Mono<DataResponse> sighHash(SignHashRequest request) {
////        if (DataUtil.isNullOrEmpty(request.getCredentialID())) {
////            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "mysign.credentialID"));
////        }
////        if (DataUtil.isNullOrEmpty(request.getCertificate())) {
////            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "mysign.certificate"));
////        }
////        if (DataUtil.isNullOrEmpty(request.getFile())) {
////            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "mysign.file.no.valid"));
////        }
////        if (DataUtil.isNullOrEmpty(request.getIdNo())) {
////            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.invalid"));
////        }
////        byte[] bytesOfFile = org.apache.commons.codec.binary.Base64.decodeBase64(request.getFile());
////        InputStream fileInputStream = new ByteArrayInputStream(bytesOfFile);
////
////        Mono<String> monoFile = null;
////        try {
////            monoFile = minioUtils.uploadFileReturnObject(fileInputStream, mySignBucket, MinioUtils.PDF_CONTENT_TYPE);
////        } catch (Exception e) {
////            log.error("Upload file error:", e);
////            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "upload.file.minio.error");
////        }
////        var monoTenantIdentify = SecurityUtils.getCurrentUser()
////                .flatMap(currentUser -> tenantIdentifyRepo.getByIdNo(currentUser.getId(), request.getIdNo(), AuthConstants.TenantType.ORGANIZATION).collectList())
////                .flatMap(tenantIdentifies -> {
////
////                    TenantIdentify tenantIdentifyResult = null;
////
////                    if (tenantIdentifies.isEmpty()) {
////                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.not.match"));
////                    }
////
////                    if (tenantIdentifies.size() == 1) {
////                        tenantIdentifyResult = tenantIdentifies.get(0);
////                    }
////
////                    if (tenantIdentifies.size() > 1) {
////                        for (TenantIdentify tenantIdentify: tenantIdentifies) {
////                            if (AuthConstants.TenantIdentify.PRIMARY_IDENTIFY.equals(tenantIdentify.getPrimaryIdentify())) {
////                                tenantIdentifyResult = tenantIdentify;
////                                break;
////                            }
////                        }
////                    }
////
////                    if (DataUtil.isNullOrEmpty(tenantIdentifyResult)) {
////                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.not.tenantIdentify"));
////                    }
////
////                    if (AuthConstants.MySign.SIGH_HASH_SUCCESS.equals(tenantIdentifyResult.getTrustStatus())) {
////                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.trusted"));
////                    }
////                    if (AuthConstants.MySign.SIGH_HASH_WAIT.equals(tenantIdentifyResult.getTrustStatus())) {
////                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.wait.trust"));
////                    }
////
////                    return Mono.just(tenantIdentifyResult);
////                }).switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.not.match")));
////
////        return Mono.zip(monoFile, monoTenantIdentify, SecurityUtils.getCurrentUser()).flatMap(input -> {
////
////            File tempFile = transferToFile(bytesOfFile);
////            String base64Hash = createHash(request.getCertificate(), tempFile.getAbsolutePath());
////
////            if (base64Hash == null || base64Hash.trim().isEmpty()) {
////                return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "create.hash.error"));
////            }
////            if (!tempFile.delete()) {
////                log.error("Remove temp file error: " + tempFile.getAbsolutePath());
////            }
////            List<String> hashList = new ArrayList<>();
////            hashList.add(base64Hash);
////
////            String hashAlgo = AuthConstants.MySign.OID_NIST_SHA1;
////            String hash = hashList.get(0);
////            if (hash != null && hash.length() != 28) {
////                hashAlgo = AuthConstants.MySign.OID_NIST_SHA256;
////            }
////
////            int id = 123;
////            String data = "Trust thông tin";
////            byte[] utf8DataBytes = data.getBytes(StandardCharsets.UTF_8);
////            String dataDisplay = Base64.getEncoder().encodeToString(utf8DataBytes);
////
////            List<DocumentBO> documents = new ArrayList<>();
////            for (int i = 0; i < hashList.size(); i++) {
////                documents.add(new DocumentBO(id, dataDisplay));
////            }
////            MySignSignHashRequest mySignSignHashRequest = new MySignSignHashRequest();
////            mySignSignHashRequest.setClientId(mySignProperties.getClientId());
////            mySignSignHashRequest.setClientSecret(mySignProperties.getClientSecret());
////            mySignSignHashRequest.setCredentialID(request.getCredentialID());
////            mySignSignHashRequest.setDocuments(documents);
////            mySignSignHashRequest.setNumSignatures(documents.size());
////            mySignSignHashRequest.setHash(hashList);
////            mySignSignHashRequest.setHashAlgo(hashAlgo);
////            mySignSignHashRequest.setSignAlgo(AuthConstants.MySign.OID_RSA_RSA);
////            mySignSignHashRequest.setAsync(AuthConstants.MySign.SIGN_HASH_ASYNC);
////            String mySignUserId;
////            if ("038099002254".equalsIgnoreCase(request.getIdNo())) {
////                mySignUserId = "CMT_038099002254";
////            } else if (AuthConstants.TenantType.ORGANIZATION.equals(input.getT2().getType())) {
//////                mySignUserId = "MST_" + request.getIdNo();
////                mySignUserId = request.getIdNo();
////            } else {
//////                mySignUserId = "CMT_" + request.getIdNo();
////                mySignUserId = request.getIdNo();
////            }
////            return Mono.zip(Mono.just(input.getT1()), Mono.just(input.getT2()), mySignClient.signHash(mySignSignHashRequest, mySignUserId), Mono.just(input.getT3()));
////        }).flatMap(data -> {
////            TenantIdentify tenantIdentify = data.getT2();
////            tenantIdentify.setCertificate(request.getCertificate());
////            tenantIdentify.setTrustFile(data.getT1());
////            tenantIdentify.setTransactionId(data.getT3().getTransactionId());
////            tenantIdentify.setTrustStatus(data.getT3().getStatus());
////            tenantIdentify.setNew(false);
////            return Mono.zip(tenantIdentifyRepo.save(tenantIdentify), Mono.just(data.getT4()));
////        }).map(tenantIdentify -> {
////
////            // add role admin of user
////            if (AuthConstants.MySign.SIGH_HASH_SUCCESS.equals(tenantIdentify.getT1().getTrustStatus())) {
////                // add role admin of user
////                String clientIdOfHub = kcProvider.getReamResource().clients().findByClientId(AuthConstants.ClientName.HUB_SME).get(0).getId();
////                RoleRepresentation adminRoleWebclient = kcProvider.getReamResource().clients().get(clientIdOfHub).roles().get(Constants.RoleHubSme.ADMIN).toRepresentation();
////                kcProvider.getReamResource().users().get(tenantIdentify.getT2().getId()).roles().clientLevel(clientIdOfHub).add(Collections.singletonList(adminRoleWebclient));
////                // remove role user of web-client for Hub
////                RoleRepresentation roleUserRepresentationUserHub = kcProvider.getReamResource().clients().get(clientIdOfHub).roles().get(Constants.RoleHubSme.USER).toRepresentation();
////                kcProvider.getReamResource().users().get(tenantIdentify.getT2().getId()).roles().clientLevel(clientIdOfHub).remove(Collections.singletonList(roleUserRepresentationUserHub));
////            }
////
////            if (AuthConstants.MySign.SIGH_HASH_WAIT.equals(tenantIdentify.getT1().getTrustStatus())
////                    || AuthConstants.MySign.SIGH_HASH_SUCCESS.equals(tenantIdentify.getT1().getTrustStatus())) {
////                return new DataResponse(Translator.toLocaleVi(SUCCESS), true);
////            } else {
////                DataResponse dataResponse = new DataResponse(Translator.toLocaleVi("mysign.sign.hash.error"), false);
////                dataResponse.setErrorCode(CommonErrorCode.INTERNAL_SERVER_ERROR);
////                return dataResponse;
////            }
////        });
////    }
//
////    @Override
////    public Mono<DataResponse> getStatusSignHash() {
////        String clientIdOfHub = kcProvider.getRealmResource().clients().findByClientId(AuthConstants.ClientName.HUB_SME).get(0).getId();
////        RoleRepresentation roleRepresentation = kcProvider.getRealmResource().clients().get(clientIdOfHub).roles().get(Constants.RoleHubSme.ADMIN).toRepresentation();
////        RoleRepresentation roleRepresentationUser = kcProvider.getRealmResource().clients().get(clientIdOfHub).roles().get(Constants.RoleHubSme.USER).toRepresentation();
////
////        log.info("clientIdOfHub {}", clientIdOfHub);
////        log.info("RoleRepresentation admin {}", roleRepresentation);
////        log.info("RoleRepresentation user {}", roleRepresentationUser);
////        return tenantIdentifyRepo.getByTrustStatus(AuthConstants.MySign.SIGH_HASH_WAIT)
////                .flatMapSequential(tenantIdentify -> {
////                    log.info("tenantIdentify sync-sign-hash id: {} idNo: {}", tenantIdentify.getId(), tenantIdentify.getIdNo());
////                        MySignGetStatusRequest request = new MySignGetStatusRequest();
////                        if ("038099002254".equalsIgnoreCase(tenantIdentify.getIdNo())) {
////                            request.setUserId("CMT_038099002254");
////                        } else {
//////                            request.setUserId("MST_" + tenantIdentify.getIdNo());
////                            request.setUserId(tenantIdentify.getIdNo());
////                        }
////                        request.setTransactionId(tenantIdentify.getTransactionId());
////                    return mySignClient.getStatusSignHash(request).flatMap(response -> {
////                            if (AuthConstants.MySign.SIGH_HASH_SUCCESS.equals(response.getStatus())) {
////                                log.info("status sync-sign-hash {}", response.getStatus());
////                                var insertSignature = insertSignature(tenantIdentify, response.getSignatures(), clientIdOfHub, roleRepresentation, roleRepresentationUser);
////                                // call productClient to syncSubscriber with telecomServiceId = 7 (CA) and idNo; isdn null
////                                UpdateAccountServiceInfoDTO updateAccountServiceInfoDTO = new UpdateAccountServiceInfoDTO();
////                                updateAccountServiceInfoDTO.setTelecomServiceId(7L);
////                                updateAccountServiceInfoDTO.setIdNo(tenantIdentify.getIdNo());
////                                var syncSubscriberOrder = productClient.syncSubscriberOrder(updateAccountServiceInfoDTO);
////                                return Mono.zip(insertSignature, syncSubscriberOrder)
////                                        .flatMap(tuple2 -> {
////                                            log.info("sync-sign-hash success for idNo {}", tenantIdentify.getIdNo());
////                                            return Mono.just(new TenantIdentify());
////                                        });
////                            } else if (!AuthConstants.MySign.SIGH_HASH_WAIT.equals(response.getStatus())) {
////                                tenantIdentify.setTrustStatus(response.getStatus());
////                                tenantIdentify.setNew(false);
////                                return tenantIdentifyRepo.save(tenantIdentify);
////                            }
////                            return Mono.just(new TenantIdentify());
////                        });
////
////                }).collectList()
////                .flatMap(rs -> Mono.just(DataResponse.success("sign-hash.success")));
////    }
//
////    private Mono<Boolean> insertSignature(TenantIdentify tenantIdentify, List<String> signatures, String clientId, RoleRepresentation roleRepresentation, RoleRepresentation roleRepresentationUser) {
////        if (DataUtil.isNullOrEmpty(signatures)) {
////            log.error("No return signature with transaction: " + tenantIdentify.getTransactionId());
////            return Mono.empty();
////        }
////        File originFile = new File(UUID.randomUUID().toString());
////        minioUtils.downloadFileBlock(mySignBucket, tenantIdentify.getTrustFile(), originFile.getAbsolutePath());
////
////        File sighFile = new File(UUID.randomUUID().toString());
////        boolean isSuccess = FileUtils.insertSignaturePdfFile(signatures.get(0), sighFile.getPath(),
////                tenantIdentify.getCertificate(), originFile.getAbsolutePath());
////        if (!originFile.delete()) {
////            log.error("Remove local file error");
////        }
////        if (!isSuccess) {
////            log.error("insertSignaturePdfFile error transaction: " + tenantIdentify.getTransactionId());
////            if (!sighFile.delete()) {
////                log.error("Remove local sign file error");
////            }
////            return Mono.empty();
////        }
////
////        FileInputStream fileInputStream;
////        try {
////            fileInputStream = new FileInputStream(sighFile);
////        } catch (FileNotFoundException e) {
////            log.error("getSignHash error: ", e);
////            return Mono.empty();
////        }
////        try {
////            return minioUtils.uploadFileReturnObject(fileInputStream, mySignBucket, MinioUtils.PDF_CONTENT_TYPE)
////                    .flatMap(trustFile -> {
////                        if (!sighFile.delete()) {
////                            log.error("Remove local sign file error");
////                        }
////                        tenantIdentify.setTrustFile(trustFile);
////                        tenantIdentify.setTrustStatus(AuthConstants.MySign.SIGH_HASH_SUCCESS);
////                        tenantIdentify.setNew(false);
////                        return tenantIdentifyRepo.save(tenantIdentify)
////                                .flatMap(saveTenantIdentify -> individualRepository.getUserIdOfOwnerByIdNo(tenantIdentify.getId(), AuthConstants.PositionCode.OWNER)
////                                        .flatMap(userId -> {
////                                            log.info("userId add role admin {}", userId);
////                                            kcProvider.getRealmResource().users().get(userId).roles().clientLevel(clientId).add(Collections.singletonList(roleRepresentation));
////                                            kcProvider.getRealmResource().users().get(userId).roles().clientLevel(clientId).remove(Collections.singletonList(roleRepresentationUser));
////                                            return Mono.just(true);
////                                        }));
////                    });
////        } catch (Exception e) {
////            log.error("Upload file error:", e);
////            return Mono.empty();
////        }
////    }
//
////    public Mono<String> getMySignUserId(String idNo) {
////        return SecurityUtils.getCurrentUser()
////                .flatMap(currentUser -> identifyService.getIdNo(currentUser.getId(), null))
////                .map(tenantIdentifyDTOS -> {
////                    Optional<TenantIdentifyDTO> tenantIdentify = tenantIdentifyDTOS.stream()
////                            .filter(item -> idNo.equalsIgnoreCase(item.getIdNo())).findFirst();
////                    if (tenantIdentify.isEmpty()) {
////                        throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.not.match");
////                    }
////                    if (AuthConstants.MySign.SIGH_HASH_SUCCESS.equals(tenantIdentify.get().getTrustStatus())) {
////                        throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.trusted");
////                    }
////                    if (AuthConstants.MySign.SIGH_HASH_WAIT.equals(tenantIdentify.get().getTrustStatus())) {
////                        throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "id.no.wait.trust");
////                    }
////                    return tenantIdentify.get();
////                })
////                .map(tenantIdentify -> {
////                    String userId;
////                    if ("038099002254".equalsIgnoreCase(tenantIdentify.getIdNo())) {
////                        userId = "CMT_038099002254";
////                    } else {
//////                        userId = "MST_" + tenantIdentify.getIdNo();
////                        userId = tenantIdentify.getIdNo();
////                    }
////                    return userId;
////                });
////    }
//
////    @Override
////    public Flux<TaxBranchResponse> getTaxBranches() {
////        return taxBranchRepository.getTaxBranchActive()
////                .map(taxBranch -> TaxBranchResponse.builder()
////                        .id(taxBranch.getId())
////                        .name(taxBranch.getName())
////                        .build());
////    }
//
////    @Override
////    public Flux<IdTypeResponse> getIdTypes() {
////        return idTypeRepository.getIdTypeActive()
////                .map(idType -> IdTypeResponse.builder()
////                        .id(idType.getId())
////                        .name(idType.getName())
////                        .code(idType.getCode()).build());
////    }
//
//    private File transferToFile(byte[] bytes) {
//        try {
//            File destFile = new File(UUID.randomUUID().toString());
//            org.apache.commons.io.FileUtils.writeByteArrayToFile(destFile, bytes);
//            return destFile;
//        } catch (Exception e) {
//            log.error("Upload file error:", e);
//            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "create.file.error");
//        }
//    }
//
////    private String createHash(String certificate, String file) {
////        Path currentRelativePath = Paths.get("");
////        log.info("Current Path {}", currentRelativePath.toAbsolutePath().toString());
////        String folderRootCA = "./RootCA";
////        X509Certificate[] certChain = X509ExtensionUtil.getCertChainOfCert(certificate, folderRootCA);
////        String fontPath = "./font/times.ttf";
////        SignPdfFile pdfSig = new SignPdfFile();
////        try {
////            return HashFilePDF.getHashTypeRectangleText(pdfSig, file, certChain, fontPath);
////        } catch (Exception e) {
////            log.error("Create hash file error:", e);
////            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.hash.error");
////        }
////    }
//
//    /**
//     * query page user profile
//     *
//     * @param request query params
//     * @return page user profile
//     */
//    @Override
//    public Mono<QueryUserResponse> queryUserProfile(QueryUserRequest request) {
//        int size = DataUtil.safeToInt(request.getPageSize(), 20);
//        if (size <= 0 || size > 500) {
//            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "size.invalid"));
//        }
//
//        int page = DataUtil.safeToInt(request.getPageIndex(), 1);
//        if (page <= 0) {
//            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "page.invalid"));
//        }
//
//        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
//        request.setSort(sort);
//        return findKeycloakUser(request.getName()).collectList().flatMap(kcUsers -> {
//            if (DataUtil.isNullOrEmpty(kcUsers)) {
//                QueryUserResponse emptyResponse = new QueryUserResponse();
//                emptyResponse.setContent(Collections.emptyList());
//                emptyResponse.setPagination(PaginationDTO.builder()
//                        .pageIndex(1)
//                        .pageSize(size)
//                        .totalRecords(0L)
//                        .build());
//                return Mono.just(emptyResponse);
//            }
//            if (!DataUtil.isNullOrEmpty(request.getName())) {
//                request.setUserIds(kcUsers.stream().map(UserRepresentation::getId).collect(Collectors.toList()));
//            }
//            Flux<UserProfileDTO> userProfileFlux = userRepository.queryUserProfile(request)
//                    .doOnNext(userProf -> {
//                        kcUsers.stream().filter(element -> element.getId().equals(userProf.getUserId()))
//                                .findFirst().ifPresent(element -> {
//                                    userProf.setName(getFullName(element));
//                                });
//                    });
//            Mono<Long> countMono = userRepository.countUserProfile(request);
//            return Mono.zip(userProfileFlux.collectList(), countMono).map(zip -> {
//                PaginationDTO pagination = new PaginationDTO();
//                pagination.setPageSize(size);
//                pagination.setPageIndex(page);
//                pagination.setTotalRecords(zip.getT2());
//
//                QueryUserResponse queryUserResponse = new QueryUserResponse();
//                queryUserResponse.setContent(zip.getT1());
//                queryUserResponse.setPagination(pagination);
//
//                return queryUserResponse;
//            });
//        });
//    }
//
//    private String getFullName(UserRepresentation userRepresentation) {
//        return DataUtil.safeToString(userRepresentation.getLastName()) + " " +
//                DataUtil.safeToString(userRepresentation.getFirstName());
//    }
//
//    /**
//     * query keycloak user
//     *
//     * @param name filter by name
//     * @return
//     */
//    public Flux<UserRepresentation> findKeycloakUser(String name) {
//        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
//        return Flux.fromIterable(usersResource.list())
//                .filter(userRepresentation -> {
//                    String fullName = getFullName(userRepresentation);
//                    return DataUtil.isNullOrEmpty(name) || fullName.toLowerCase().contains(name.trim().toLowerCase());
//                });
//    }
//
//    /**
//     * fill missing data for user profile from keycloak
//     *
//     * @param userProfile   user profile dto
//     * @param usersResource keycloak user resource
//     */
//    private void fillDataUserFromKeycloak(UserProfileDTO userProfile, UsersResource usersResource) {
//        try {
//            UserRepresentation userRepresentation = usersResource.get(userProfile.getUserId()).toRepresentation();
//            if (userRepresentation != null) {
//                String firstName = DataUtil.safeToString(userRepresentation.getFirstName(), "");
//                String lastName = DataUtil.safeToString(userRepresentation.getLastName(), "");
//                userProfile.setName(lastName + " " + firstName);
//            }
//        } catch (Exception ignored) {
//            log.warn("get user keycloak error ", ignored);
//        }
//    }
//
//    /**
//     * export excel user profile
//     *
//     * @param request query params
//     * @return excel file resource
//     */
//    public Mono<Resource> exportUser(QueryUserRequest request) {
//        request.setPageIndex(null);
//        request.setPageSize(null);
//        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
//        request.setSort(sort);
//        return findKeycloakUser(request.getName())
//                .collectList()
//                .flatMap(kcUsers -> {
//                    if (DataUtil.isNullOrEmpty(kcUsers)) {
//                        return Mono.just(Collections.emptyList());
//                    }
//                    if (!DataUtil.isNullOrEmpty(request.getName())) {
//                        List<String> userIds = kcUsers.stream().map(UserRepresentation::getId).collect(Collectors.toList());
//                        request.setUserIds(userIds);
//                    }
//                    return userRepository.queryUserProfile(request)
//                            .doOnNext(userProf -> {
//                                kcUsers.stream()
//                                        .filter(element -> element.getId().equals(userProf.getUserId()))
//                                        .findFirst().ifPresent(userRepresentation -> {
//                                            userProf.setName(getFullName(userRepresentation));
//                                        });
//                            })
//                            .collectList();
//                })
//                .flatMap(userProfiles -> writeUserProfiles((List<UserProfileDTO>) userProfiles))
//                .flatMap(workbook -> Mono.fromCallable(() -> writeExcel(workbook)));
//    }
//
//    private ByteArrayResource writeExcel(Workbook workbook) {
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); workbook) {
//            workbook.write(os);
//            return new ByteArrayResource(os.toByteArray()) {
//                @Override
//                public String getFilename() {
//                    return "List_User_Profile.xlsx";
//                }
//            };
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "export.error");
//        }
//    }
//
//    /**
//     * write user profile list to workbook
//     *
//     * @param userProfiles user profile list
//     * @return workbook
//     */
//    private Mono<Workbook> writeUserProfiles(List<UserProfileDTO> userProfiles) {
//        Set<String> parents = new HashSet<>();
//        parents.add("");
//        for (UserProfileDTO userProfile : userProfiles) {
//            if (userProfile.getProvinceCode() == null || userProfile.getDistrictCode() == null) {
//                continue;
//            }
//            parents.add(userProfile.getProvinceCode());
//            parents.add(userProfile.getProvinceCode() + userProfile.getDistrictCode());
//        }
//        return getAreaName(parents).collectList().flatMap(areas -> {
//            log.info("areas : {}", areas);
//            return Mono.fromCallable(() -> {
//                return writeListUser(userProfiles, areas);
//            });
//        });
//    }
//
//    private Workbook writeListUser(List<UserProfileDTO> userProfiles, List<AreaDTO> areas) {
//        try (InputStream templateInputStream = new ClassPathResource("template/template_export_user.xlsx")
//                .getInputStream()
//        ) {
//            Workbook workbook = new XSSFWorkbook(templateInputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            String currentDate = DataUtil.formatDate(LocalDateTime.now(), Constants.DateTimePattern.DMY, "");
//            Cell exportDateCell = sheet.getRow(1).getCell(12);
//            exportDateCell.setCellValue(exportDateCell.getStringCellValue().replace("${date}", currentDate));
//            DataUtil.formatDate(LocalDateTime.now(), Constants.DateTimePattern.DMY_HMS, "");
//
//            int rowCount = 4;
//            int index = 1;
//            for (UserProfileDTO userProfile : userProfiles) {
//                Row row = sheet.createRow(rowCount++);
//
//                CellStyle style = workbook.createCellStyle();
//                style.setBorderBottom(BorderStyle.THIN);
//                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//                style.setBorderRight(BorderStyle.THIN);
//                style.setRightBorderColor(IndexedColors.BLUE.getIndex());
//                style.setBorderTop(BorderStyle.THIN);
//                style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//                style.setBorderLeft(BorderStyle.THIN);
//                style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//
//                String provinceName = areas.stream()
//                        .filter(area -> area.getAreaCode().equals(userProfile.getProvinceCode()))
//                        .map(AreaDTO::getName)
//                        .findFirst().orElse(null);
//                String districtName = areas.stream()
//                        .filter(area -> area.getAreaCode().equals(userProfile.getProvinceCode() + userProfile.getDistrictCode()))
//                        .map(AreaDTO::getName)
//                        .findFirst().orElse(null);
//                String precinctName = areas.stream()
//                        .filter(area -> area.getAreaCode().equals(userProfile.getProvinceCode() + userProfile.getDistrictCode() + userProfile.getPrecinctCode()))
//                        .map(AreaDTO::getName)
//                        .findFirst().orElse(null);
//                writeRow(row, style, 0, Arrays.asList(
//                        String.valueOf(index++),
//                        userProfile.getName(),
//                        userProfile.getPhone(),
//                        userProfile.getTaxCode(),
//                        provinceName,
//                        districtName,
//                        precinctName,
//                        userProfile.getStreetBlock(),
//                        DataUtil.formatDate(userProfile.getCreateAt(), Constants.DateTimePattern.DMY_HMS, ""),
//                        userProfile.getCreateBy(),
//                        userProfile.getCompanyName(),
//                        userProfile.getTaxDepartment(),
//                        userProfile.getRepresentative(),
//                        DataUtil.formatDate(userProfile.getFoundingDate(), Constants.DateTimePattern.DMY, ""),
//                        userProfile.getBusinessType(),
//                        DataUtil.formatDate(userProfile.getUpdateAt(), Constants.DateTimePattern.DMY_HMS, ""),
//                        userProfile.getUpdateBy()
//                ));
//            }
//
//            return workbook;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * write workbook row
//     *
//     * @param row        workbook row
//     * @param startIndex start cell index
//     * @param rowData    row data
//     */
//    private void writeRow(Row row, CellStyle cellStyle, int startIndex, List<String> rowData) {
//        int cellIndex = startIndex;
//        for (String data : rowData) {
//            Cell cell = row.createCell(cellIndex++);
//            cell.setCellValue(data);
//            cell.setCellStyle(cellStyle);
//        }
//    }
//
//    private Flux<AreaDTO> getAreaName(Iterable<String> parents) {
//        log.info("list parents {}", parents);
//        return Flux.fromIterable(parents)
//                .flatMap(settingClient::getAreas)
//                .filter(res -> res.getData() != null)
//                .flatMap(res -> Flux.fromIterable(res.getData()))
//                .onErrorReturn(new AreaDTO());
//    }
//
//    @Override
//    public Mono<UserProfileDTO> getUserProfile(String id) {
//        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
//        return userRepository.findById(id).map(userProfile -> {
//            UserProfileDTO dto = new UserProfileDTO();
//            BeanUtils.copyProperties(userProfile, dto);
//            return dto;
//        }).doOnNext(userProfileDTO -> {
//            UserRepresentation representation = usersResource.get(userProfileDTO.getUserId()).toRepresentation();
//            String firstName = DataUtil.safeToString(representation.getFirstName(), "");
//            String lastName = DataUtil.safeToString(representation.getLastName(), "");
//            userProfileDTO.setName(lastName + " " + firstName);
//        });
//    }
//
//
////    @Override
////    public Mono<Account> findUserByIdNo(String idNo, String idType) {
////        return tenantIdentifyRepo.getIndividualIdByIdNoAndType(idNo, idType, AuthConstants.TenantType.ORGANIZATION)
////                .flatMap(orgId -> {
////                    var getOwnerAccount = individualRepository.findAccountByOrgId(orgId, AuthConstants.PositionCode.OWNER);
////                    var getTrustedIdNos = tenantIdentifyRepo.getTrustedIdNoByTenantId(orgId, AuthConstants.TenantType.ORGANIZATION).collectList();
////                    return Mono.zip(getOwnerAccount, getTrustedIdNos)
////                            .flatMap(tuple -> {
////                                var individual = tuple.getT1();
////                                var trustedIdNos = tuple.getT2();
////                                return productClient.getActiveTelecomService(trustedIdNos).flatMap(activeService -> {
////                                    log.info("Active telecom Services {}", activeService);
////                                    var account = buildAccountResponse(individual, activeService.get());
////                                    return Mono.just(account);
////                                });
////                            });
////                }).switchIfEmpty(Mono.empty());
////    }
//
////    private Account buildAccountResponse(Individual individual, DataResponse activeTelecomServices) {
////        Account account = new Account();
////        Customer customer = Customer.builder().id(individual.getId()).email(individual.getEmail())
////                .phoneNumber(individual.getPhone()).name(individual.getName()).username(individual.getUsername())
////                .build();
////        account.setCustomer(customer);
////        if (activeTelecomServices != null && activeTelecomServices.getData() != null) {
////            List<Map> telecomDTOS = (List<Map>) activeTelecomServices.getData();
////            List<ActiveTelecomService> activeServices = new ArrayList<>();
////            for (Map telecom : telecomDTOS
////            ) {
////                TelecomDTO convertTelecom = mapper.convertValue(telecom, TelecomDTO.class);
////                ActiveTelecomService activeTelecomService = ActiveTelecomService.builder().id(convertTelecom.getOriginId())
////                        .name(convertTelecom.getName()).build();
////                activeServices.add(activeTelecomService);
////            }
////            account.setActiveTelecomServices(activeServices);
////        }
////        account.setCustomer(customer);
////        return account;
////    }
//}
