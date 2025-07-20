package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.Setting;
import com.ezbuy.settingmodel.request.CreateSettingRequest;
import com.ezbuy.settingmodel.request.SearchSettingRequest;
import com.ezbuy.settingmodel.response.SearchSettingResponse;
import com.ezbuy.settingservice.repository.SettingRepository;
import com.ezbuy.settingservice.repositoryTemplate.SettingRepositoryTemplate;
import com.ezbuy.settingservice.service.SettingService;
import com.reactify.annotations.LocalCache;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.model.TokenUser;
import com.reactify.model.response.DataResponse;
import com.reactify.util.AppUtils;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final SettingRepositoryTemplate settingRepositoryTemplate;

    @Override
    @LocalCache(durationInMinute = 30)
    public Mono<String> findByCode(String code) {
        return settingRepository.findByCode(code);
    }

    @Override
    public Mono<SearchSettingResponse> searchSetting(SearchSettingRequest request) {
        int pageIndex = DataUtil.safeToInt(request.getPageIndex(), 1);
        int pageSize = DataUtil.safeToInt(request.getPageSize(), 10);
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();

        if (pageIndex < 1) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.pageIndex.invalid"));
        }
        if (pageSize > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.pageSize.invalid"));
        }
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        if (request.getCode().length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.code.max.length");
        }
        if ((Objects.isNull(fromDate) && Objects.nonNull(toDate))
                || (Objects.nonNull(fromDate) && Objects.isNull(toDate))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        if (!Objects.isNull(fromDate) && fromDate.isAfter(toDate)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
        }

        return Mono.zip(
                        settingRepositoryTemplate
                                .searchSettingByRequest(request)
                                .collectList(),
                        settingRepositoryTemplate.count(request))
                .map(zip -> {
                    PaginationDTO pagination = new PaginationDTO();
                    pagination.setPageIndex((request.getPageIndex()));
                    pagination.setPageSize(request.getPageSize());
                    pagination.setTotalRecords(zip.getT2());
                    SearchSettingResponse response = new SearchSettingResponse();
                    response.setSettingPage(zip.getT1());
                    response.setPagination(pagination);
                    return response;
                });
    }

    @Override
    public Mono<List<Setting>> getAllSetting() {
        return settingRepository.findAllSetting().collectList();
    }

    @Override
    public Mono<List<Setting>> getAllActiveSetting() {
        return settingRepository.getAllActiveSetting().collectList().map(list -> list);
    }

    @Override
    public Mono<DataResponse<Setting>> createSetting(CreateSettingRequest request) {

        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .defaultIfEmpty(new TokenUser()), // TODO: dong comment ham nay khi chay
                        // .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND,
                        // "user.null"))), //TODO: mo comment ham nay khi chay
                        validateExistCode(code, true, null))
                .flatMap(data -> {
                    String settingId = UUID.randomUUID().toString();
                    LocalDateTime now = LocalDateTime.now();
                    Setting newSetting = Setting.builder()
                            .id(settingId)
                            .code(code)
                            .value(DataUtil.safeTrim(request.getValue()))
                            .description(DataUtil.safeTrim(request.getDescription()))
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(data.getT1().getUsername())
                            .updateAt(now)
                            .isNew(true)
                            .build();
                    return AppUtils.insertData(settingRepository.save(newSetting))
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "setting.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("cuccess", newSetting)));
                });
    }

    @Override
    public Mono<DataResponse<Setting>> updateSetting(String id, CreateSettingRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());

        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                // .switchIfEmpty(Mono.error(new
                                // BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))), //TODO: mo
                                // comment ham
                                // nay khi chay
                                .switchIfEmpty(Mono.just(new TokenUser())), // TODO: comment lai dong nay khi chay
                        validateExistCode(code, false, id))
                .flatMap(data -> settingRepository
                        .updateSetting(
                                id,
                                code,
                                DataUtil.safeTrim(request.getValue()),
                                DataUtil.safeTrim(request.getDescription()),
                                request.getStatus(),
                                data.getT1().getUsername())
                        .defaultIfEmpty(new Setting())
                        .switchIfEmpty(Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "setting.update.failed")))
                        .flatMap(x -> Mono.just(new DataResponse<>("cuccess", null))));
    }

    @Override
    public Mono<DataResponse<Setting>> deleteSetting(String id) {
        String settingId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(settingId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.id.null");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                // .switchIfEmpty(Mono.error(new
                                // BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                                .switchIfEmpty(Mono.just(new TokenUser())),
                        settingRepository
                                .getById(id) // lay thong tin setting theo id
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, "setting.validate.find.by.id.null"))))
                .flatMap(tuple -> settingRepository
                        .updateStatus(
                                settingId,
                                Constants.Activation.INACTIVE,
                                tuple.getT1().getUsername())
                        .defaultIfEmpty(new Setting())
                        .flatMap(response -> Mono.just(new DataResponse<>("cuccess", null))));
    }

    private void validateInput(CreateSettingRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.code.null");
        }
        if (code.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.code.max.length");
        }
        String description = DataUtil.safeTrim(request.getDescription());
        if (DataUtil.isNullOrEmpty(description)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.description.null");
        }
        if (description.length() > 1000) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.description.max.length");
        }
        String value = DataUtil.safeTrim(request.getValue());
        if (DataUtil.isNullOrEmpty(value)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.value.null");
        }
        if (value.length() > 2000) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.value.max.length");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.status.error");
        }
    }

    public Mono<Boolean> validateExistCode(String code, Boolean isInsert, String id) {
        return settingRepository.getByCode(code).defaultIfEmpty(new Setting()).flatMap(settingByCode -> {
            if ((isInsert && settingByCode.getCode() != null)
                    || (!isInsert
                            && settingByCode.getCode() != null
                            && !DataUtil.safeEqual(settingByCode.getId(), id))) {
                return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "setting.validate.code.is.exist"));
            }
            return Mono.just(true);
        });
    }
}
