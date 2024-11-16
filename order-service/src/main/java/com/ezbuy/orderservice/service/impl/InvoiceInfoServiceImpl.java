package com.ezbuy.orderservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.ordermodel.constants.Constants;
import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoRequest;
import com.ezbuy.ordermodel.dto.request.UpdateInvoiceInfoRequest;
import com.ezbuy.ordermodel.model.InvoiceInfo;
import com.ezbuy.orderservice.client.SettingClient;
import com.ezbuy.orderservice.repository.InvoiceInfoRepository;
import com.ezbuy.orderservice.service.InvoiceInfoService;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceInfoServiceImpl implements InvoiceInfoService {
    private final InvoiceInfoRepository invoiceInfoRepository;
    private final SettingClient settingClient;

    @Override
    public Mono<Optional<InvoiceInfo>> getByUserIdAndOrganizationId(String userId, String organizationId) {
        userId = DataUtil.safeTrim(userId);
        if (DataUtil.isNullOrEmpty(userId)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.user.id.empty")));
        }
        if (DataUtil.isNullOrEmpty(organizationId)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.organization.id.empty")));
        }
        return invoiceInfoRepository
                .findByUserIdAndOrganizationId(userId, organizationId)
                .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)));
    }

    @Override
    public Mono<DataResponse<InvoiceInfo>> createInvoiceInfo(CreateInvoiceInfoRequest request) {
        return getByUserIdAndOrganizationId(request.getUserId(), request.getOrganizationId())
                .switchIfEmpty(Mono.just(Optional.of(new InvoiceInfo())))
                .flatMap(rs -> {
                    if (rs.isPresent() && !DataUtil.isNullOrEmpty(rs.get().getId())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.is.exits")));
                    }
                    var getSysDate = invoiceInfoRepository.getSysDate();
                    return Mono.zip(
                                    SecurityUtils.getCurrentUser() // get info user
                                            .switchIfEmpty(Mono.error(
                                                    new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                                    getSysDate,
                                    settingClient
                                            .getAreaName(
                                                    request.getProvinceCode(),
                                                    request.getDistrictCode(),
                                                    request.getPrecinctCode())
                                            .switchIfEmpty(Mono.just(new AreaDTO())))
                            .flatMap(tuple -> {
                                String invoiceInfoId = UUID.randomUUID().toString();
                                LocalDateTime now = tuple.getT2();
                                String userName = tuple.getT1().getUsername();
                                AreaDTO areaDTO = tuple.getT3();
                                InvoiceInfo invoiceInfo = InvoiceInfo.builder()
                                        .id(invoiceInfoId)
                                        .userId(DataUtil.safeTrim(request.getUserId()))
                                        .organizationId(DataUtil.safeTrim(request.getOrganizationId()))
                                        .organizationName(DataUtil.safeTrim(request.getOrganizationName()))
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
                                        .status(Constants.Common.STATUE_ACTIVE)
                                        .createAt(now)
                                        .createBy(userName)
                                        .updateAt(now)
                                        .updateBy(userName)
                                        .payType(DataUtil.safeTrim(request.getPayType()))
                                        .accountNumber(DataUtil.safeTrim(request.getAccountNumber()))
                                        .isNew(true)
                                        .build();
                                return invoiceInfoRepository
                                        .save(invoiceInfo)
                                        .switchIfEmpty(Mono.error(new BusinessException(
                                                CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                Translator.toLocale("invoice.info.insert.failed"))))
                                        .flatMap(x -> Mono.just(
                                                new DataResponse<>(Translator.toLocaleVi(SUCCESS), invoiceInfo)));
                            });
                });
    }

    @Override
    public Mono<DataResponse<InvoiceInfo>> updateInvoiceInfo(String id, UpdateInvoiceInfoRequest request) {
        String invoiceInfoId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(invoiceInfoId)) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("invoice.info.id.not.empty"));
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        invoiceInfoRepository
                                .getById(invoiceInfoId) // lay thong tin invoiceInfo theo id
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, Translator.toLocale("invoice.info.not.found")))),
                        settingClient
                                .getAreaName(
                                        request.getProvinceCode(), request.getDistrictCode(), request.getPrecinctCode())
                                .switchIfEmpty(Mono.just(new AreaDTO())))
                .flatMap(tuple -> {
                    InvoiceInfo invoiceInfo = tuple.getT2();
                    AreaDTO areaDTO = tuple.getT3();
                    invoiceInfo.setNew(false);
                    String organizationName = DataUtil.safeTrim((request.getOrganizationName()));
                    if (!DataUtil.isNullOrEmpty(organizationName)) {
                        invoiceInfo.setOrganizationName(organizationName);
                    }
                    String taxCode = DataUtil.safeTrim(request.getTaxCode());
                    if (!DataUtil.isNullOrEmpty(taxCode)) {
                        invoiceInfo.setTaxCode(taxCode);
                    }
                    String fullName = DataUtil.safeTrim(request.getFullName());
                    if (!DataUtil.isNullOrEmpty(fullName)) {
                        invoiceInfo.setFullName(fullName);
                    }
                    String phone = DataUtil.safeTrim(request.getPhone());
                    if (!DataUtil.isNullOrEmpty(phone)) {
                        invoiceInfo.setPhone(phone);
                    }
                    String email = DataUtil.safeTrim(request.getEmail());
                    if (!DataUtil.isNullOrEmpty(email)) {
                        invoiceInfo.setEmail(email);
                    }
                    String addressDetail = DataUtil.safeTrim(request.getAddressDetail());
                    if (!DataUtil.isNullOrEmpty(addressDetail)) {
                        invoiceInfo.setAddressDetail(addressDetail);
                    }
                    String provinceCode = DataUtil.safeTrim(request.getProvinceCode());
                    if (!DataUtil.isNullOrEmpty(provinceCode)) {
                        invoiceInfo.setProvinceCode(provinceCode);
                        invoiceInfo.setProvinceName(areaDTO.getProvinceName()); //
                    }
                    String districtCode = DataUtil.safeTrim(request.getDistrictCode());
                    if (!DataUtil.isNullOrEmpty(districtCode)) {
                        invoiceInfo.setDistrictCode(districtCode);
                        invoiceInfo.setDistrictName(areaDTO.getDistrictName());
                    }
                    String precinctCode = DataUtil.safeTrim(request.getPrecinctCode());
                    if (!DataUtil.isNullOrEmpty(precinctCode)) {
                        invoiceInfo.setPrecinctCode(precinctCode);
                        invoiceInfo.setPrecinctName(areaDTO.getPrecinctName());
                    }
                    String payType = DataUtil.safeTrim(request.getPayType());
                    if (!DataUtil.isNullOrEmpty(payType)) {
                        invoiceInfo.setPayType(payType); // bo sung cap nhat hinh thuc chuyen khoan
                    }
                    String accountNumber = DataUtil.safeTrim(request.getAccountNumber());
                    if (!DataUtil.isNullOrEmpty(accountNumber)) {
                        invoiceInfo.setAccountNumber(accountNumber); // bo sung cap nhat so tai khoan
                    }
                    invoiceInfo.setNew(false);
                    return invoiceInfoRepository
                            .save(invoiceInfo)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                    Translator.toLocale("invoice.info.insert.failed"))))
                            .flatMap(x -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), invoiceInfo)));
                });
    }
}
