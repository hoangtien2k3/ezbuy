package com.ezbuy.productservice.client.impl;

import static com.ezbuy.authmodel.constants.AuthConstants.Field.ORGANIZATION_ID;
import static com.ezbuy.ordermodel.constants.MessageConstant.AUTH_SERVICE_ERROR;

import com.ezbuy.authmodel.dto.response.GetActionLoginReportResponse;
import com.ezbuy.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.OrganizationUnit;
import com.ezbuy.productservice.client.AuthClient;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.ObjectMapperUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {

    private final BaseRestClient baseRestClient;

    @Qualifier("authClient")
    private final WebClient authClient;

    public static final String AUTH_ERROR = "auth.service.error";
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public Mono<List<TenantIdentifyDTO>> getTenantIdentify(String organizationId) {

        return SecurityUtils.getCurrentUser()
                .map(currentUser -> {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("userId", currentUser.getId());
                    params.add("organizationId", organizationId);
                    return params;
                })
                .flatMap(params -> baseRestClient.get(authClient, "/identify/idNo", null, params, DataResponse.class))
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(AUTH_ERROR)));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(AUTH_ERROR)));
                    }
                    List<TenantIdentifyDTO> tenantIdentifyDTOS = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> tenantIdentifyDTOS.add(
                            ObjectMapperFactory.getInstance().convertValue(x, TenantIdentifyDTO.class)));
                    return tenantIdentifyDTOS;
                })
                .doOnError(err -> log.error("Exception when call auth service api/identify/idNo: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_ERROR)));
    }

    @Override
    public Mono<List<String>> getUsersTrusted() {
        return baseRestClient
                .get(authClient, "/identify/trusted-idNo", null, null, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (DataUtil.isNullOrEmpty(respOptional)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(AUTH_ERROR)));
                    }

                    return respOptional.get().getData();
                })
                .doOnError(err -> log.error("Exception when call auth service api identify/trusted-idNo ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_ERROR)));
    }

    @Override
    public Mono<List<String>> getTrustedIdNoOrganization(String organizationId) {

        return SecurityUtils.getCurrentUser()
                .map(currentUser -> {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("organizationId", organizationId);
                    return params;
                })
                .flatMap(params -> baseRestClient.get(
                        authClient, "/identify/trusted-idNo-organization", null, params, DataResponse.class))
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    List<String> lstIdNo = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(
                            x -> lstIdNo.add(ObjectMapperFactory.getInstance().convertValue(x, String.class)));
                    return lstIdNo;
                })
                .doOnError(err ->
                        log.error("Exception when call auth service api/identify/trusted-idNo-organization: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "auth.service.error")));
    }

    /**
     * get idNo trusted have check isMember
     *
     * @param organizationId
     * @param userId
     * @return
     */
    @Override
    public Mono<List<String>> getTrustedIdNoOrganizationMember(String organizationId, String userId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("organizationId", organizationId);
        params.add("userId", userId);
        return baseRestClient
                .get(authClient, "/identify/trusted-idNo-organization-member", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    List<String> lstIdNo = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(
                            x -> lstIdNo.add(ObjectMapperFactory.getInstance().convertValue(x, String.class)));
                    return lstIdNo;
                })
                .doOnError(err -> log.error(
                        "Exception when call auth service api/identify/trusted-idNo-organization-member: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "auth.service.error")));
    }

    @Override
    public Mono<List<OrganizationUnit>> findAllActiveOrganizationUnitsByOrganizationId(
            String userId, String organizationId) {
        return SecurityUtils.getCurrentUser()
                .map(currentUser -> {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("userId", currentUser.getId());
                    params.add("organizationId", organizationId);
                    return params;
                })
                .flatMap(params -> baseRestClient.get(
                        authClient, "/organization/unit/user-organization", null, params, DataResponse.class))
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(AUTH_ERROR)));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(AUTH_ERROR)));
                    }
                    List<OrganizationUnit> organizationUnitList = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> organizationUnitList.add(
                            ObjectMapperFactory.getInstance().convertValue(x, OrganizationUnit.class)));
                    return organizationUnitList;
                })
                .doOnError(err ->
                        log.error("Exception when call auth service api /organization/unit/user-organization: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_ERROR)));
    }

    @Override
    public Mono<Optional<String>> getIdNoOrganization(String idType, String type, String tenantId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("idType", idType);
        params.add("type", type);
        params.add("tenantId", tenantId);
        return baseRestClient
                .get(authClient, "/identify/idNo-organization", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    String result = (String) dataResponseOptional.get().getData();
                    return Optional.ofNullable(result);
                })
                .switchIfEmpty(Mono.just(Optional.empty()))
                .onErrorResume(throwable -> {
                    log.error("call.api.auth.error");
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                });
    }

    @Override
    public Mono<Optional<GetActionLoginReportResponse>> getActionLoginReport(LocalDate dateReport) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dateReport", String.valueOf(dateReport));
        return baseRestClient
                .get(authClient, "/auth/action-login", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    String result = ObjectMapperUtil.convertObjectToJson(
                            dataResponseOptional.get().getData());
                    GetActionLoginReportResponse getActionLoginReportResponse =
                            objectMapperUtil.convertStringToObject(result, GetActionLoginReportResponse.class);
                    return Optional.ofNullable(getActionLoginReportResponse);
                })
                .switchIfEmpty(Mono.just(Optional.empty()))
                .onErrorResume(throwable -> {
                    log.error("call.api.auth.error");
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                });
    }

    @Override
    public Mono<Individual> findIndividualByUserIdAndOrganizationId(String userId, String organizationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", userId);
        params.add(ORGANIZATION_ID, organizationId);
        return baseRestClient
                .get(authClient, "/individual/user-and-org", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    ;
                    return ObjectMapperFactory.getInstance()
                            .convertValue(dataResponseOptional.get().getData(), Individual.class);
                })
                .doOnError(err -> log.error("Exception when call auth service /individual/user-id: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }
}
