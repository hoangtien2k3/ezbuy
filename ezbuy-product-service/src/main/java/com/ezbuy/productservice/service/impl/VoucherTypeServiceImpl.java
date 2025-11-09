package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.core.constants.Constants.STATUS.INACTIVE;

import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.dto.VoucherTypeV2DTO;
import com.ezbuy.productmodel.dto.request.SearchVoucherTypeRequest;
import com.ezbuy.productmodel.dto.request.VoucherTypeRequest;
import com.ezbuy.productmodel.dto.response.PaginationDTO;
import com.ezbuy.productmodel.dto.response.SearchVoucherTypeResponse;
import com.ezbuy.productmodel.model.VoucherType;
import com.ezbuy.productservice.repository.VoucherTypeRepository;
import com.ezbuy.productservice.repository.repoTemplate.VoucherTypeRepositoryTemplate;
import com.ezbuy.productservice.service.VoucherTypeService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.AppUtils;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.Translator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoucherTypeServiceImpl extends BaseServiceHandler implements VoucherTypeService {

    private final VoucherTypeRepository voucherTypeRepository;
    private final VoucherTypeRepositoryTemplate voucherTypeRepositoryTemplate;

    @Override
    public Mono<List<VoucherType>> getAll() {
        return voucherTypeRepository.findAllVoucherType().collectList();
    }

    @Override
    public Mono<DataResponse<List<VoucherType>>> getAllVoucherTypeActive() {
        return voucherTypeRepository
                .getAllVoucherTypeActive()
                .collectList()
                .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocale("Success"), response)));
    }

    @Override
    public Mono<DataResponse<VoucherType>> create(VoucherTypeRequest request) {
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistedCode(code, true, null))
                .flatMap(data -> {
                    String voucherTypeId = UUID.randomUUID().toString();
                    LocalDateTime now = LocalDateTime.now();
                    VoucherType newVoucherType = VoucherType.builder()
                            .id(voucherTypeId)
                            .code(code)
                            .createBy(data.getT1().getUsername())
                            .description(request.getDescription())
                            .createAt(now)
                            .state(request.getState())
                            .status(request.getStatus())
                            .name(request.getName())
                            .actionType(request.getActionType())
                            .actionValue(request.getActionValue())
                            .priorityLevel(request.getPriorityLevel())
                            .payment(request.getPayment())
                            .conditionUse(request.getConditionUse())
                            .isNew(true)
                            .build();
                    return AppUtils.insertData(voucherTypeRepository.save(newVoucherType))
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher-type.insert.failed")))
                            .flatMap(
                                    x -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), newVoucherType)));
                });
    }

    public Mono<Object> validateExistedCode(String code, Boolean isInsert, String id) {
        return voucherTypeRepository
                .findByCode(code)
                .defaultIfEmpty(new VoucherType())
                .map(voucherType -> {
                    if ((isInsert && voucherType.getCode() != null)
                            || (!isInsert
                                    && voucherType.getCode() != null
                                    && !DataUtil.safeEqual(voucherType.getId(), id))) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "voucher-type.validate.code.is.exist"));
                    }
                    return voucherType;
                });
    }

    private void validateInput(VoucherTypeRequest request) {
        Pattern p = Pattern.compile(Constants.VOUCHER_TYPE.CODE_REGEX);

        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.code.null");
        }
        if (code.length() > Constants.VOUCHER_TYPE.CODE_MAX_LENGTH) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.code.max.length");
        }
        Matcher containSpecialCharacterMatcher = p.matcher(code);
        if (containSpecialCharacterMatcher.find()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.code.special-character");
        }
        String description = DataUtil.safeTrim(request.getDescription());
        if (!DataUtil.isNullOrEmpty(description)) {
            if (description.length() > Constants.VOUCHER_TYPE.DESCRIPTION_MAX_LENGTH) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.description.max.length");
            }
        }
        Integer priorityLevel = request.getPriorityLevel();
        if (priorityLevel == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.priority.null");
        }
        if (priorityLevel < 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.code.priority.integer");
        }
        String actionValue = DataUtil.safeTrim(request.getActionValue());
        if (DataUtil.isNullOrEmpty(actionValue)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.action.value.null");
        }
        if (DataUtil.isNullOrEmpty(actionValue)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.name.null");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.status.null");
        }
        String actionType = DataUtil.safeTrim(request.getActionType());
        if (!actionType.equals(Constants.VOUCHER_TYPE.ACTION_TYPE_DISCOUNT)
                && !actionType.equals(Constants.VOUCHER_TYPE.ACTION_TYPE_FIXED)
                && !actionType.equals(Constants.VOUCHER_TYPE.ACTION_TYPE_ITEM)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher-type.validate.action.type");
        }
    }

    @Override
    public Mono<DataResponse<VoucherType>> update(String id, VoucherTypeRequest request) {
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());

        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistedCode(code, false, id))
                .flatMap(data -> {
                    VoucherType fetchedVoucherType = (VoucherType) data.getT2();
                    VoucherType updateVoucherType = VoucherType.builder()
                            .id(id)
                            .code(code)
                            .createBy(fetchedVoucherType.getCreateBy())
                            .description(request.getDescription())
                            .createAt(fetchedVoucherType.getCreateAt())
                            .createBy(fetchedVoucherType.getCreateBy())
                            .updateAt(LocalDateTime.now())
                            .updateBy(data.getT1().getUsername())
                            .state(request.getState())
                            .status(request.getStatus())
                            .name(request.getName())
                            .actionType(request.getActionType())
                            .actionValue(request.getActionValue())
                            .priorityLevel(request.getPriorityLevel())
                            .payment(request.getPayment())
                            .conditionUse(request.getConditionUse())
                            .build();
                    return voucherTypeRepository
                            .save(updateVoucherType)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher-type.update.failed")))
                            .flatMap(x ->
                                    Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), updateVoucherType)));
                });
    }

    @Override
    public Mono<SearchVoucherTypeResponse> search(SearchVoucherTypeRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);

        // validate bat buoc nhap tu ngay den ngay
        if ((Objects.isNull(request.getCreateFromDate()) && Objects.nonNull(request.getCreateToDate()))
                || (Objects.nonNull(request.getCreateFromDate()) && Objects.isNull(request.getCreateToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        // validate tu ngay khong duoc lon hon den ngay
        if (!Objects.isNull(request.getCreateFromDate()) && !Objects.isNull(request.getCreateToDate())) {
            if (request.getCreateFromDate().isAfter(request.getCreateToDate())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }

        return Mono.zip(
                        voucherTypeRepositoryTemplate.search(request).collectList(),
                        voucherTypeRepositoryTemplate.count(request))
                .map(zip -> {
                    PaginationDTO pagination = new PaginationDTO();
                    pagination.setPageIndex((request.getPageIndex()));
                    pagination.setPageSize(request.getPageSize());
                    pagination.setTotalRecords(zip.getT2());
                    SearchVoucherTypeResponse response = new SearchVoucherTypeResponse();
                    response.setListVoucherType(zip.getT1());
                    response.setPagination(pagination);
                    return response;
                });
    }

    @Override
    public Mono<DataResponse<VoucherType>> delete(String id) {
        String voucherTypeId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(id)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "vouchertype.validate.id.null");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        voucherTypeRepository
                                .getById(id) // lay thong tin voucherType theo id
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, "voucher-type.validate.find.by.id.null"))))
                .flatMap(tuple -> voucherTypeRepository
                        .updateStatus(voucherTypeId, INACTIVE, tuple.getT1().getUsername())
                        .defaultIfEmpty(new VoucherType())
                        .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null))));
    }

    @Override
    public Mono<DataResponse<List<VoucherTypeV2DTO>>> findVoucherTypeByVoucherCode(String voucherCode) {
        return voucherTypeRepositoryTemplate
                .findVoucherTypeByVoucherCodeUsed(voucherCode)
                .collectList()
                .map(voucherTypeList -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), voucherTypeList));
    }
}
