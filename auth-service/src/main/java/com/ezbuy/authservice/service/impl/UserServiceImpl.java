package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.dto.UserProfileDTO;
import com.ezbuy.authmodel.dto.request.QueryUserRequest;
import com.ezbuy.authmodel.dto.request.UpdateUserRequest;
import com.ezbuy.authmodel.dto.response.QueryUserResponse;
import com.ezbuy.authmodel.dto.response.UserContact;
import com.ezbuy.authmodel.model.UserProfile;
import com.ezbuy.authservice.client.SettingClient;
import com.ezbuy.authservice.config.KeycloakProvider;
import com.ezbuy.authservice.repository.UserRepository;
import com.ezbuy.authservice.service.UserService;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.constants.Regex;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.ValidateUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakProvider kcProvider;
    private final SettingClient settingClient;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${minio.bucket}")
    public String mySignBucket;

    @Override
    public Mono<Optional<UserProfile>> getUserProfile() {
        return SecurityUtils.getCurrentUser().flatMap(currentUser -> userRepository
                .findById(currentUser.getId())
                .flatMap(user -> Mono.just(Optional.ofNullable(user)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found"))));
    }

    @Override
    public Mono<UserProfile> update(UpdateUserRequest u) {
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
    public Mono<Optional<UserProfile>> getUserById(String id) {
        return userRepository
                .findById(id)
                .flatMap(user -> Mono.just(Optional.ofNullable(user)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    private File transferToFile(byte[] bytes) {
        try {
            File destFile = new File(UUID.randomUUID().toString());
            org.apache.commons.io.FileUtils.writeByteArrayToFile(destFile, bytes);
            return destFile;
        } catch (Exception e) {
            log.error("Upload file error:", e);
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "create.file.error");
        }
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
                    .doOnNext(userProf -> {
                        kcUsers.stream()
                                .filter(element -> element.getId().equals(userProf.getUserId()))
                                .findFirst()
                                .ifPresent(element -> {
                                    userProf.setName(getFullName(element));
                                });
                    });
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
     * @param name
     *            filter by name
     * @return
     */
    public Flux<UserRepresentation> findKeycloakUser(String name) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        return Flux.fromIterable(usersResource.list()).filter(userRepresentation -> {
            String fullName = getFullName(userRepresentation);
            return DataUtil.isNullOrEmpty(name)
                    || fullName.toLowerCase().contains(name.trim().toLowerCase());
        });
    }

    /**
     * fill missing data for user profile from keycloak
     *
     * @param userProfile
     *            user profile dto
     * @param usersResource
     *            keycloak user resource
     */
    private void fillDataUserFromKeycloak(UserProfileDTO userProfile, UsersResource usersResource) {
        try {
            UserRepresentation userRepresentation =
                    usersResource.get(userProfile.getUserId()).toRepresentation();
            if (userRepresentation != null) {
                String firstName = DataUtil.safeToString(userRepresentation.getFirstName(), "");
                String lastName = DataUtil.safeToString(userRepresentation.getLastName(), "");
                userProfile.setName(lastName + " " + firstName);
            }
        } catch (Exception ex) {
            log.warn("get user keycloak error ", ex);
        }
    }

    /**
     * export excel user profile
     *
     * @param request
     *            query params
     * @return excel file resource
     */
    public Mono<Resource> exportUser(QueryUserRequest request) {
        request.setPageIndex(null);
        request.setPageSize(null);
        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
        request.setSort(sort);
        return findKeycloakUser(request.getName())
                .collectList()
                .flatMap(kcUsers -> {
                    if (DataUtil.isNullOrEmpty(kcUsers)) {
                        return Mono.just(Collections.emptyList());
                    }
                    if (!DataUtil.isNullOrEmpty(request.getName())) {
                        List<String> userIds =
                                kcUsers.stream().map(UserRepresentation::getId).collect(Collectors.toList());
                        request.setUserIds(userIds);
                    }
                    return userRepository
                            .queryUserProfile(request)
                            .doOnNext(userProf -> {
                                kcUsers.stream()
                                        .filter(element -> element.getId().equals(userProf.getUserId()))
                                        .findFirst()
                                        .ifPresent(userRepresentation -> {
                                            userProf.setName(getFullName(userRepresentation));
                                        });
                            })
                            .collectList();
                })
                .flatMap(userProfiles -> writeUserProfiles((List<UserProfileDTO>) userProfiles))
                .flatMap(workbook -> Mono.fromCallable(() -> writeExcel(workbook)));
    }

    private ByteArrayResource writeExcel(Workbook workbook) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook) {
            workbook.write(os);
            return new ByteArrayResource(os.toByteArray()) {
                @Override
                public String getFilename() {
                    return "List_User_Profile.xlsx";
                }
            };
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "export.error");
        }
    }

    /**
     * write user profile list to workbook
     *
     * @param userProfiles
     *            user profile list
     * @return workbook
     */
    private Mono<Workbook> writeUserProfiles(List<UserProfileDTO> userProfiles) {
        Set<String> parents = new HashSet<>();
        parents.add("");
        for (UserProfileDTO userProfile : userProfiles) {
            if (userProfile.getProvinceCode() == null || userProfile.getDistrictCode() == null) {
                continue;
            }
            parents.add(userProfile.getProvinceCode());
            parents.add(userProfile.getProvinceCode() + userProfile.getDistrictCode());
        }
        return getAreaName(parents).collectList().flatMap(areas -> {
            log.info("areas : {}", areas);
            return Mono.fromCallable(() -> {
                return writeListUser(userProfiles, areas);
            });
        });
    }

    private Workbook writeListUser(List<UserProfileDTO> userProfiles, List<AreaDTO> areas) {
        try (InputStream templateInputStream =
                new ClassPathResource("template/template_export_user.xlsx").getInputStream()) {
            Workbook workbook = new XSSFWorkbook(templateInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            String currentDate = DataUtil.formatDate(LocalDateTime.now(), Constants.DateTimePattern.DMY, "");
            Cell exportDateCell = sheet.getRow(1).getCell(12);
            exportDateCell.setCellValue(exportDateCell.getStringCellValue().replace("${date}", currentDate));
            DataUtil.formatDate(LocalDateTime.now(), Constants.DateTimePattern.DMY_HMS, "");

            int rowCount = 4;
            int index = 1;
            for (UserProfileDTO userProfile : userProfiles) {
                Row row = sheet.createRow(rowCount++);

                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderRight(BorderStyle.THIN);
                style.setRightBorderColor(IndexedColors.BLUE.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderLeft(BorderStyle.THIN);
                style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

                String provinceName = areas.stream()
                        .filter(area -> area.getAreaCode().equals(userProfile.getProvinceCode()))
                        .map(AreaDTO::getName)
                        .findFirst()
                        .orElse(null);
                String districtName = areas.stream()
                        .filter(area -> area.getAreaCode()
                                .equals(userProfile.getProvinceCode() + userProfile.getDistrictCode()))
                        .map(AreaDTO::getName)
                        .findFirst()
                        .orElse(null);
                String precinctName = areas.stream()
                        .filter(area -> area.getAreaCode()
                                .equals(userProfile.getProvinceCode()
                                        + userProfile.getDistrictCode()
                                        + userProfile.getPrecinctCode()))
                        .map(AreaDTO::getName)
                        .findFirst()
                        .orElse(null);
                writeRow(
                        row,
                        style,
                        0,
                        Arrays.asList(
                                String.valueOf(index++),
                                userProfile.getName(),
                                userProfile.getPhone(),
                                userProfile.getTaxCode(),
                                provinceName,
                                districtName,
                                precinctName,
                                userProfile.getStreetBlock(),
                                DataUtil.formatDate(userProfile.getCreateAt(), Constants.DateTimePattern.DMY_HMS, ""),
                                userProfile.getCreateBy(),
                                userProfile.getCompanyName(),
                                userProfile.getTaxDepartment(),
                                userProfile.getRepresentative(),
                                DataUtil.formatDate(userProfile.getFoundingDate(), Constants.DateTimePattern.DMY, ""),
                                userProfile.getBusinessType(),
                                DataUtil.formatDate(userProfile.getUpdateAt(), Constants.DateTimePattern.DMY_HMS, ""),
                                userProfile.getUpdateBy()));
            }

            return workbook;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * write workbook row
     *
     * @param row
     *            workbook row
     * @param startIndex
     *            start cell index
     * @param rowData
     *            row data
     */
    private void writeRow(Row row, CellStyle cellStyle, int startIndex, List<String> rowData) {
        int cellIndex = startIndex;
        for (String data : rowData) {
            Cell cell = row.createCell(cellIndex++);
            cell.setCellValue(data);
            cell.setCellStyle(cellStyle);
        }
    }

    private Flux<AreaDTO> getAreaName(Iterable<String> parents) {
        log.info("list parents {}", parents);
        return Flux.fromIterable(parents)
                .flatMap(settingClient::getAreas)
                .filter(res -> res.getData() != null)
                .flatMap(res -> Flux.fromIterable(res.getData()))
                .onErrorReturn(new AreaDTO());
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
