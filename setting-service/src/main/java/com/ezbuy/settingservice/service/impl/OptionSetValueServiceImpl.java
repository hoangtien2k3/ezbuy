package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetValueRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetValueResponse;
import com.ezbuy.settingservice.repository.OptionSetValueRepository;
import com.ezbuy.settingservice.repositoryTemplate.OptionSetValueRepositoryTemplate;
import com.ezbuy.settingservice.service.OptionSetValueService;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.aop.cache.Cache2L;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OptionSetValueServiceImpl extends BaseServiceHandler implements OptionSetValueService {
    private final OptionSetValueRepository optionSetValueRepository;
    private final OptionSetValueRepositoryTemplate optionSetValueRepositoryTemplate;

    @Override
    @Transactional
    public Mono<DataResponse<OptionSetValue>> createOptionSetValue(CreateOptionSetValueRequest request) {
        // validate input
        validateInput(request);
        var getSysDate = optionSetValueRepository.getSysDate();
        String code = DataUtil.safeTrim(request.getCode());
        Long optionSetId = request.getOptionSetId();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateDuplicateCode(code, optionSetId),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT3();
                    String value = DataUtil.safeTrim(request.getValue());
                    String description = DataUtil.safeTrim(request.getDescription());
                    String userName = tuple.getT1().getUsername();
                    OptionSetValue optionSetValue = OptionSetValue.builder()
                            .optionSetId(request.getOptionSetId())
                            .code(code)
                            .value(value)
                            .description(description)
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(userName)
                            .updateAt(now)
                            .updateBy(userName)
                            .isNew(true)
                            .build();
                    return optionSetValueRepository
                            .save(optionSetValue)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "option.set.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", optionSetValue)));
                });
    }

    public void validateInput(CreateOptionSetValueRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.code.max.length");
        }
        String description = DataUtil.safeTrim(request.getDescription());
        if (DataUtil.isNullOrEmpty(description)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.name");
        }
        if (description.length() > 1000) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.name.max.length");
        }
        String value = DataUtil.safeTrim(request.getValue());
        if (DataUtil.isNullOrEmpty(value)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.value.value.null");
        }
        String optionSetId = DataUtil.safeTrim(request.getOptionSetId());
        if (DataUtil.isNullOrEmpty(optionSetId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.option.set.value.option.set.id.null");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateDuplicateCode(String code, Long optionSetId) {
        return optionSetValueRepository
                .findByCodeAndOptionSetId(code, optionSetId)
                .defaultIfEmpty(new OptionSetValue())
                .flatMap(response -> {
                    if (!DataUtil.isNullOrEmpty(response.getCode())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.option.set.code.is.exists"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Cache2L(durationInMinute = 30)
    public Mono<List<OptionSetValueDTO>> getAllActiveDataPolicyConfigByOptionSetCode(String optionSetCode) {
        // lay danh sach dieu khoan tu DB
        return optionSetValueRepository
                .findAllActiveOptionSetValueByOptionSetCode(optionSetCode)
                .collectList()
                .map(optionSetList -> {
                    if (DataUtil.isNullOrEmpty(optionSetList)) {
                        return Collections.emptyList();
                    }
                    // map data tu entity sang dto
                    List<OptionSetValueDTO> resultList = new ArrayList<>();
                    optionSetList.forEach(optionSet -> {
                        OptionSetValueDTO optionSetValueDTO;
                        optionSetValueDTO = !DataUtil.isNullOrEmpty(optionSet.getValue())
                                ? DataUtil.parseStringToObject(optionSet.getValue(), OptionSetValueDTO.class)
                                : new OptionSetValueDTO();
                        if (optionSetValueDTO == null) {
                            optionSetValueDTO = new OptionSetValueDTO();
                        }
                        optionSetValueDTO.setError(false);
                        optionSetValueDTO.setChecked(false);
                        optionSetValueDTO.setCode(optionSet.getCode());
                        resultList.add(optionSetValueDTO);
                    });
                    return resultList;
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<OptionSetValue>> editOptionSetValue(String id, CreateOptionSetValueRequest request) {
        // validate input
        validateInput(request);
        String optionSetValueId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(optionSetValueId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "option.set.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        optionSetValueRepository
                                .getById(optionSetValueId)
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "option.set.not.found"))))
                .flatMap(tuple -> {
                    OptionSetValue optionSetValue = tuple.getT2();
                    Mono<Boolean> checkExistCode = Mono.just(true);
                    String value = DataUtil.safeTrim(request.getValue());
                    String code = DataUtil.safeTrim(request.getCode());
                    // check trung code
                    if (!DataUtil.safeEqual(code, optionSetValue.getCode())) {
                        checkExistCode = validateDuplicateCode(code, optionSetValue.getOptionSetId());
                    }
                    String description = DataUtil.safeTrim(request.getDescription());
                    return checkExistCode.flatMap(tuple2 -> optionSetValueRepository
                            .updateOptionSetValue(
                                    optionSetValue.getId(),
                                    code,
                                    value,
                                    description,
                                    request.getStatus(),
                                    tuple.getT1().getUsername())
                            .defaultIfEmpty(new OptionSetValue())
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null))));
                });
    }

    @Override
    @Transactional
    public Mono<SearchOptionSetValueResponse> findOptionSetValueByOptionSetId(SearchOptionSetValueRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        // tim kiem thong tin theo input
        Flux<OptionSetValueDTO> lstOptionSetValueDTO = optionSetValueRepositoryTemplate.findOptionSetValue(request);
        // lay tong so luong ban ghi
        Mono<Long> countMono = optionSetValueRepositoryTemplate.countOptionSetValue(request);
        return Mono.zip(lstOptionSetValueDTO.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchOptionSetValueResponse response = new SearchOptionSetValueResponse();
            response.setLstOptionSetValueDTO(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    public Mono<List<OptionSetValue>> getLstAcronymByAliases(String code, List<String> serviceAliases) {
        if (DataUtil.isNullOrEmpty(serviceAliases)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("license.key.service.alias.empty")));
        }
        return getAllActiveOptionSetValueByOptionSetCode(code)
                .flatMap(result -> Mono.just(result.stream()
                        .filter(x -> serviceAliases.contains(x.getCode()))
                        .collect(Collectors.toList())));
    }

    @Override
    @Transactional
    @Cache2L(durationInMinute = 30)
    public Mono<List<OptionSetValue>> getAllActiveOptionSetValueByOptionSetCode(String optionSetCode) {
        return optionSetValueRepository
                .findAllActiveOptionSetValueByOptionSetCode(optionSetCode)
                .collectList()
                .map(optionSetList -> {
                    if (DataUtil.isNullOrEmpty(optionSetList)) {
                        return Collections.emptyList();
                    }
                    return optionSetList;
                });
    }
}
