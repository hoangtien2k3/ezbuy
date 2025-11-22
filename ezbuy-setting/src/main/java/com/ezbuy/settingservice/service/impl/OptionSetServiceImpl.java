package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.OptionSetDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingservice.model.entity.OptionSet;
import com.ezbuy.settingservice.model.entity.OptionSetValue;
import com.ezbuy.settingservice.model.dto.request.CreateOptionSetRequest;
import com.ezbuy.settingservice.model.dto.response.SearchOptionSetResponse;
import com.ezbuy.settingservice.repository.OptionSetRepository;
import com.ezbuy.settingservice.repository.OptionSetValueRepository;
import com.ezbuy.settingservice.repositoryTemplate.OptionSetRepositoryTemplate;
import com.ezbuy.settingservice.service.OptionSetService;
import com.ezbuy.core.cache.LocalCache;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionSetServiceImpl extends BaseServiceHandler implements OptionSetService {
    private final OptionSetRepository optionSetRepository;
    private final OptionSetRepositoryTemplate optionSetRepositoryTemplate;
    private final OptionSetValueRepository optionSetValueRepository;

    @Override
    @LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
    public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
        return optionSetValueRepository.findByOptionSetCode(optionSetCode).collectList();
    }

    @Override
    @LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
    public Mono<OptionSetValue> findByOptionSetCodeAndOptionValueCode(String optionSetCode, String optionValueCode) {
        return optionSetValueRepository.findByOptionSetCodeAndOptionValueCode(optionSetCode, optionValueCode);
    }

    @Override
    @Transactional
    public Mono<DataResponse<OptionSet>> createOptionSet(CreateOptionSetRequest request) {
        validateInput(request);
        var getSysDate = optionSetRepository.getSysDate();
        String code = DataUtil.safeTrim(request.getCode());
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))),
                        validateDuplicateCode(code),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT3();
                    String description = DataUtil.safeTrim(request.getDescription());
                    String userName = tuple.getT1().getUsername();
                    OptionSet optionSet = OptionSet.builder()
                            .id(UUID.randomUUID().toString())
                            .code(code)
                            .description(description)
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(userName)
                            .updateAt(now)
                            .updateBy(userName)
                            .isNew(true)
                            .build();
                    return optionSetRepository
                            .save(optionSet)
                            .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "option.set.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", optionSet)));
                });
    }

    public void validateInput(CreateOptionSetRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.option.set.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.option.set.code.max.length");
        }
        String description = DataUtil.safeTrim(request.getDescription());
        if (DataUtil.isNullOrEmpty(description)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.option.set.name");
        }
        if (description.length() > 1000) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.option.set.name.max.length");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateDuplicateCode(String code) {
        return optionSetRepository
                .findByCode(code)
                .defaultIfEmpty(new OptionSet())
                .flatMap(response -> {
                    if (!DataUtil.isNullOrEmpty(response.getCode())) {
                        return Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "create.option.set.code.is.exists"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<OptionSet>> editOptionSet(String id, CreateOptionSetRequest request) {
        validateInput(request);
        String optionSetId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(optionSetId)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "option.set.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))),
                        optionSetRepository
                                .getById(optionSetId)
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "option.set.not.found"))))
                .flatMap(tuple -> {
                    OptionSet optionSet = tuple.getT2();
                    Mono<Boolean> checkExistCode = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    if (!DataUtil.safeEqual(code, optionSet.getCode())) {
                        checkExistCode = validateDuplicateCode(code);
                    }
                    String description = DataUtil.safeTrim(request.getDescription());
                    return checkExistCode.flatMap(tuple2 -> optionSetRepository
                            .updateOptionSet(
                                    optionSet.getId(),
                                    code,
                                    description,
                                    request.getStatus(),
                                    tuple.getT1().getUsername())
                            .defaultIfEmpty(new OptionSet())
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    ErrorCode.INTERNAL_SERVER_ERROR, "option.set.update.failed")))
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null))));
                });
    }

    @Override
    @Transactional
    @LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
    public Mono<SearchOptionSetResponse> findOptionSet(SearchOptionSetRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if (!Objects.isNull(request.getFromDate()) && !Objects.isNull(request.getToDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        Flux<OptionSetDTO> lstOptionSetDTO = optionSetRepositoryTemplate.findOptionSet(request);
        Mono<Long> countMono = optionSetRepositoryTemplate.countOptionSet(request);
        return Mono.zip(lstOptionSetDTO.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchOptionSetResponse response = new SearchOptionSetResponse();
            response.setLstOptionSetDTO(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }
}
