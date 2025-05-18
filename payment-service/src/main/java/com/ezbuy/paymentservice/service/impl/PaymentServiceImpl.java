package com.ezbuy.paymentservice.service.impl;

import static com.ezbuy.ordermodel.constants.Constants.RequestBanking.STATE_DONE;
import static com.ezbuy.ordermodel.constants.Constants.RequestBanking.STATE_FAIL;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.reactify.constants.CommonErrorCode.INTERNAL_SERVER_ERROR;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentmodel.constants.OrderState;
import com.ezbuy.paymentmodel.constants.PaymentConstants;
import com.ezbuy.paymentmodel.constants.PaymentState;
import com.ezbuy.paymentmodel.dto.ConfigPaymentDTO;
import com.ezbuy.paymentmodel.dto.RequestBankingSyncDTO;
import com.ezbuy.paymentmodel.dto.UpdateOrderStateDTO;
import com.ezbuy.paymentmodel.dto.request.*;
import com.ezbuy.paymentmodel.dto.response.ProductPaymentResponse;
import com.ezbuy.paymentmodel.dto.response.SearchPaymentState;
import com.ezbuy.paymentmodel.model.RequestBanking;
import com.ezbuy.paymentservice.client.*;
import com.ezbuy.paymentservice.client.OrderClient;
import com.ezbuy.paymentservice.client.PaymentClient;
import com.ezbuy.paymentservice.client.properties.PaymentClientProperties;
import com.ezbuy.paymentservice.repoTemplate.RequestBankingRepositoryTemplate;
import com.ezbuy.paymentservice.repository.OrderItemRepository;
import com.ezbuy.paymentservice.repository.OrderRepository;
import com.ezbuy.paymentservice.repository.RequestBankingRepository;
import com.ezbuy.paymentservice.service.OrderFieldConfigService;
import com.ezbuy.paymentservice.service.PaymentService;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String ERROR_CODE = "00";
    private static final Integer PAYMENT_STATUS = 1;
    private final PaymentClientProperties paymentProperties;
    private final RequestBankingRepository requestBankingRepository;
    private final RequestBankingRepositoryTemplate requestBankingRepositoryTemplate;
    private final OrderClient orderClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final SettingClient settingClient;
    private final OrderItemRepository orderItemRepository;
    private final AuthClient authClient;
    private final OrderFieldConfigService orderFieldConfigService;
    private final OrderRepository orderRepository;
    private final ObjectMapperUtil objectMapperUtil;
    private final ObjectMapper mapperObject = ObjectMapperFactory.defaultGetInstance();

    @Value("${application.data.sync-payment.limit}")
    private String limit;

    @Override
    public Mono<DataResponse> createLinkCheckout(ProductPaymentRequest request) {
        var getListOptionSet = settingClient.findByOptionSetCode(PaymentConstants.OptionSet.MERCHANT_CODE);
        var validate = validateRequest(request);
        return Mono.zip(validate, getListOptionSet).flatMap(tuple -> {
            UUID id = UUID.randomUUID();
            Long totalFee = request.getTotalFee();

            String orderCode = request.getOrderCode();
            String orderType = request.getOrderType();
            String telecomServiceAlias = request.getTelecomServiceAlias();
            String merchantCode = null;
            if (DataUtil.isNullOrEmpty(telecomServiceAlias)) {
                merchantCode = paymentProperties.getMerchantCode();
            } else {
                // lay thong tin cau hinh merchantCode
                List<OptionSetValue> lstOptionSetValue = tuple.getT2();
                if (!DataUtil.isNullOrEmpty(lstOptionSetValue)) {
                    for (OptionSetValue optionSetValue : lstOptionSetValue) {
                        if (optionSetValue.getCode().equals(telecomServiceAlias)) {
                            merchantCode = optionSetValue.getValue();
                            break;
                        }
                    }
                }
            }

            String cancelUrl = request.getCancelUrl();
            String returnUrl = request.getReturnUrl();

            var getListPaymentOptionSet =
                    settingClient.findByOptionSetCode(PaymentConstants.OptionSet.MERCHANT_CODE_PAYGATE);
            String finalMerchantCode = merchantCode;
            return Mono.zip(
                            saveRequestBanking(id, orderType, orderCode, totalFee, finalMerchantCode),
                            getListPaymentOptionSet)
                    .flatMap(savers -> {
                        String accessCode = paymentProperties.getAccessCode();
                        String hashCode = paymentProperties.getHashCode();
                        List<OptionSetValue> lstOptionSetValue = savers.getT2();
                        if (!DataUtil.isNullOrEmpty(lstOptionSetValue)) {
                            for (OptionSetValue optionSetValue : lstOptionSetValue) {
                                if (optionSetValue.getCode().equals(finalMerchantCode)) {
                                    ConfigPaymentDTO configPaymentDTO = objectMapperUtil.convertStringToObject(
                                            optionSetValue.getValue(), ConfigPaymentDTO.class);
                                    accessCode = configPaymentDTO.getAccessCode();
                                    hashCode = configPaymentDTO.getHashCode();
                                    break;
                                }
                            }
                        }
                        String data = DataUtil.sumListString(
                                accessCode,
                                finalMerchantCode,
                                id.toString(),
                                String.valueOf(totalFee).replace(",", ""));

                        String checksum = createChecksum(data, hashCode);
                        Integer isCombo = PaymentConstants.PaymentStatus.STATUS_INACTIVE; // khong phai luong combo
                        if (!DataUtil.isNullOrEmpty(request.getLstPaymentOrderDetail())) {
                            isCombo = PaymentConstants.PaymentStatus.STATUS_ACTIVE; // luong combo
                            for (PaymentOrderDetailDTO paymentOrderDetailDTO : request.getLstPaymentOrderDetail()) {
                                paymentOrderDetailDTO.setOrderCode(id.toString());
                            }
                        }
                        String lstPaymentDetail = null;
                        try {
                            lstPaymentDetail = URLEncoder.encode(
                                    mapperObject.writeValueAsString(request.getLstPaymentOrderDetail()),
                                    StandardCharsets.UTF_8.toString());
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        String templateCheckoutLink = paymentProperties.getTemplateCheckoutLink();
                        if (PaymentConstants.PaymentStatus.STATUS_ACTIVE.equals(isCombo)) {
                            templateCheckoutLink = paymentProperties.getTemplateCheckoutComboLink();
                        }
                        String checkoutLink = MessageFormat.format(
                                templateCheckoutLink,
                                finalMerchantCode,
                                checksum,
                                id.toString(),
                                String.valueOf(totalFee).replace(",", ""),
                                cancelUrl,
                                returnUrl,
                                isCombo,
                                lstPaymentDetail);
                        ProductPaymentResponse productPaymentResponse = new ProductPaymentResponse();
                        productPaymentResponse.setCheckoutLink(checkoutLink);
                        productPaymentResponse.setRequestBankingId(id.toString());
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), productPaymentResponse));
                    });
        });
    }

    @Override
    public Mono<DataResponse> getResultFromVnPay(PaymentResultRequest request) {
        var getListOptionSet = settingClient.findByOptionSetCode(PaymentConstants.OptionSet.MERCHANT_CODE_PAYGATE);
        return Mono.zip(validatePaymentResultRequest(request), getListOptionSet).flatMap(tuple -> {
            String accessCode = paymentProperties.getAccessCode();
            String hashCode = paymentProperties.getHashCode();
            List<OptionSetValue> lstOptionSetValue = tuple.getT2();
            if (!DataUtil.isNullOrEmpty(lstOptionSetValue)) {
                for (OptionSetValue optionSetValue : lstOptionSetValue) {
                    if (optionSetValue.getCode().equals(request.getMerchant_code())) {
                        ConfigPaymentDTO configPaymentDTO = objectMapperUtil.convertStringToObject(
                                optionSetValue.getValue(), ConfigPaymentDTO.class);
                        accessCode = configPaymentDTO.getAccessCode();
                        hashCode = configPaymentDTO.getHashCode();
                        break;
                    }
                }
            }
            String data = DataUtil.sumListString(
                    accessCode,
                    request.getError_code(),
                    request.getMerchant_code(),
                    request.getOrder_code(),
                    String.valueOf(request.getTrans_amount()));

            String checkSum = createChecksum(data, hashCode);
            if (!checkSum.equals(request.getCheck_sum())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "check_sum.invalid"));
            }

            return requestBankingRepository
                    .findRequestBankingByIdAndStatus(request.getOrder_code())
                    .switchIfEmpty(Mono.error(
                            new BusinessException(CommonErrorCode.INVALID_PARAMS, "request.banking.not.exist")))
                    .flatMap(requestBanking -> {
                        if (requestBanking.getState() >= PAYMENT_STATUS) {
                            log.info(
                                    "Đơn hàng đã được thực hiện từ trước. Trạng thái là: {}",
                                    requestBanking.getState());
                            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "state.invalid"));
                        }
                        if (!request.getTrans_amount().equals(requestBanking.getTotalFee())) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "trans_amount.invalid"));
                        }
                        // update: neu payment_status = 1 => xu ly don hang, neu != 1 => cap nhat
                        // request_banking state = -1
                        if (!PaymentConstants.PaymentStatus.STATUS_ACTIVE.equals(request.getPayment_status())) {
                            log.info("payment_status != 1");
                            return AppUtils.insertData(requestBankingRepository.updateRequestBankingById(
                                            request.getOrder_code(), request.getVt_transaction_id(), STATE_FAIL))
                                    .flatMap(rs -> {
                                        if (Boolean.FALSE.equals(rs)) {
                                            return Mono.error(new BusinessException(
                                                    INTERNAL_SERVER_ERROR, "request_banking.update.failed"));
                                        }
                                        return Mono.just(new DataResponse<>(
                                                Translator.toLocaleVi(SUCCESS), request.getVt_transaction_id()));
                                    });
                        }
                        return AppUtils.insertData(requestBankingRepository.updateRequestBankingById(
                                        request.getOrder_code(), request.getVt_transaction_id(), STATE_DONE))
                                .flatMap(rs -> {
                                    if (Boolean.FALSE.equals(rs)) {
                                        return Mono.error(new BusinessException(
                                                INTERNAL_SERVER_ERROR, "request_banking.update.failed"));
                                    }
                                    log.info("Before call order-service for updateStateOrder");
                                    // update: updateStatusOrder truyen orderState = request.payment_status
                                    AppUtils.runHiddenStream(orderClient.updateStatusOrder(
                                            requestBanking.getOrderId(), request.getPayment_status()));
                                    return Mono.just(new DataResponse<>(
                                            Translator.toLocaleVi(SUCCESS), request.getVt_transaction_id()));
                                });
                    });
        });
    }

    @Override
    @Transactional
    public Mono<DataResponse> updateOrderState(UpdateOrderStateRequest request) {
        return validateUpdateOrderStateRequest(request)
                .flatMap(validateRequest -> requestBankingRepositoryTemplate
                        .getRequestBankingByListOrderCode(request)
                        .collectList())
                .flatMap(requestBankings -> {
                    if (DataUtil.isNullOrEmpty(requestBankings)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.code.not_exist"));
                    }
                    if (request.getUpdateOrderStateDTOList().size() > requestBankings.size()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.code.not_exist"));
                    }
                    return settingClient
                            .findByOptionSetCode(PaymentConstants.OptionSet.MERCHANT_CODE_PAYGATE)
                            .flatMap(lstSetting -> {
                                return buildOrderIdUpdateStateMap(requestBankings, lstSetting)
                                        .flatMap(orderIdUpdateStateMap -> {
                                            if (DataUtil.isNullOrEmpty(orderIdUpdateStateMap)) {
                                                return Mono.error(new BusinessException(
                                                        INTERNAL_SERVER_ERROR, "order.state.internal"));
                                            }
                                            return AppUtils.insertData(requestBankingRepositoryTemplate
                                                    .updateRequestBankingBatch(orderIdUpdateStateMap)
                                                    .collectList());
                                        })
                                        .flatMap(updateRequestBanking -> {
                                            if (DataUtil.isNullOrEmpty(updateRequestBanking)
                                                    || Boolean.FALSE.equals(updateRequestBanking)) {
                                                return Mono.error(new BusinessException(
                                                        INTERNAL_SERVER_ERROR, "order.state.internal"));
                                            }
                                            return Mono.just(new DataResponse<>(
                                                    Translator.toLocaleVi(SUCCESS),
                                                    request.getUpdateOrderStateDTOList()
                                                            .size()));
                                        });
                            });
                });
    }

    @Override
    public Mono<DataResponse> syncPaymentState(SyncOrderStateRequest request) {

        LocalDateTime startDate = request.getStartTime();
        LocalDateTime endDate = request.getEndTime();
        if (endDate != null) {
            endDate = endDate.plusDays(1);
        }
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.start.time.before.end.time"));
        }
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setLimit(DataUtil.safeToInt(limit));
        return syncData(request, new ArrayList<>()).flatMap(rs -> {
            var updateRequestBanking = AppUtils.insertData(requestBankingRepositoryTemplate
                    .updateRequestBankingBatchForSync(rs)
                    .collectList());
            List<RequestBankingSyncDTO> requestBankingSyncDTOList = rs.stream()
                    .filter(r -> !DataUtil.isNullOrEmpty(r.getVtTransactionId()))
                    .collect(Collectors.toList());
            var updateOrderState = Flux.fromIterable(requestBankingSyncDTOList)
                    .flatMap(r -> {
                        // update check payment_status = 1 => update status order
                        if (!DataUtil.safeEqual(r.getPaymentStatus(), SUCCESS)) {
                            return Mono.empty();
                        }
                        return orderClient.updateStatusOrder(r.getOrderCode(), r.getPaymentStatus());
                    })
                    .collectList();
            return Mono.zip(updateRequestBanking, updateOrderState)
                    .flatMap(update -> Mono.just(new DataResponse<>(
                            Translator.toLocaleVi(SUCCESS), update.getT1().toString())));
        });
    }

    private Mono<Boolean> saveRequestBanking(
            UUID id, String orderType, String orderCode, Long totalFee, String merchantCode) {
        var saveRequestBanking = requestBankingRepository.insertRequestBanking(
                id.toString(),
                orderType,
                orderCode,
                PaymentState.NEW.getValue(),
                OrderState.NEW.getValue(),
                totalFee,
                merchantCode,
                PaymentConstants.RoleName.SYSTEM,
                PaymentConstants.RoleName.SYSTEM);

        return saveRequestBanking.map(rs -> true).switchIfEmpty(Mono.just(true));
    }

    private Mono<Boolean> validateRequest(ProductPaymentRequest request) {

        if (DataUtil.isNullOrEmpty(request.getReturnUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "link.return.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getCancelUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "link.cancel.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getOrderCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.code.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getTotalFee())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.total.fee.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.type.null"));
        }

        if (request.getOrderCode().length() > 36) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.code.invalid"));
        }

        if (!ValidateUtils.validateLink(request.getReturnUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "link.return.invalid"));
        }

        if (!ValidateUtils.validateLink(request.getCancelUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "link.cancel.invalid"));
        }

        if (request.getTotalFee() <= 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "total.fee.invalid"));
        }

        if (!PaymentConstants.OrderType.ALLOW_ORDER_TYPES.contains(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "payment.order.type.invalid"));
        }

        return Mono.just(true);
    }

    private Mono<Boolean> validatePaymentResultRequest(PaymentResultRequest request) {

        log.info("Request recv from Bank {}", request);
        if (DataUtil.isNullOrEmpty(request.getCheck_sum())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "check_sum.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getError_code())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "error_code.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getMerchant_code())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "merchant_code.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getOrder_code())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order_code.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getPayment_status())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "payment_status.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getTrans_amount())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "trans_amount.null"));
        }

        if (DataUtil.isNullOrEmpty(request.getVt_transaction_id())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "vt_transaction_id.null"));
        }

        if (!ERROR_CODE.equals(request.getError_code())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "error_code.invalid"));
        }

        if (request.getTrans_amount() <= 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "trans_amount.invalid"));
        }

        return Mono.just(true);
    }

    private Mono<Boolean> validateUpdateOrderStateRequest(UpdateOrderStateRequest request) {
        if (request.getUpdateOrderStateDTOList().size() < 1) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.state.request.null"));
        }
        for (UpdateOrderStateDTO updateOrderStateDTO : request.getUpdateOrderStateDTOList()) {
            if (DataUtil.isNullOrEmpty(updateOrderStateDTO.getOrderCode())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.code.null"));
            }
            if (DataUtil.isNullOrEmpty(updateOrderStateDTO.getOrderType())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.type.null"));
            }
            if (DataUtil.isNullOrEmpty(updateOrderStateDTO.getOrderState())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.state.null"));
            }
            if (updateOrderStateDTO.getOrderState() != OrderState.COMPLETED.getValue()) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.state.invalid"));
            }
        }
        return Mono.just(true);
    }

    private Mono<List<RequestBankingSyncDTO>> syncData(
            SyncOrderStateRequest request, List<RequestBankingSyncDTO> currentList) {
        if (request.getRunCount() == 0) {
            return Mono.just(currentList);
        }

        long offset = request.getOffSet();
        log.info("run sync request_banking offset: {}, limit: {}", offset, request.getLimit());
        return requestBankingRepository
                .findAllByStataAndTime(
                        PaymentState.NEW.getValue(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getLimit(),
                        offset)
                .collectList()
                .flatMap(requestBankings -> {
                    if (DataUtil.isNullOrEmpty(requestBankings)) {
                        return Mono.just(currentList);
                    }

                    return Flux.fromIterable(requestBankings)
                            .flatMap(requestBanking -> {
                                String data = DataUtil.sumListString(
                                        paymentProperties.getAccessCode(),
                                        paymentProperties.getMerchantCode(),
                                        requestBanking.getId());

                                String check_sum = createChecksum(data, paymentProperties.getAccessCode());
                                return paymentClient
                                        .searchPaymentState(
                                                check_sum, requestBanking.getId(), paymentProperties.getMerchantCode())
                                        .flatMap(search -> {
                                            RequestBankingSyncDTO requestBankingSyncDTO = new RequestBankingSyncDTO();
                                            if (search.isEmpty()) {
                                                requestBankingSyncDTO.setUpdateState(0);
                                                requestBankingSyncDTO.setPaymentStatus(0);
                                                requestBankingSyncDTO.setVtTransactionId(null);
                                                requestBankingSyncDTO.setId(requestBanking.getId());
                                                requestBankingSyncDTO.setOrderCode(requestBanking.getOrderId());
                                                return Mono.just(requestBankingSyncDTO);
                                            }
                                            if (DataUtil.isNullOrEmpty(
                                                    search.get().getData())) {
                                                requestBankingSyncDTO.setUpdateState(0);
                                                requestBankingSyncDTO.setPaymentStatus(0);
                                                requestBankingSyncDTO.setVtTransactionId(null);
                                                requestBankingSyncDTO.setId(requestBanking.getId());
                                                requestBankingSyncDTO.setOrderCode(requestBanking.getOrderId());
                                                return Mono.just(requestBankingSyncDTO);
                                            }

                                            SearchPaymentState searchPaymentState =
                                                    search.get().getData();
                                            if (searchPaymentState.getPayment_status() == 1
                                                    && check_sum.equals(searchPaymentState.getCheck_sum())) {
                                                requestBankingSyncDTO.setUpdateState(1);
                                                requestBankingSyncDTO.setPaymentStatus(1);
                                                requestBankingSyncDTO.setVtTransactionId(
                                                        searchPaymentState.getVt_transaction_id());
                                            }
                                            if (searchPaymentState.getPayment_status() != 1
                                                    && check_sum.equals(searchPaymentState.getCheck_sum())) {
                                                requestBankingSyncDTO.setUpdateState(1);
                                                requestBankingSyncDTO.setPaymentStatus(
                                                        searchPaymentState.getPayment_status());
                                                requestBankingSyncDTO.setVtTransactionId(
                                                        searchPaymentState.getVt_transaction_id());
                                            }
                                            requestBankingSyncDTO.setId(requestBanking.getId());
                                            requestBankingSyncDTO.setOrderCode(requestBanking.getOrderId());
                                            return Mono.just(requestBankingSyncDTO);
                                        });
                            })
                            .collectList()
                            .flatMap(rs -> {
                                rs.addAll(currentList);
                                request.reduceCount();
                                return syncData(request, rs);
                            });
                });
    }

    private Mono<Map<String, Integer>> buildOrderIdUpdateStateMap(
            List<RequestBanking> requestBankings, List<OptionSetValue> lstSettingPayment) {
        return Flux.fromIterable(requestBankings)
                .flatMap(requestBanking -> {
                    String accessCode = paymentProperties.getAccessCode();
                    String hashCode = paymentProperties.getHashCode();
                    String merchantCode = paymentProperties.getMerchantCode();
                    if (!DataUtil.isNullOrEmpty(requestBanking.getMerchantCode())) {
                        merchantCode = requestBanking.getMerchantCode();
                    }

                    if (!DataUtil.isNullOrEmpty(lstSettingPayment)) {
                        for (OptionSetValue optionSetValue : lstSettingPayment) {
                            if (DataUtil.safeEqual(optionSetValue.getCode(), (merchantCode))) {
                                ConfigPaymentDTO configPaymentDTO = objectMapperUtil.convertStringToObject(
                                        optionSetValue.getValue(), ConfigPaymentDTO.class);
                                accessCode = configPaymentDTO.getAccessCode();
                                hashCode = configPaymentDTO.getHashCode();
                                break;
                            }
                        }
                    }
                    String data = DataUtil.sumListString(
                            accessCode,
                            merchantCode,
                            requestBanking.getOrderId(),
                            String.valueOf(OrderState.COMPLETED.getValue()),
                            requestBanking.getVtTransactionId());

                    String check_sum = createChecksum(data, hashCode);

                    UpdateOrderStateMyViettelRequest updateOrderStateMyViettelRequest =
                            new UpdateOrderStateMyViettelRequest();
                    updateOrderStateMyViettelRequest.setTransaction_id(requestBanking.getVtTransactionId());
                    updateOrderStateMyViettelRequest.setMerchant_code(paymentProperties.getMerchantCode());
                    updateOrderStateMyViettelRequest.setCheck_sum(check_sum);
                    updateOrderStateMyViettelRequest.setStatus(OrderState.COMPLETED.getValue());
                    updateOrderStateMyViettelRequest.setPayment_status(requestBanking.getState());
                    updateOrderStateMyViettelRequest.setOrder_code(requestBanking.getOrderId());

                    return paymentClient
                            .updateOrderStateForMyViettel(updateOrderStateMyViettelRequest)
                            .map(updateOrderStateMyViettel -> {
                                if (DataUtil.isNullOrEmpty(updateOrderStateMyViettel)
                                        || updateOrderStateMyViettel.isEmpty()) {
                                    return Map.entry(requestBanking.getId(), 0);
                                }

                                if (updateOrderStateMyViettel.get().getErrorCode() != 0) {
                                    return Map.entry(requestBanking.getId(), 0);
                                }
                                return Map.entry(requestBanking.getId(), 1);
                            });
                })
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .map(Collections::unmodifiableMap);
    }

    private String createChecksum(String data, String hashCode) {
        try {
            return SecurityUtils.hmac(data, hashCode, paymentProperties.getAlgorithm());
        } catch (SignatureException e) {
            log.error("Error when create checksum {}", e);
            throw new RuntimeException(e);
        }
    }
}
