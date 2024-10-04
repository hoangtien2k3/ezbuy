package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingmodel.model.OptionSet;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetResponse;
import com.ezbuy.settingservice.repository.OptionSetRepository;
import com.ezbuy.settingservice.repository.OptionSetValueRepository;
import com.ezbuy.settingservice.repositoryTemplate.OptionSetRepositoryTemplate;
import com.ezbuy.settingservice.service.OptionSetService;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;
import io.hoangtien2k3.reactify.aop.cache.Cache2L;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    @Cache2L(durationInMinute = 30)
    public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
        return optionSetValueRepository.findByOptionSetCode(optionSetCode).collectList();
    }

    @Override
    @Cache2L(durationInMinute = 30)
    public Mono<OptionSetValue> findByOptionSetCodeAndOptionValueCode(String optionSetCode, String optionValueCode) {
        return optionSetValueRepository.findByOptionSetCodeAndOptionValueCode(optionSetCode, optionValueCode);
    }

    @Override
    @Transactional
    public Mono<DataResponse<OptionSet>> createOptionSet(CreateOptionSetRequest request) {
        // validate input
        validateInput(request);
        var getSysDate = optionSetRepository.getSysDate();
        String code = DataUtil.safeTrim(request.getCode());
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateDuplicateCode(code),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT3();
                    String description = DataUtil.safeTrim(request.getDescription());
                    String userName = tuple.getT1().getUsername();
                    OptionSet optionSet = OptionSet.builder()
                            .code(code)
                            .description(description)
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(userName)
                            .updateAt(now)
                            .updateBy(userName)
                            .build();
                    return optionSetRepository
                            .save(optionSet)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "option.set.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", optionSet)));
                });
    }

    public void validateInput(CreateOptionSetRequest request) {
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
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateDuplicateCode(String code) {
        return optionSetRepository
                .findByCode(code)
                .defaultIfEmpty(new OptionSet())
                .flatMap(response -> {
                    if (!DataUtil.isNullOrEmpty(response.getCode())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.option.set.code.is.exists"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<OptionSet>> editOptionSet(String id, CreateOptionSetRequest request) {
        // validate input
        validateInput(request);
        String optionSetId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(optionSetId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "option.set.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        optionSetRepository
                                .getById(optionSetId)
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "option.set.not.found"))))
                .flatMap(tuple -> {
                    OptionSet optionSet = tuple.getT2();
                    Mono<Boolean> checkExistCode = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    // check trung code
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
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "option.set.update.failed")))
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null))));
                });
    }

    @Override
    @Transactional
    @Cache2L(durationInMinute = 30)
    public Mono<SearchOptionSetResponse> findOptionSet(SearchOptionSetRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        // validate tu ngay khong duoc lon hon den ngay
        if (!Objects.isNull(request.getFromDate()) && !Objects.isNull(request.getToDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        // tim kiem thong tin theo input
        Flux<OptionSetDTO> lstOptionSetDTO = optionSetRepositoryTemplate.findOptionSet(request);
        // lay tong so luong ban ghi
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
