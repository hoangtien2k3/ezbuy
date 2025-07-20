package com.ezbuy.orderservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.ordermodel.constants.Constants;
import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.model.InvoiceInfoHistory;
import com.ezbuy.orderservice.client.SettingClient;
import com.ezbuy.orderservice.repoTemplate.InvoiceInfoHistoryRepositoryTemplate;
import com.ezbuy.orderservice.repository.InvoiceInfoHistoryRepository;
import com.ezbuy.orderservice.repository.InvoiceInfoRepository;
import com.ezbuy.orderservice.service.InvoiceInfoHistoryService;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceInfoHistoryServiceImpl implements InvoiceInfoHistoryService {
    private final InvoiceInfoHistoryRepository invoiceInfoHistoryRepository;
    private final InvoiceInfoRepository invoiceInfoRepository;
    private final SettingClient settingClient;
    private final InvoiceInfoHistoryRepositoryTemplate invoiceInfoHistoryRepositoryTemplate;

    @Override
    public Mono<DataResponse<InvoiceInfoHistory>> createInvoiceInfoHistory(CreateInvoiceInfoHistoryRequest request) {
        return invoiceInfoRepository
                .findByUserIdAndOrganizationId(request.getUserId(), request.getOrganizationId())
                .flatMap(oldInvoiceInfo -> {
                    if (!oldInvoiceInfo.getTaxCode().equals(request.getTaxCode())
                            || !oldInvoiceInfo.getOrganizationName().equals(request.getOrganizationName())
                            || !oldInvoiceInfo.getFullName().equals(request.getFullName())
                            || !oldInvoiceInfo.getPhone().equals(request.getPhone())
                            || !oldInvoiceInfo.getEmail().equals(request.getEmail())
                            || !oldInvoiceInfo.getAddressDetail().equals(request.getAddressDetail())
                            || !oldInvoiceInfo.getProvinceCode().equals(request.getProvinceCode())
                            || !oldInvoiceInfo.getDistrictCode().equals(request.getDistrictCode())
                            || !oldInvoiceInfo.getPrecinctCode().equals(request.getPrecinctCode())
                            || !oldInvoiceInfo.getPayType().equals(request.getPayType())
                            || !oldInvoiceInfo.getAccountNumber().equals(request.getAccountNumber())) {
                        return Mono.zip(
                                        SecurityUtils.getCurrentUser() // get info user
                                                .switchIfEmpty(Mono.error(
                                                        new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                                        invoiceInfoHistoryRepository.getSysDate(),
                                        settingClient
                                                .getAreaName(
                                                        request.getProvinceCode(),
                                                        request.getDistrictCode(),
                                                        request.getPrecinctCode())
                                                .switchIfEmpty(Mono.just(new AreaDTO())))
                                .flatMap(tuple -> {
                                    List<String> updatedFields = new ArrayList<>();
                                    if (!oldInvoiceInfo.getOrganizationName().equals(request.getOrganizationName())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getOrganizationName())) {
                                        updatedFields.add("organizationName");
                                    }
                                    if (!oldInvoiceInfo.getTaxCode().equals(request.getTaxCode())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getTaxCode())) {
                                        updatedFields.add("taxCode");
                                    }
                                    if (!oldInvoiceInfo.getFullName().equals(request.getFullName())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getFullName())) {
                                        updatedFields.add("fullName");
                                    }
                                    if (!oldInvoiceInfo.getPhone().equals(request.getPhone())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getPhone())) {
                                        updatedFields.add("phone");
                                    }
                                    if (!oldInvoiceInfo.getEmail().equals(request.getEmail())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getEmail())) {
                                        updatedFields.add("email");
                                    }
                                    if (!oldInvoiceInfo.getAddressDetail().equals(request.getAddressDetail())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getAddressDetail())) {
                                        updatedFields.add("addressDetail");
                                    }
                                    if (!oldInvoiceInfo.getProvinceCode().equals(request.getProvinceCode())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getProvinceCode())) {
                                        updatedFields.add("provinceCode");
                                    }
                                    if (!oldInvoiceInfo.getDistrictCode().equals(request.getDistrictCode())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getDistrictCode())) {
                                        updatedFields.add("districtCode");
                                    }
                                    if (!oldInvoiceInfo.getPrecinctCode().equals(request.getPrecinctCode())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getPrecinctCode())) {
                                        updatedFields.add("precinctCode");
                                    }
                                    if (!oldInvoiceInfo.getPayType().equals(request.getPayType())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getPayType())) {
                                        updatedFields.add("payType");
                                    }
                                    if (!oldInvoiceInfo.getAccountNumber().equals(request.getAccountNumber())
                                            || DataUtil.isNullOrEmpty(oldInvoiceInfo.getAccountNumber())) {
                                        updatedFields.add("accountNumber");
                                    }
                                    String updateLog = String.join(",", updatedFields);
                                    String invoiceInfoHistoryId =
                                            UUID.randomUUID().toString();
                                    LocalDateTime now = tuple.getT2();
                                    String userName = tuple.getT1().getUsername();
                                    AreaDTO areaDTO = tuple.getT3();
                                    InvoiceInfoHistory invoiceInfoHistory = InvoiceInfoHistory.builder()
                                            .id(invoiceInfoHistoryId)
                                            .userId(DataUtil.safeTrim(request.getUserId()))
                                            .organizationName(DataUtil.safeTrim(request.getOrganizationName()))
                                            .organizationId(DataUtil.safeTrim(request.getOrganizationId()))
                                            .taxCode(DataUtil.safeTrim(request.getTaxCode()))
                                            .fullName(DataUtil.safeTrim(request.getFullName()))
                                            .phone(DataUtil.safeTrim(request.getPhone()))
                                            .email(DataUtil.safeTrim(request.getEmail()))
                                            .provinceCode(DataUtil.safeTrim(request.getProvinceCode()))
                                            .provinceName(areaDTO.getProvinceName())
                                            .districtCode(DataUtil.safeTrim(request.getDistrictCode()))
                                            .districtName(areaDTO.getDistrictName())
                                            .precinctCode(DataUtil.safeTrim(request.getPrecinctCode()))
                                            .precinctName(areaDTO.getPrecinctName())
                                            .addressDetail(DataUtil.safeTrim(request.getAddressDetail()))
                                            .createAt(now)
                                            .createBy(userName)
                                            .status(Constants.Common.STATUE_ACTIVE)
                                            .organizationName(DataUtil.safeTrim(request.getOrganizationName()))
                                            .payType(DataUtil.safeTrim(request.getPayType()))
                                            .accountNumber(DataUtil.safeTrim(request.getAccountNumber()))
                                            .updateLog(updateLog)
                                            .isNew(true)
                                            .build();
                                    return invoiceInfoHistoryRepository
                                            .save(invoiceInfoHistory)
                                            .switchIfEmpty(Mono.error(new BusinessException(
                                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                    Translator.toLocale("invoice.info.insert.failed"))))
                                            .flatMap(x -> Mono.just(new DataResponse<>(
                                                    Translator.toLocaleVi(SUCCESS), invoiceInfoHistory)));
                                });
                    } else {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    }
                });
    }

    @Override
    public Mono<Optional<List<InvoiceInfoHistory>>> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request) {
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();
        String userId = DataUtil.safeTrim(request.getUserId());
        if (DataUtil.isNullOrEmpty(userId)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.user.id.empty")));
        }
        if (DataUtil.isNullOrEmpty(request.getOrganizationId())) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.organization.id.empty")));
        }
        if ((Objects.isNull(fromDate) && Objects.nonNull(toDate))
                || (Objects.nonNull(fromDate) && Objects.isNull(toDate))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        if (!Objects.isNull(fromDate) && !Objects.isNull(toDate) && fromDate.isAfter(toDate)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
        }

        return invoiceInfoHistoryRepositoryTemplate
                .findInvoiceInfoHistory(request)
                .collectList()
                .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)));
    }
}
