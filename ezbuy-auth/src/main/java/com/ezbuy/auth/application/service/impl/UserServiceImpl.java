package com.ezbuy.auth.application.service.impl;

import com.ezbuy.auth.application.dto.PaginationDTO;
import com.ezbuy.auth.application.dto.UserProfileDTO;
import com.ezbuy.auth.application.dto.request.QueryUserRequest;
import com.ezbuy.auth.application.dto.request.UpdateUserRequest;
import com.ezbuy.auth.application.dto.response.QueryUserResponse;
import com.ezbuy.auth.application.dto.response.UserContact;
import com.ezbuy.auth.domain.model.entity.UserProfileEntity;
import com.ezbuy.auth.infrastructure.config.KeycloakProvider;
import com.ezbuy.auth.infrastructure.repository.UserRepository;
import com.ezbuy.auth.application.service.UserService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Regex;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.ValidateUtils;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakProvider kcProvider;

    @Value("${keycloak.realm}")
    public String realm;

    @Override
    public Mono<Optional<UserProfileEntity>> getUserProfile() {
        return SecurityUtils.getCurrentUser().flatMap(currentUser -> userRepository
                .findById(currentUser.getId())
                .flatMap(user -> Mono.just(Optional.ofNullable(user)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found"))));
    }

    @Override
    public Mono<UserProfileEntity> update(UpdateUserRequest u) {
        if (!ValidateUtils.validateRegex(DataUtil.safeTrim(u.getPhone()), Regex.PHONE_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "user.phone.invalid"));
        }
        if (!ValidateUtils.validateRegex(DataUtil.safeTrim(u.getTaxCode()), Regex.TAX_CODE_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "user.taxCode.invalid"));
        }
        return Mono.zip(SecurityUtils.getCurrentUser(), userRepository.currentTimeDb())
                .flatMap(currentUser -> userRepository
                        .findById(currentUser.getT1().getId())
                        .map(userProfile -> {
                            userProfile.setCompanyName(DataUtil.safeTrim(u.getCompanyName()));
                            userProfile.setTaxCode(DataUtil.safeTrim(u.getTaxCode()));
                            userProfile.setTaxDepartment(DataUtil.safeTrim(u.getTaxDepartment()));
                            userProfile.setRepresentative(DataUtil.safeTrim(u.getRepresentative()));
                            userProfile.setFoundingDate(u.getFoundingDate());
                            userProfile.setBusinessType(DataUtil.safeTrim(u.getBusinessType()));
                            userProfile.setProvinceCode(DataUtil.safeTrim(u.getProvinceCode()));
                            userProfile.setDistrictCode(DataUtil.safeTrim(u.getDistrictCode()));
                            userProfile.setPrecinctCode(DataUtil.safeTrim(u.getPrecinctCode()));
                            userProfile.setPhone(DataUtil.safeTrim(u.getPhone()));
                            userProfile.setUpdateAt(currentUser.getT2());
                            userProfile.setUpdateBy(currentUser.getT1().getUsername());
                            return userProfile;
                        })
                        .flatMap(userRepository::save))
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "query.user.not.found")));
    }

    @Override
    public Flux<UserContact> getUserContacts(List<UUID> userIds) {
        if (userIds == null) {
            return Flux.just(new UserContact());
        }
        Set<UUID> unixUserIds = new HashSet<>(userIds);
        if (unixUserIds.size() > 100) {
            return Flux.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "array.limit"));
        }
        UsersResource usersResource = kcProvider.getRealmResource().users();
        return Flux.fromIterable(unixUserIds).map(userId -> mappingUserContract(userId, usersResource));
    }

    private UserContact mappingUserContract(UUID userId, UsersResource usersResource) {
        try {
            String email =
                    usersResource.get(userId.toString()).toRepresentation().getEmail();
            return new UserContact(userId, email);
        } catch (Exception ex) {
            log.error("Get UserResource error {}", userId, ex);
        }
        return new UserContact(userId, null);
    }

    @Override
    public Mono<Optional<UserProfileEntity>> getUserById(String id) {
        return userRepository
                .findById(id)
                .flatMap(user -> Mono.just(Optional.ofNullable(user)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    /**
     * query page user profile
     *
     * @param request
     *            query params
     * @return page user profile
     */
    @Override
    public Mono<QueryUserResponse> queryUserProfile(QueryUserRequest request) {
        int size = DataUtil.safeToInt(request.getPageSize(), 20);
        if (size <= 0 || size > 500) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "size.invalid"));
        }

        int page = DataUtil.safeToInt(request.getPageIndex(), 1);
        if (page <= 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "page.invalid"));
        }

        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
        request.setSort(sort);
        return findKeycloakUser(request.getName()).collectList().flatMap(kcUsers -> {
            if (DataUtil.isNullOrEmpty(kcUsers)) {
                QueryUserResponse emptyResponse = new QueryUserResponse();
                emptyResponse.setContent(Collections.emptyList());
                emptyResponse.setPagination(PaginationDTO.builder()
                        .pageIndex(1)
                        .pageSize(size)
                        .totalRecords(0L)
                        .build());
                return Mono.just(emptyResponse);
            }
            if (!DataUtil.isNullOrEmpty(request.getName())) {
                request.setUserIds(
                        kcUsers.stream().map(UserRepresentation::getId).collect(Collectors.toList()));
            }
            Flux<UserProfileDTO> userProfileFlux = userRepository
                    .queryUserProfile(request)
                    .doOnNext(userProf -> kcUsers.stream()
                            .filter(element -> element.getId().equals(userProf.getUserId()))
                            .findFirst()
                            .ifPresent(element -> userProf.setName(getFullName(element))));
            Mono<Long> countMono = userRepository.countUserProfile(request);
            return Mono.zip(userProfileFlux.collectList(), countMono).map(zip -> {
                PaginationDTO pagination = new PaginationDTO();
                pagination.setPageSize(size);
                pagination.setPageIndex(page);
                pagination.setTotalRecords(zip.getT2());

                QueryUserResponse queryUserResponse = new QueryUserResponse();
                queryUserResponse.setContent(zip.getT1());
                queryUserResponse.setPagination(pagination);

                return queryUserResponse;
            });
        });
    }

    private String getFullName(UserRepresentation userRepresentation) {
        return DataUtil.safeToString(userRepresentation.getLastName()) + " "
                + DataUtil.safeToString(userRepresentation.getFirstName());
    }

    /**
     * query keycloak user
     *
     * @param name filter by name
     */
    public Flux<UserRepresentation> findKeycloakUser(String name) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        return Flux.fromIterable(usersResource.list()).filter(userRepresentation -> {
            String fullName = getFullName(userRepresentation);
            return DataUtil.isNullOrEmpty(name) || fullName.toLowerCase().contains(name.trim().toLowerCase());
        });
    }

    @Override
    public Mono<UserProfileDTO> getUserProfile(String id) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        return userRepository
                .findById(String.valueOf(UUID.fromString(id)))
                .map(userProfile -> {
                    UserProfileDTO dto = new UserProfileDTO();
                    BeanUtils.copyProperties(userProfile, dto);
                    return dto;
                })
                .doOnNext(userProfileDTO -> {
                    UserRepresentation representation =
                            usersResource.get(userProfileDTO.getUserId()).toRepresentation();
                    String firstName = DataUtil.safeToString(representation.getFirstName(), "");
                    String lastName = DataUtil.safeToString(representation.getLastName(), "");
                    userProfileDTO.setName(lastName + " " + firstName);
                });
    }

    @Override
    public Mono<UserRepresentation> getEmailsByKeycloakUsername(String username) {
        RealmResource resource = kcProvider.getInstance().realm(realm);
        return Flux.fromIterable(resource.users().search(username))
                .collectList()
                .mapNotNull(userRepresentations -> userRepresentations.stream()
                        .filter(userRepresentation ->
                                userRepresentation.getUsername().trim().equals(username))
                        .findFirst()
                        .orElse(null));
    }
}
