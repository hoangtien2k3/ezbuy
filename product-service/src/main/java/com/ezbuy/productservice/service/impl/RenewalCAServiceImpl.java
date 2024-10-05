package com.ezbuy.productservice.service.impl;

import com.ezbuy.productservice.client.*;
import com.ezbuy.productservice.client.utils.ProductClientUtils;
import com.ezbuy.productservice.repository.ActiveTelecomRepository;
import com.ezbuy.productservice.repository.SubscriberRepository;
import com.ezbuy.productservice.repository.TelecomRepository;
import com.ezbuy.productservice.service.RenewalCAService;
import com.ezbuy.sme.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.sme.framework.constants.CommonErrorCode;
import com.ezbuy.sme.framework.constants.Constants;
import com.ezbuy.sme.framework.exception.BusinessException;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.ordermodel.dto.response.PricingProductItemResponse;
import com.ezbuy.sme.productmodel.constants.ErrorCode;
import com.ezbuy.sme.productmodel.dto.ws.SubscriberCMResponse;
import com.ezbuy.sme.productmodel.model.ActiveTelecom;
import com.ezbuy.sme.productmodel.model.Subscriber;
import com.ezbuy.sme.productmodel.request.ProductSpecificationRequest;
import com.ezbuy.sme.productservice.client.*;
import com.ezbuy.sme.settingmodel.dto.TelecomDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ezbuy.sme.authmodel.constants.AuthConstants.MySign.SIGH_HASH_SUCCESS;
import static com.ezbuy.sme.framework.constants.MessageConstant.SUCCESS;

@Data
@Service
@RequiredArgsConstructor
@Slf4j
public class RenewalCAServiceImpl implements RenewalCAService {

    private final AuthClient authClient;
    private final CmClient cmClient;
    private final SubscriberRepository subscriberRepository;
    private final ActiveTelecomRepository activeTelecomRepository;
    private final SettingClient settingClient;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final TelecomRepository telecomRepository;

