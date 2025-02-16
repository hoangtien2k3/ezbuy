package com.ezbuy.orderservice.service.impl;

import com.ezbuy.ordermodel.constants.Constants;
import com.ezbuy.ordermodel.dto.PartnerLicenseKeyDTO;
import com.ezbuy.ordermodel.model.OrderItem;
import com.ezbuy.ordermodel.model.PartnerLicenseKey;
import com.ezbuy.orderservice.client.SettingClient;
import com.ezbuy.orderservice.repository.PartnerLicenseKeyRepository;
import com.ezbuy.orderservice.service.PartnerLicenseKeyService;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.TokenUser;
import com.reactify.util.AppUtils;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerLicenseKeyServiceImpl implements PartnerLicenseKeyService {
    private final PartnerLicenseKeyRepository partnerLicenseKeyRepository;
    private final SettingClient settingClient;

    public Flux<PartnerLicenseKey> getlstAliasKeyExists(
            String userId, String organizationId, List<PartnerLicenseKeyDTO> lstAliasCreateDTO) {
        userId = DataUtil.safeTrim(userId);
        if (DataUtil.isNullOrEmpty(userId)) {
            return Flux.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("license.key.user.id.empty")));
        }
        if (DataUtil.isNullOrEmpty(organizationId)) {
            return Flux.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("license.key.organization.id.empty")));
        }
        if (DataUtil.isNullOrEmpty(lstAliasCreateDTO)) {
            return Flux.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("license.key.service.alias.empty")));
        }
        List<String> lstAliasCreate = lstAliasCreateDTO.stream()
                .map(PartnerLicenseKeyDTO::getServiceAlias)
                .collect(Collectors.toList());
        return partnerLicenseKeyRepository.getlstAliasKeyExsit(userId, organizationId, lstAliasCreate);
    }
    ;

    public Mono<List<OptionSetValue>> getLstAcronym(List<String> lstServiceAlias) {
        if (DataUtil.isNullOrEmpty(lstServiceAlias)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("license.key.service.alias.empty")));
        }
        return settingClient
                .getAllActiveOptionSetValueByOptionSetCode("PARTNER_LICENSE_KEY_CODE")
                .flatMap(result -> {
                    return Mono.just(result.stream()
                            .filter(x -> lstServiceAlias.contains(x.getCode()))
                            .collect(Collectors.toList()));
                });
    }

    /**
     * Ham gen ra day so ngau nhien
     *
     * @return
     */
    public String generateRandomNumericString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomDigit = (int) (Math.random() * 10);
            stringBuilder.append(randomDigit);
        }
        return stringBuilder.toString();
    }

    /**
     * Ham gen ra license key
     *
     * @param serviceAlias
     * @param acronym
     * @return
     */
    private Mono<String> generateUniqueLicenseKey(String serviceAlias, String acronym) {
        String licenseKey = acronym + "_" + generateRandomNumericString();
        return partnerLicenseKeyRepository
                .findByLicenseKey(licenseKey)
                .flatMap(existingKey -> generateUniqueLicenseKey(serviceAlias, acronym))
                .switchIfEmpty(Mono.just(licenseKey));
    }

    @Override
    public Mono<List<PartnerLicenseKeyDTO>> createLicenseKey(
            String userId, String organizationId, List<PartnerLicenseKeyDTO> lstAliasCreateDTO) {

        // 1 lay thong tin licence key da tao tren DB
        return Mono.zip(
                        SecurityUtils.getCurrentUser(),
                        getlstAliasKeyExists(userId, organizationId, lstAliasCreateDTO)
                                .collectList())
                .flatMap(tuple -> {
                    TokenUser tokenUser = tuple.getT1();
                    List<PartnerLicenseKey> lstPartnerLicenceExists = tuple.getT2();
                    List<String> lstAliasCreate = lstAliasCreateDTO.stream()
                            .map(PartnerLicenseKeyDTO::getServiceAlias)
                            .collect(Collectors.toList()); // danh sach alias tao don hang
                    List<String> lstAliasExists = new ArrayList<>(); // danh sach alias da ton tai tren DB
                    List<String> lstAliasNeedCreateLicence = lstAliasCreate; // danh sach alias tao don hang chua ton
                    // tai tren DB bang licence key
                    if (!DataUtil.isNullOrEmpty(lstPartnerLicenceExists)) {
                        lstAliasExists = lstPartnerLicenceExists.stream()
                                .map(PartnerLicenseKey::getServiceAlias)
                                .collect(Collectors.toList());
                        lstAliasNeedCreateLicence = ListUtils.subtract(lstAliasCreate, lstAliasExists);
                    }
                    // Danh sach can tao map lai thong tin co san tren DB
                    for (PartnerLicenseKeyDTO partnerLicenseKeyDTO : lstAliasCreateDTO) {
                        PartnerLicenseKey partnerLicenseKey = lstPartnerLicenceExists.stream()
                                .filter(x ->
                                        DataUtil.safeEqual(x.getServiceAlias(), partnerLicenseKeyDTO.getServiceAlias()))
                                .findFirst()
                                .orElse(null);
                        partnerLicenseKeyDTO.setLicenceKey(
                                DataUtil.isNullOrEmpty(partnerLicenseKey) ? null : partnerLicenseKey.getLicenseKey());
                    }
                    if (DataUtil.isNullOrEmpty(lstAliasNeedCreateLicence)) {
                        return Mono.just(lstAliasCreateDTO);
                    }
                    // Neu danh sach can tao licence khac rong thi thuc hien tao licence

                    // lay cau hinh tien to cua licence
                    return getLstAcronym(lstAliasNeedCreateLicence).flatMap(lstAcronym -> {
                        if (DataUtil.isNullOrEmpty(lstAcronym)) {
                            // neu khong ton tai cau hinh thi lay danh sach truyen vao map lai thong tin co
                            // san tren DB va tra ra
                            return Mono.just(lstAliasCreateDTO);
                        }
                        // Nguoc lai thuc hien lay thong tin cau hinh va map them tao vao DB
                        List<OrderItem> lstSaveOrderItem = new ArrayList<>();
                        return Flux.fromIterable(lstAcronym)
                                .flatMap(configLicence -> {
                                    return generateUniqueLicenseKey(configLicence.getCode(), configLicence.getValue())
                                            .flatMap(licenseKey -> {
                                                PartnerLicenseKey partnerLicenseKey = PartnerLicenseKey.builder()
                                                        .id(UUID.randomUUID().toString())
                                                        .userId(userId)
                                                        .organizationId(organizationId)
                                                        .serviceAlias(configLicence.getCode())
                                                        .status(Constants.Common.STATUE_ACTIVE)
                                                        .licenseKey(licenseKey)
                                                        .createAt(LocalDateTime.now())
                                                        .createBy(tokenUser.getUsername())
                                                        .updateAt(LocalDateTime.now())
                                                        .updateBy(tokenUser.getUsername())
                                                        .isNew(true)
                                                        .build();

                                                return Mono.just(partnerLicenseKey);
                                            });
                                })
                                .collectList()
                                .flatMap(lstPartnerLicenseCreateKey -> {
                                    for (PartnerLicenseKeyDTO partnerLicenseKeyDTO : lstAliasCreateDTO) {
                                        PartnerLicenseKey partnerLicenseKey = lstPartnerLicenseCreateKey.stream()
                                                .filter(x -> DataUtil.safeEqual(
                                                        x.getServiceAlias(), partnerLicenseKeyDTO.getServiceAlias()))
                                                .findFirst()
                                                .orElse(null);
                                        partnerLicenseKeyDTO.setLicenceKey(
                                                DataUtil.isNullOrEmpty(partnerLicenseKey)
                                                        ? null
                                                        : partnerLicenseKey.getLicenseKey());
                                    }
                                    var saveAllItemMono = AppUtils.insertData(partnerLicenseKeyRepository
                                            .saveAll(lstPartnerLicenseCreateKey)
                                            .collectList());
                                    return saveAllItemMono.flatMap(result -> Mono.just(lstAliasCreateDTO));
                                });
                    });
                });
    }
}
