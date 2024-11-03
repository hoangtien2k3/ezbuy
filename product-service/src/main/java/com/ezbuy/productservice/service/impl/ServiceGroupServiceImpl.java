package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.productmodel.dto.ServiceGroupDTO;
import com.ezbuy.productmodel.model.ServiceGroup;
import com.ezbuy.productmodel.request.CreateServiceGroupRequest;
import com.ezbuy.productmodel.request.SearchServiceGroupRequest;
import com.ezbuy.productmodel.response.PaginationDTO;
import com.ezbuy.productmodel.response.SearchServiceGroupResponse;
import com.ezbuy.productservice.repository.ServiceGroupRepository;
import com.ezbuy.productservice.repository.repoTemplate.ServiceGroupCustomerRepo;
import com.ezbuy.productservice.service.ServiceGroupService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ServiceGroupServiceImpl extends BaseServiceHandler implements ServiceGroupService {
    private final ServiceGroupRepository serviceGroupRepository;
    private final ServiceGroupCustomerRepo serviceGroupCustomerRepo;

    @Override
    @Transactional
    public Mono<DataResponse<ServiceGroup>> createServiceGroup(CreateServiceGroupRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Integer displayOrder = request.getDisplayOrder();
        var getSysDate = serviceGroupRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistServiceGroup(code, displayOrder),
                        getSysDate)
                .flatMap(
                        tuple -> { // validate ton tai thong
                            // tin code hoac
                            // displayOrder
                            String serviceGroupId = UUID.randomUUID().toString();
                            LocalDateTime now = tuple.getT3();
                            String name = DataUtil.safeTrim(request.getName());
                            String userName = tuple.getT1().getUsername();
                            ServiceGroup serviceGroup = ServiceGroup.builder()
                                    .id(serviceGroupId)
                                    .code(code)
                                    .name(name)
                                    .displayOrder(displayOrder)
                                    .status(request.getStatus())
                                    .createAt(now)
                                    .createBy(userName)
                                    .updateAt(now)
                                    .updateBy(userName)
                                    .build();
                            return serviceGroupRepository
                                    .save(serviceGroup)
                                    .switchIfEmpty(Mono.error(new BusinessException(
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, "service.group.insert.failed")))
                                    .flatMap(x -> Mono.just(
                                            new DataResponse<>(Translator.toLocaleVi(SUCCESS), serviceGroup)));
                        });
    }

    @Override
    @Transactional
    public Mono<DataResponse<ServiceGroup>> editServiceGroup(String id, CreateServiceGroupRequest request) {
        // validate input
        validateInput(request);
        String serviceGroupId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(serviceGroupId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.group.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        serviceGroupRepository
                                .getById(serviceGroupId) // lay thong tin serviceGroup theo id
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "service.group.not.found"))))
                .flatMap(tuple -> {
                    ServiceGroup serviceGroup = tuple.getT2();
                    Mono<Boolean> checkExistGroupCode = Mono.just(true);
                    Mono<Boolean> checkExistGroupOrder = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    // check trung code
                    if (!DataUtil.safeEqual(code, serviceGroup.getCode())) {
                        checkExistGroupCode = validateExistServiceGroup(code, null);
                    }
                    // check trung displayOrder
                    Integer displayOrder = request.getDisplayOrder();
                    if (!DataUtil.safeEqual(displayOrder, serviceGroup.getDisplayOrder())) {
                        checkExistGroupOrder = validateExistServiceGroup(null, displayOrder);
                    }
                    // thuc hien cap nhat
                    String name = DataUtil.safeTrim(request.getName());
                    Mono<ServiceGroup> updateServiceGroup = serviceGroupRepository
                            .updateServiceGroup(
                                    serviceGroupId,
                                    code,
                                    name,
                                    displayOrder,
                                    request.getStatus(),
                                    tuple.getT1().getUsername())
                            .defaultIfEmpty(new ServiceGroup());
                    return Mono.zip(checkExistGroupCode, checkExistGroupOrder, updateServiceGroup)
                            .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null)));
                });
    }

    @Override
    @Transactional
    public Mono<SearchServiceGroupResponse> findServiceGroup(SearchServiceGroupRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        // validate bat buoc nhap tu ngay den ngay
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        // validate tu ngay khong duoc lon hon den ngay
        if (!Objects.isNull(request.getFromDate()) && !Objects.isNull(request.getToDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        // tim kiem thong tin theo input
        Flux<ServiceGroupDTO> serviceGroups = serviceGroupCustomerRepo.findServiceGroup(request);
        // lay tong so luong ban ghi
        Mono<Long> countMono = serviceGroupCustomerRepo.countServiceGroup(request);
        return Mono.zip(serviceGroups.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchServiceGroupResponse response = new SearchServiceGroupResponse();
            response.setLstServiceGroupDTO(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    /**
     * validate input
     *
     * @param request
     */
    public void validateInput(CreateServiceGroupRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.code.max.length");
        }
        String name = DataUtil.safeTrim(request.getName());
        if (DataUtil.isNullOrEmpty(name)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.name");
        }
        if (name.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.name.max.length");
        }
        Integer displayOrder = request.getDisplayOrder();
        if (displayOrder == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.order");
        }
        if (displayOrder < 1) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.group.order.min");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    /**
     * check trung code hoac displayOrder
     *
     * @param code
     *            ma nhom dich vu
     * @param displayOrder
     *            thu tu hien thi
     * @return
     */
    public Mono<Boolean> validateExistServiceGroup(String code, Integer displayOrder) {
        return Mono.zip(
                        serviceGroupRepository.getByGroupCode(code).defaultIfEmpty(new ServiceGroup()),
                        serviceGroupRepository.getByGroupOrder(displayOrder).defaultIfEmpty(new ServiceGroup()))
                .flatMap(tuple -> {
                    ServiceGroup serviceGroupByGroupCode = tuple.getT1();
                    if (serviceGroupByGroupCode.getCode() != null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "create.service.group.code.is.exists"));
                    }
                    ServiceGroup serviceGroupByGroupOrder = tuple.getT2();
                    if (serviceGroupByGroupOrder.getDisplayOrder() != null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "create.service.group.order.is.exits"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    public Mono<List<ServiceGroup>> getAllServiceGroupActive() {
        return serviceGroupRepository.getAllTelecomServiceActive().collectList();
    }
}