    // TODO: kiểm tra lại logic hàm này, nếu user cố tình truyền lên 1 organizationId bất kỳ thì đang lấy được thông tin
    @Override
    public Mono<DataResponse> getStatisticSubscriber(String organizationId, Integer time) {
        if (DataUtil.isNullOrEmpty(organizationId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "organization.id.required"));
        }
        Mono<List<TelecomDTO>> teleMono = settingClient.getTelecomService(null);
        Mono<String> idNo = getIdNo(organizationId);
        return Mono.zip(teleMono, idNo)
                .flatMap(data -> {
                    if (data.getT1().isEmpty()) {
                        return Mono.just(new ArrayList<>());
                    }
                    List<String> lstTelecomServiceAlias = data.getT1().stream()
                            .map(TelecomDTO::getServiceAlias)
                            .filter(serviceAlias -> !DataUtil.isNullOrEmpty(serviceAlias))
                            .collect(Collectors.toList());
                    return subscriberRepository.getStatisticSubscriber(data.getT2(), lstTelecomServiceAlias, time)
                            .map(statisticSubscriber -> {
                                for (TelecomDTO telecomDTO : data.getT1()) {
                                    if (statisticSubscriber.getTelecomServiceId().toString().equals(telecomDTO.getOriginId())) {
                                        statisticSubscriber.setImage(telecomDTO.getImage());
                                    }
                                }
                                return statisticSubscriber;
                            })
                            .collectList();
                }).map(response -> new DataResponse(Translator.toLocaleVi(SUCCESS), response));
    }

    public Mono<List<SubscriberResponse>> getSubscriberSmeInfo(Long telecomServiceId, String idNo, String isdn, String telecomServiceAlias) {

        return cmClient.getCustomerSubscriberSmeInfo(telecomServiceId, idNo, isdn).map(rspn -> {
            List<SubscriberResponse> listSubscriberResponse = new ArrayList<>();
            if (CollectionUtils.isEmpty(rspn.getListSubscriber())) {
                return new ArrayList<>();
            }
            for (SubscriberCMResponse subscriberCM : rspn.getListSubscriber()) {
                SubscriberResponse subscriberResponse = new SubscriberResponse();
                subscriberResponse.setSubscriberId(subscriberCM.getSubscriberId());
                subscriberResponse.setStatus(subscriberCM.getStatus());
                subscriberResponse.setProductId(subscriberCM.getProductId());
                subscriberResponse.setProductCode(subscriberCM.getProductCode());
                subscriberResponse.setProductName(subscriberCM.getProductName());
                subscriberResponse.setActivationDate(LocalDate.parse(subscriberCM.getStaDatetime().split("T")[0]));
                subscriberResponse.setIsdn(isdn);
                subscriberResponse.setIdNo(idNo);
                subscriberResponse.setTelecomServiceId(telecomServiceId);
                subscriberResponse.setTelecomServiceAlias(telecomServiceAlias);
                subscriberResponse.setAccountId(subscriberCM.getAccountId());
                subscriberResponse.setAddress(subscriberCM.getUserInfoDTO() != null ? subscriberCM.getUserInfoDTO().getAddress() : null);
                subscriberResponse.setGroupType(rspn.getCustTypeDTO() != null ? rspn.getCustTypeDTO().getGroupType() : null);
                if (!CollectionUtils.isEmpty(subscriberCM.getListSubAttDTO())) {
                    subscriberCM.getListSubAttDTO().forEach(subAtt -> {
                        // TODO: đưa EXPIRED_DATE_DURATION ra constant
                        if ("EXPIRED_DATE_DURATION".equalsIgnoreCase(subAtt.getAttCode()) && subAtt.getListSubAttDetailDTO() != null
                                && !subAtt.getListSubAttDetailDTO().isEmpty() && !DataUtil.isNullOrEmpty(subAtt.getListSubAttDetailDTO().get(0).getAttDetailValue())) {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            subscriberResponse.setExpiredDate(LocalDate.parse(subAtt.getListSubAttDetailDTO().get(0).getAttDetailValue(), dateTimeFormatter));
                        }
                    });
                }
                listSubscriberResponse.add(subscriberResponse);
            }

            return listSubscriberResponse;
        });
    }

    //    TODO: kiểm tra lại logic hàm này, nếu user cố tình truyền lên 1 organizationId bất kỳ thì đang lấy được thông tin
    @Override
    public Mono<DataResponse> getListSubscriber(Long telecomServiceId, String telecomServiceAlias ,String organizationId) {
        boolean isTelecomServiceId = DataUtil.isNullOrEmpty(telecomServiceId);
        boolean isTelecomServiceAlias = DataUtil.isNullOrEmpty(telecomServiceAlias);

        if(isTelecomServiceAlias && isTelecomServiceId){
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.invalid"));
        }
        if (DataUtil.isNullOrEmpty(organizationId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "organization.id.required"));
        }

            return getIdNo(organizationId).flatMap(idNo -> subscriberRepository.findByIdnoAndTelecomServiceIdAndTelecomServiceAlias(idNo, telecomServiceId, telecomServiceAlias)
                            .map(this::modelToDto).collectList())
                    .map(rspn -> new DataResponse(Translator.toLocaleVi(SUCCESS), rspn));

    }

    private SubscriberResponse modelToDto(Subscriber subscriber) {
        return SubscriberResponse.builder().subscriberId(subscriber.getId())
                .idNo(subscriber.getIdNo())
                .isdn(subscriber.getIsdn())
                .address(subscriber.getAddress())
                .productId(subscriber.getProductId())
                .productCode(subscriber.getProductCode())
                .productName(subscriber.getProductName())
                .activationDate(subscriber.getActivationDate())
                .expiredDate(subscriber.getExpiredDate())
                .status(subscriber.getStatus())
                .groupType(subscriber.getGroupType())
                .accountId(subscriber.getAccountId())
                .build();
    }

    private Subscriber dtoToModel(SubscriberResponse subscriberResponse) {
        return Subscriber.builder()
                .id(subscriberResponse.getSubscriberId())
                .idNo(subscriberResponse.getIdNo())
                .isdn(subscriberResponse.getIsdn())
                .address(subscriberResponse.getAddress())
                .productId(subscriberResponse.getProductId())
                .productCode(subscriberResponse.getProductCode())
                .productName(subscriberResponse.getProductName())
                .activationDate(subscriberResponse.getActivationDate())
                .expiredDate(subscriberResponse.getExpiredDate())
                .status(subscriberResponse.getStatus())
                .groupType(subscriberResponse.getGroupType())
                .accountId(subscriberResponse.getAccountId())
                .telecomServiceId(subscriberResponse.getTelecomServiceId())
                .telecomServiceAlias(subscriberResponse.getTelecomServiceAlias()) // bo sung alias
                .isNew(true)
                .build();
    }

    // TODO: ham nay se duoc xoa khi  nang cap xong ham sync-subscriber-order
    // TODO: ham nay se phai off di neu co scontract vi response tra ve CM khong co alias
    @Override
    public Mono<DataResponse> syncListSubscriber() {
        return authClient.getUsersTrusted().flatMap(idNos -> {
            log.info("idNos when call authClient {}", idNos);
            Flux<Boolean> isSuccess = Flux.fromIterable(idNos.stream()
                            .filter(idNo -> !DataUtil.isNullOrEmpty(idNo)).collect(Collectors.toList()))
                    // TODO: sao lại dùng hàm isSuccess.subscribe() ở đây; hạn chế dùng subscribe() vì đội myViettel đã gặp nhiều su co voi ham nay
                    .map(this::syncSubscriber);
            return Mono.just(new DataResponse(Translator.toLocaleVi(SUCCESS), isSuccess.subscribe().isDisposed()));
        });
    }

    // TODO: 1 ham to ntn ma khong co log
    private boolean syncSubscriber(String idNo) {
        return cmClient.getListSubscriberByIdNo(idNo)
                .flatMap(listSubscriber -> Flux.fromIterable(listSubscriber)
                        .flatMap(subscriber -> telecomRepository.getByOriginId(String.valueOf(subscriber.getTelecomServiceId()))
                                .flatMap(telecom -> {
                                    subscriber.setTelecomServiceAlias(telecom.getServiceAlias());  // bo sung alias vi CM khong tra ve alias
                                    return getSubscriberSmeInfo(subscriber.getTelecomServiceId(), idNo, subscriber.getIsdn(), subscriber.getTelecomServiceAlias());
                                })).collectList())
                .map(data -> data.stream().flatMap(Collection::stream).map(this::dtoToModel).collect(Collectors.toList()))
                .map(models -> {
                    for (Subscriber subscriber : models) {
                        subscriberRepository.findById(subscriber.getId())
                                .map(data -> {

                                    log.info("subscriberRepository before save {}", data);
                                    subscriber.setTelecomServiceAlias(data.getTelecomServiceAlias()); // khong doi alias voi co so du lieu
                                    subscriber.setNew(false);
                                    subscriberRepository.save(subscriber).subscribe();
                                    return subscriber;
                                })
                                .switchIfEmpty(subscriberRepository.save(subscriber))
                                .subscribe(); // TODO: khong dung ham subscribe
                    }
                    return models;
                }).map(subscribers -> {
                    List<ActiveTelecom> activeTelecoms = new ArrayList<>();
                    for (Subscriber subscriberSave : subscribers) {
                        if (!checkExistActiveTelecom(activeTelecoms, subscriberSave)) {
                            ActiveTelecom activeTelecom = new ActiveTelecom();
                            activeTelecom.setTelecomServiceId(subscriberSave.getTelecomServiceId());
                            activeTelecom.setAlias(subscriberSave.getTelecomServiceAlias()); // bo sung alias
                            activeTelecom.setIdNo(subscriberSave.getIdNo());
                            activeTelecom.setId(UUID.randomUUID().toString());
                            activeTelecom.setNew(true);
                            activeTelecoms.add(activeTelecom);
                        }
                    }
                    return activeTelecoms;
                }).subscribe(activeTelecoms -> {
                    for (ActiveTelecom activeTelecom : activeTelecoms) {
                        activeTelecomRepository.count(activeTelecom.getTelecomServiceId(), activeTelecom.getIdNo(), activeTelecom.getAlias()) // bo sung dieu kien alias
                                .subscribe(count -> {
                                    if (count == 0) {
                                        activeTelecomRepository.save(activeTelecom).subscribe();
                                    }
                                });
                    }
                })
                .isDisposed();
    }

    private boolean checkExistActiveTelecom(List<ActiveTelecom> activeTelecoms, Subscriber subscriberSave) {
        return activeTelecoms.stream().anyMatch(item -> item.getTelecomServiceId().equals(subscriberSave.getTelecomServiceId())
                && item.getIdNo().equals(subscriberSave.getIdNo()));
    }

    @Override
    public Mono<DataResponse> getProductSpecification(ProductSpecificationRequest request) {
        if (DataUtil.isNullOrEmpty(request.getProductIds())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.id.required"));
        }
        if (DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "telecom.service.alias.required"));
        }
        if (DataUtil.isNullOrEmpty(request.getGroupType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "group.type.required"));
        }
        if (DataUtil.isNullOrEmpty(request.getOrganizationId())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "organization.id.required"));
        }
        Mono<String> idNoMono = getIdNo(request.getOrganizationId());
        Flux<ProductSpecificationCAResponse> fluxProducts = Flux.concat(request.getProductIds().stream()
                .filter(productId -> !DataUtil.isNullOrEmpty(productId))
                .map(this::getProductTemplate).collect(Collectors.toList()));

        Mono<String> telecoService = this.telecomRepository.getTelecomServiceId(request.getTelecomServiceAlias());
        return idNoMono.flatMapMany(idNo ->
                        fluxProducts.flatMap(product ->
                                telecoService.flatMap(serviceId ->
                                        getPricingProduct(product, request.getGroupType(), idNo, product.getProductId(), Long.valueOf(serviceId),request.getTelecomServiceAlias()))))
                                        .collectList()
                                        .map(data -> new DataResponse(Translator.toLocaleVi(SUCCESS), data));
    }

    private Mono<ProductSpecificationCAResponse> getProductTemplate(String productId) {
        return productClient.getProductOfferingSpecification(productId)
                .map(respOptional -> {
                    ProductSpecificationCAResponse productSpecification = new ProductSpecificationCAResponse();
                    productSpecification.setProductId(productId);
                    if (respOptional.isEmpty()
                            || DataUtil.isNullOrEmpty(respOptional.get().getLstProductSpecCharUseDTO())) {
                        productSpecification.setProductPrices(new ArrayList<>());
                        return productSpecification;
                    }
                    List<ProductSpecCharDTO> productSpecCharDTOS = respOptional.get().getLstProductSpecCharUseDTO().stream()
                            .map(ProductSpecCharUseDTO::getProductSpecCharDTO)
                            .collect(Collectors.toList());
                    List<PriceProductDTO> priceProductList = new ArrayList<>();
                    for (ProductSpecCharDTO productSpecCharDTO : productSpecCharDTOS) {
                        if (ProductClientUtils.CA_PREPAID_DURATION.equals(productSpecCharDTO.getCode())) {
                            for (ProductSpecCharValueDTO value : productSpecCharDTO.getProductSpecCharValueDTOList()) {
                                priceProductList.add(PriceProductDTO.builder()
                                        .type(productSpecCharDTO.getCode())
                                        .dataType(productSpecCharDTO.getDataType())
                                        .value(Integer.valueOf(value.getValue())).build());
                            }
                        }
                        if (ProductClientUtils.CA_PREPAID_DURATION_DAY.equals(productSpecCharDTO.getCode())) {
                            for (ProductSpecCharValueDTO value : productSpecCharDTO.getProductSpecCharValueDTOList()) {
                                priceProductList.add(PriceProductDTO.builder()
                                        .dataType(productSpecCharDTO.getDataType())
                                        .type(productSpecCharDTO.getCode())
                                        .value(Integer.valueOf(value.getValue())).build());
                            }
                        }
                    }

                    productSpecification.setProductPrices(priceProductList);
                    return productSpecification;
                });
    }

    private Mono<ProductSpecificationCAResponse> getPricingProduct(ProductSpecificationCAResponse product, Integer groupType,
                                                                   String idNo, String productId, Long telecomServiceId, String telecomServiceAlias) {
        if (DataUtil.isNullOrEmpty(product.getProductPrices())) {
            return Mono.just(product);
        }
        PricingProductRequest pricingRequest = new PricingProductRequest();
        CustomerPricingDTO customer = new CustomerPricingDTO();
        customer.setGroupType(groupType);
        CustomerIdentityDTO customerIdentity = new CustomerIdentityDTO();
        customerIdentity.setIdNo(idNo);
        customer.setCustomerIdentity(customerIdentity);
        pricingRequest.setCustomer(customer);
        List<ProductOrderItemDTO> productOrderItems = new ArrayList<>();
        for (PriceProductDTO template : product.getProductPrices()) {
            ProductOrderItemDTO productOrderItemDTO = new ProductOrderItemDTO();
            ProductDTO productDTO = new ProductDTO();
            ProductCharacteristicRequest productCharacteristic = new ProductCharacteristicRequest();
            productCharacteristic.setName(template.getType());
            productCharacteristic.setValue(template.getValue());
            productCharacteristic.setValueType(template.getDataType());
            productDTO.setProductCharacteristic(productCharacteristic);
            productOrderItemDTO.setProduct(productDTO);
            productOrderItemDTO.setQuantity(1);

            ProductOfferingRefRequest poRef = new ProductOfferingRefRequest();
            poRef.setId(productId);
            poRef.setTelecomServiceId(telecomServiceId);
            poRef.setTelecomServiceAlias(telecomServiceAlias);
            productOrderItemDTO.setProductOffering(poRef);

            productOrderItems.add(productOrderItemDTO);
        }
        pricingRequest.setProductOrderItem(productOrderItems);

        return orderClient.getPricingProduct(pricingRequest).map(
                pricingResponse -> {
                    if (DataUtil.isNullOrEmpty(pricingResponse) || DataUtil.isNullOrEmpty(pricingResponse.getPricingProductItems())) {
                        product.setProductPrices(new ArrayList<>());
                        return product;
                    }
                    List<PricingProductItemResponse> productOrderItemResponse = pricingResponse.getPricingProductItems();
                    for (int i = 0; i < product.getProductPrices().size(); i++) {
                        product.getProductPrices().get(i).setPrice(productOrderItemResponse.get(i).getItemPrice().getPrice());
                    }
                    return product;
                }
        );
    }

    //    TODO: ham nay phai kiem tra them xem user goi api co thuoc organizationId khong? truyen them thong tin user_id sang.
    private Mono<String> getIdNo(String organizationId) {
        return authClient.getTenantIdentify(organizationId).map(tenantIdentifies -> {
            Optional<TenantIdentifyDTO> primaryIdentify = tenantIdentifies.stream()
                    .filter(tenantIdentifyDTO -> Constants.Activation.ACTIVE.equals(tenantIdentifyDTO.getPrimaryIdentify()))
                    .findFirst();
            if (primaryIdentify.isEmpty()) {
                throw new BusinessException(ErrorCode.ReNew.TRUST_MST_01, "organization.primary.empty");
            }
            if (!SIGH_HASH_SUCCESS.equals(primaryIdentify.get().getTrustStatus())) {
                throw new BusinessException(ErrorCode.ReNew.TRUST_MST_02, "organization.not.trusted");
            }
            return primaryIdentify.get().getIdNo();
        });
    }
}
