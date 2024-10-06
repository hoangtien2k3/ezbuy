package com.ezbuy.orderservice.client.impl;

import com.ezbuy.authmodel.dto.request.GetOrganizationByIndividualIdRequest;
import com.ezbuy.authmodel.dto.request.UpdateTenantTrustStatusRequest;
import com.ezbuy.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.authmodel.model.Organization;
import com.ezbuy.authmodel.model.TenantIdentify;
import com.ezbuy.orderservice.client.AuthClient;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.factory.ObjectMapperFactory;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static com.ezbuy.ordermodel.constants.MessageConstant.AUTH_SERVICE_ERROR;

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {

    private final BaseRestClient baseRestClient;

    @Qualifier("authClient")
    private final WebClient authClient;

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
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    List<TenantIdentifyDTO> tenantIdentifyDTOS = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> tenantIdentifyDTOS.add(ObjectMapperFactory.getInstance().convertValue(x, TenantIdentifyDTO.class)));
                    return tenantIdentifyDTOS;
                })
                .doOnError(err -> log.error("Exception when call auth service api/identify/idNo: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<Organization> getOrganizationByIndividualId(String individualId) {
        return SecurityUtils.getCurrentUser()
                .map(currentUser -> {
                    GetOrganizationByIndividualIdRequest params = new GetOrganizationByIndividualIdRequest();
                    params.setIndividualId(individualId);
                    return params;
                })
                .flatMap(params -> baseRestClient.post(authClient, "/organization/get-organization-by-individual-id", null, params, DataResponse.class))
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("organization.service.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("organization.service.error")));
                    }
                    if (DataUtil.isNullOrEmpty(dataResponseOptional.get().getErrorCode())) {
                        String dataJson = DataUtil.parseObjectToString(dataResponseOptional.get().getData());
                        try {
                            return ObjectMapperFactory.getInstance().readValue(dataJson, Organization.class);
                        } catch (Exception ex) {
                            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("organization.service.error")));
                        }
                    } else {
                        return Mono.error(new BusinessException(dataResponseOptional.get().getErrorCode(), dataResponseOptional.get().getMessage()));
                    }
                })
                .doOnError(err -> log.error("Exception when call auth service api/get-organization-by-individual-id: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "organization.service.error")));
    }

    @Override
    public Mono<List<String>> getTrustedIdNoOrganization(String organizationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("organizationId", organizationId);
        return baseRestClient.get(authClient, "/identify/trusted-idNo-organization", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    List<String> lstIdNo = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> lstIdNo.add(ObjectMapperFactory.getInstance().convertValue(x, String.class)));
                    return lstIdNo;
                })
                .doOnError(err -> log.error("Exception when call auth service api/identify/trusted-idNo-organization: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<String> findIndividualIdByUserIdAndOrganizationId(String userId, String organizationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", userId);
        params.add("organizationId", organizationId);
         return baseRestClient.get(authClient, "/individual/user-id", null, params, DataResponse.class)
                .map(dataResponse -> {
                           if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    return dataResponseOptional.get().getData();
                })
                .doOnError(err -> log.error("Exception when call auth service /individual/user-id: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<Optional<String>> findUserIdByIndividualIdAndActive(String individualId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("individualId", individualId);
        return baseRestClient.get(authClient, "/individual/user-id/individual-id", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    return Optional.of(dataResponseOptional.get().getData());
                })
                .doOnError(err -> log.error("Exception when call auth service /individual/user-id/individual-id: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<String> findIndividualIdByUserId(String userId, String organizationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", userId);
        params.add("organizationId", organizationId);
        return baseRestClient.get(authClient, "/individual/find-by-user-id", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    return dataResponseOptional.get().getData();
                })
                .doOnError(err -> log.error("Exception when call auth service /individual/user-id: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<String> updateTrustStatusByTenantId(UpdateTenantTrustStatusRequest request) {
        return baseRestClient.callPostBodyJson(authClient, "/identify/org-trust-status/tenant-id", null, request, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    return dataResponseOptional.get().getData();
                })
                .doOnError(err -> log.error("Exception when call auth service /identify/org-trust-status/tenant-id: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }

    @Override
    public Mono<List<TenantIdentify>>getByTypeAndTenantIdAndTrustStatus(String type, String tenantId, String trustStatus) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("type", type);
      params.add("tenantId", tenantId);
      params.add("trustStatus", trustStatus);
      return baseRestClient.get(authClient, "/identify/type-tenant-trust-status", null, params, DataResponse.class)
          .map(dataResponse->{
            if (DataUtil.isNullOrEmpty(dataResponse)) {
              return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
            }
            Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
            if (dataResponseOptional.isEmpty()) {
              return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
            }
            List<TenantIdentify> tenantIdentifies = new ArrayList<>();
            List list = (List) dataResponseOptional.get().getData();
            list.forEach(x -> tenantIdentifies.add(ObjectMapperFactory.getInstance().convertValue(x, TenantIdentify.class)));
            return tenantIdentifies;
          });
    }

    @Override
    public Mono<String> updateTrustStatusNotSign(UpdateTenantTrustStatusRequest request) {
        return baseRestClient.callPostBodyJson(authClient, "/identify/org-trust-status/not-sign", null, request, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    return dataResponseOptional.get().getData();
                })
                .doOnError(err -> log.error("Exception when call auth service /identify/org-trust-status/not-sign: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR)));
    }
}
