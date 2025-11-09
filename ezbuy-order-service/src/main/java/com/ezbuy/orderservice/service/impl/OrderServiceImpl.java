package com.ezbuy.orderservice.service.impl;

import static com.ezbuy.ordermodel.constants.Constants.*;
import static com.ezbuy.ordermodel.constants.Constants.Actor.SYSTEM;
import static com.ezbuy.ordermodel.constants.Constants.CharacteristicKey.SUB_ISDN;
import static com.ezbuy.ordermodel.constants.Constants.Common.VALUE_0_STRING;
import static com.ezbuy.ordermodel.constants.Constants.Common.VALUE_1_STRING;
import static com.ezbuy.ordermodel.constants.Constants.OptionSetCode.DATA_POLICY;
import static com.ezbuy.ordermodel.constants.Constants.Order.CURRENCY_VND;
import static com.ezbuy.ordermodel.constants.Constants.OrderBccsData.STATUS_ACTIVE;
import static com.ezbuy.ordermodel.constants.Constants.OrderExt.ORDER_TRUST_IDENTITY;
import static com.ezbuy.ordermodel.constants.Constants.OrderType.*;
import static com.ezbuy.ordermodel.constants.Constants.OrderType.ALLOW_ORDER_TYPES;
import static com.ezbuy.ordermodel.constants.Constants.OrderType.COMBO_SME_HUB;
import static com.ezbuy.ordermodel.constants.Constants.ROW_TEMPLATE_NAME.*;
import static com.ezbuy.ordermodel.constants.Constants.TRUST_STATUS.*;
import static com.ezbuy.ordermodel.constants.MessageConstant.*;
import static com.ezbuy.ordermodel.constants.TemplateConstants.*;
import static com.ezbuy.paymentmodel.constants.PaymentConstants.OrderType.*;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.settingmodel.constants.Constants.UPLOAD_STATUS.ACTIVE;
import static com.ezbuy.core.constants.CommonConstant.FORMAT_DATE_DMY_HYPHEN;
import static com.ezbuy.core.constants.MessageConstant.FAIL;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.ezbuy.authmodel.dto.request.UpdateTenantTrustStatusRequest;
import com.ezbuy.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.ordermodel.constants.Constants;
import com.ezbuy.ordermodel.constants.ErrorCode;
import com.ezbuy.ordermodel.constants.OrderState;
import com.ezbuy.ordermodel.dto.*;
import com.ezbuy.ordermodel.dto.CustomerDTO;
import com.ezbuy.ordermodel.dto.OrderProductDTO;
import com.ezbuy.ordermodel.dto.pricing.OrderPrice;
import com.ezbuy.ordermodel.dto.request.*;
import com.ezbuy.ordermodel.dto.response.*;
import com.ezbuy.ordermodel.dto.sale.*;
import com.ezbuy.ordermodel.dto.ws.GetCustomerSubscriberSmeInfoResponse;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import com.ezbuy.ordermodel.dto.ws.ProductOrderItemWsDTO;
import com.ezbuy.ordermodel.model.*;
import com.ezbuy.ordermodel.model.Order;
import com.ezbuy.ordermodel.model.OrderBccsData;
import com.ezbuy.ordermodel.model.OrderExt;
import com.ezbuy.orderservice.client.*;
import com.ezbuy.orderservice.client.properties.OrderProperties;
import com.ezbuy.orderservice.client.utils.OrderClientUtils;
import com.ezbuy.orderservice.repoTemplate.OrderRepositoryTemplate;
import com.ezbuy.orderservice.repoTemplate.OrderTransactionRepositoryTemplate;
import com.ezbuy.orderservice.repository.*;
import com.ezbuy.orderservice.service.OrderFieldConfigService;
import com.ezbuy.orderservice.service.OrderService;
import com.ezbuy.orderservice.service.PartnerLicenseKeyService;
import com.ezbuy.paymentmodel.dto.UpdateOrderStateDTO;
import com.ezbuy.paymentmodel.dto.request.*;
import com.ezbuy.paymentmodel.dto.response.ProductPaymentResponse;
import com.ezbuy.productmodel.dto.response.ProductOfferTemplateDTO;
import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Regex;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.factory.ObjectMapperFactory;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CharacteristicRepository characteristicRepository;
    private final OrderFieldConfigService orderFieldConfigService;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PricingClient pricingClient;
    private final PaymentClient paymentClient;
    private final SettingClient settingClient;
    private final CartClient cartClient;
    private final CmClient cmClient;
    private final AuthClient authClient;
    // private final ObjectMapperUtil objectMapperUtil;
    private final CmPortalClient cmPortalClient;
    // private final ProvisioningClient provisioningClient;
    private final OrderBccsDataRepository orderBccsDataRepository;
    private final OrderRepositoryTemplate orderRepositoryTemplate;
    private final OrderV2Client orderV2Client;
    private final OrderProperties orderProperties;
    private static final Integer PAYMENT_STATUS = 1;
    private static final String ORGANIZATION_ID_CHARACTERISTIC = "ORGANIZATION_ID";
    private static final String RETURN_URL_SINGLE = "/SINGLE";
    private static final String RETURN_URL_COMBO = "COMBO";
    private final OrderExtRepository orderExtRepository;
    private final PartnerLicenseKeyService partnerLicenseKeyService;
    private final OrderTransactionRepositoryTemplate orderTransactionRepositoryTemplate;

    @Value("${application.data.sync-order.limit}")
    private String limit;

    @Value("${application.data.system-user}")
    private String systemUser;

    @Value("${application.connectCA.returnUrl}")
    private String returnUrl;

    @Value("${application.connectCA.cancelUrl}")
    private String cancelUrl;

    @Value("${application.connectOrder.returnUrl}")
    private String returnOrderUrl;

    @Value("${application.connectOrder.cancelUrl}")
    private String cancelOrderUrl;

    private String UPDATE_BY_SYSTEM = "system";
    private final ApiGatewayClient apiGatewayClient;

    @Override
    public Mono<DataResponse> searchOrder(SearchOrderRequest request) {
        int size = DataUtil.safeToInt(request.getPageSize(), 20);
        if (size <= 0 || size > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "size.invalid"));
        }

        int page = DataUtil.safeToInt(request.getPageIndex(), 1);
        if (page <= 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "page.invalid"));
        }

        Integer state = Constants.ORDER_STATE_MAP.get(DataUtil.safeTrim(request.getState()));
        String sort = DataUtil.safeToString(request.getSort(), "-createAt");

        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> {
                    var countMono = orderRepository.countOrderHistory(tokenUser.getId(), state);
                    return Mono.zip(countMono, Mono.just(tokenUser.getId()));
                })
                .flatMap(countOrderGroupData -> {
                    long count = DataUtil.safeToLong(countOrderGroupData.getT1());
                    if (count == 0) {
                        return Mono.just(new DataResponse<>(
                                Translator.toLocaleVi(SUCCESS), new SearchOrderHistoryResponse(page, size)));
                    }

                    int offset = PageUtils.getOffset(page, size);
                    var orderListMono = orderRepositoryTemplate
                            .searchOrderDetail(countOrderGroupData.getT2(), state, offset, size, sort)
                            .collectList();

                    return Mono.zip(orderListMono, Mono.just(count)).flatMap(orderGroupData -> {
                        Map<String, OrderDetailDTO> orderMap = new LinkedHashMap<>();
                        for (OrderDetailDTO order : orderGroupData.getT1()) {
                            if (orderMap.containsKey(order.getId())) {
                                orderMap.get(order.getId()).addItem(order.getItemList());
                            } else {
                                orderMap.put(order.getId(), order);
                            }
                        }

                        List<OrderDetailDTO> orderList = new ArrayList<>(orderMap.values());

                        PaginationDTO pagination = PaginationDTO.builder()
                                .pageIndex(page)
                                .pageSize(size)
                                .totalRecords(count)
                                .build();

                        SearchOrderHistoryResponse response = SearchOrderHistoryResponse.builder()
                                .data(orderList)
                                .pagination(pagination)
                                .build();
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
                    });
                });
    }

    @Override
    public Mono<DataResponse> findDetail(String orderId) {
        String trimOrderId = DataUtil.safeTrim(orderId);
        if (!ValidateUtils.validateUUID(trimOrderId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "orderId.invalid"));
        }

        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> orderRepositoryTemplate
                        .findOneDetailById(tokenUser.getId(), trimOrderId)
                        .collectList())
                .flatMap(orderList -> {
                    if (DataUtil.isNullOrEmpty(orderList)) {
                        return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "order.not.found"));
                    }

                    OrderDetailDTO orderDetail = orderList.getFirst();
                    for (int i = 1; i < orderList.size(); i++) {
                        orderDetail.addItem(orderList.get(i).getItemList());
                    }

                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), orderDetail));
                });
    }

    private Mono<DataResponse> deleteCartItem(
            List<ProductTemplateDTO> productTemplateList, String userId, boolean fromCart) {
        if (!fromCart) {
            return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
        }
        List<String> templateIds = productTemplateList.stream()
                .map(ProductTemplateDTO::getTemplateId)
                .collect(Collectors.toList());
        return cartClient
                .clearAllCartItem(userId, templateIds)
                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
    }

    private Mono<Boolean> saveOrder(
            UUID orderId,
            String orderCode,
            String userId,
            String individualId,
            Double price,
            String username,
            CreatePreOrderRequest request) {
        var saveOrderMono = orderRepository.insertOrder(
                orderId.toString(),
                orderCode,
                userId,
                individualId,
                price,
                "VND",
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.AREA_CODE)),
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.PROVINCE_NAME)),
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.DISTRICT_NAME)),
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.PRECINCT_NAME)),
                OrderState.IN_PROGRESS.getValue(),
                Constants.OrderType.PRE_ORDER,
                username,
                username);
        return saveOrderMono.map(rs -> true).switchIfEmpty(Mono.just(true));
    }

    private Mono<Boolean> saveOrderWithoutLogin(
            UUID orderId, String orderCode, Double price, CreatePreOrderRequest request) {
        return orderRepository
                .insertOrderWithoutLogin(
                        orderId.toString(),
                        orderCode,
                        price,
                        "VND",
                        OrderState.IN_PROGRESS.getValue(),
                        Constants.OrderType.PRE_ORDER,
                        DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.NAME)),
                        DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.EMAIL)),
                        DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.PHONE)),
                        DataUtil.safeToString(
                                request.getCustomerInfo().get(Constants.CustomerInfoProperty.COMPANY_NAME)))
                .map(res -> true)
                .switchIfEmpty(Mono.just(true));
    }

    private List<ProductTemplateDTO> getProducTemplateList(
            CreatePreOrderRequest request, List<ProductOfferTemplateDTO> productTemplateList) {
        String monthLabel = Translator.toLocaleVi("month.string");
        return productTemplateList.stream()
                .map(item -> {
                    Optional<com.ezbuy.ordermodel.dto.OrderProductDTO> productOptional =
                            request.getProductList().stream()
                                    .filter(product -> DataUtil.safeEqual(
                                            product.getTemplateId(), item.getProductOfferTemplateId()))
                                    .findAny();
                    List<ProductTemplateCharacteristicDTO> characteristicList =
                            getProductCharacteristics(item, request);

                    if (productOptional.isEmpty()) {
                        return null;
                    }
                    com.ezbuy.ordermodel.dto.OrderProductDTO orderProductDTO = productOptional.get();
                    String duration = !DataUtil.isNullOrEmpty(item.getDurationValue())
                            ? DataUtil.safeToInt(item.getDurationValue()) + " " + monthLabel
                            : "";
                    Double totalPrice = DataUtil.safeToDouble(item.getTotalPrice());
                    Double originPrice = item.getCost() != null ? item.getCost() : totalPrice;

                    return ProductTemplateDTO.builder()
                            .name(item.getTemplateName())
                            .templateCode(item.getTemplateCode())
                            .templateId(item.getProductOfferTemplateId())
                            .quantity(DataUtil.safeToInt(orderProductDTO.getQuantity(), 1))
                            .telecomServiceId(DataUtil.safeToLong(item.getTelecomServiceId()))
                            .telecomServiceAlias(DataUtil.safeToString(item.getTelecomServiceAlias()))
                            .productCharacteristic(characteristicList)
                            .name(item.getTemplateName())
                            .price(totalPrice)
                            .originPrice(originPrice)
                            .duration(duration)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<ProductTemplateCharacteristicDTO> getProductCharacteristics(
            ProductOfferTemplateDTO item, CreatePreOrderRequest request) {
        List<ProductTemplateCharacteristicDTO> characteristicList = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(item.getDurationValue())) {
            ProductTemplateCharacteristicDTO durationCharacteristic = ProductTemplateCharacteristicDTO.builder()
                    .desc(Translator.toLocaleVi("order.duration.usage"))
                    .name(Constants.PreOrderInfo.DURATION)
                    .value(item.getDurationValue())
                    .valueType(Constants.DataType.STRING)
                    .build();
            characteristicList.add(durationCharacteristic);
        }

        String description = "";
        if (!DataUtil.isNullOrEmpty(item.getLstSpecMainOffer())) {
            description =
                    DataUtil.safeToString(item.getLstSpecMainOffer().get(0).getName());
        }

        ProductTemplateCharacteristicDTO descriptionCharacteristic = ProductTemplateCharacteristicDTO.builder()
                .desc(Translator.toLocaleVi("order.description"))
                .name(Constants.PreOrderInfo.DESCRIPTION)
                .value(description)
                .valueType(Constants.DataType.STRING)
                .build();
        characteristicList.add(descriptionCharacteristic);

        ProductTemplateCharacteristicDTO totalPriceCharacteristic = ProductTemplateCharacteristicDTO.builder()
                .desc(Translator.toLocaleVi("order.total.price"))
                .name(Constants.PreOrderInfo.TOTAL_PRICE)
                .value(DataUtil.safeToString(item.getTotalPrice()))
                .valueType(Constants.DataType.LONG)
                .build();
        characteristicList.add(totalPriceCharacteristic);

        String fromStaffValue =
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.FROM_STAFF));
        if (!DataUtil.isNullOrEmpty(fromStaffValue)) {
            ProductTemplateCharacteristicDTO formStaffCharacteristic = ProductTemplateCharacteristicDTO.builder()
                    .desc(Translator.toLocaleVi("order.from.staff"))
                    .name(Constants.PreOrderInfo.FROM_STAFF)
                    .value(fromStaffValue)
                    .valueType(Constants.DataType.STRING)
                    .build();
            characteristicList.add(formStaffCharacteristic);
        }

        String amStaffValue =
                DataUtil.safeToString(request.getCustomerInfo().get(Constants.CustomerInfoProperty.STAFF_AM_SUPPORT));
        if (!DataUtil.isNullOrEmpty(amStaffValue)) {
            ProductTemplateCharacteristicDTO amStaffCharacteristic = ProductTemplateCharacteristicDTO.builder()
                    .desc(Translator.toLocaleVi("order.am.staff"))
                    .name(Constants.PreOrderInfo.STAFF_AM_SUPPORT)
                    .value(amStaffValue)
                    .valueType(Constants.DataType.STRING)
                    .build();
            characteristicList.add(amStaffCharacteristic);
        }
        return characteristicList;
    }

    /**
     * Ham build placeOrderDate
     *
     * @param request
     * @param productTemplateList
     * @param producTemplateList
     * @param accountId
     * @return
     */
    private PlaceOrderData buildPlaceOrderData(
            CreatePreOrderRequest request,
            List<ProductOfferTemplateDTO> productTemplateList,
            List<ProductTemplateDTO> producTemplateList,
            String accountId) {
        Map<String, Object> customerInfo = request.getCustomerInfo();
        CustomerDTO customer = CustomerDTO.builder()
                .name(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.COMPANY_NAME)))
                .contactEmail(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.EMAIL)))
                .telMobile(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.PHONE)))
                .areaCode(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.AREA_CODE)))
                .province(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.PROVINCE)))
                .district(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.DISTRICT)))
                .precinct(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.PRECINCT)))
                .portalAccountId(accountId)
                .build();

        // bo sung them alias cho serviceItemList theo PYCXXX/LuongToanTrinhScontract
        List<ServiceItemDTO> serviceItemList = productTemplateList.stream()
                .map(rs3 -> new ServiceItemDTO(
                        DataUtil.safeToLong(rs3.getTelecomServiceId()), null, rs3.getTelecomServiceAlias(), null))
                .distinct()
                .collect(Collectors.toList());

        return PlaceOrderData.builder()
                .orderType(CONNECT_EZBUY)
                .customer(customer)
                .lstProductTemplate(producTemplateList)
                .serviceItem(serviceItemList)
                .systemType(Constants.SystemType.SME_HUB)
                .recipientName(DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.NAME)))
                .payInfo(new PayInfoDTO())
                .preOrder(true)
                .needAssignStaff(true)
                .needSignCAContract(false)
                .needFillInfo(false)
                .needAutoFillInfo(true)
                .target("")
                .transactionPlace("HOME")
                .assignStaffCode(
                        DataUtil.safeToString(customerInfo.get(Constants.CustomerInfoProperty.STAFF_AM_SUPPORT)))
                .build();
    }

    @Override
    public Mono<DataResponse> syncOrderState(SyncOrderStateRequest request) {
        LocalDateTime startDate = request.getStartTime();
        LocalDateTime endDate = request.getEndTime();
        if (endDate != null) {
            endDate = endDate.plusDays(1);
        }
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.order.start.time.before.end.time"));
        }
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setLimit(DataUtil.safeToInt(limit));
        return synData(request, new ArrayList<>()).flatMap(changedOrderList -> {
            log.info("changedOrderList {}", changedOrderList.size());
            if (DataUtil.isNullOrEmpty(changedOrderList)) {
                return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), 0));
            }

            List<OrderSyncDTO> changedOrderListOrder = changedOrderList.stream()
                    .filter(rs -> DataUtil.safeEqual("1", rs.getType()))
                    .collect(Collectors.toList());
            List<OrderSyncDTO> changedOrderListOrderItem = changedOrderList.stream()
                    .filter(rs -> DataUtil.safeEqual("0", rs.getType()))
                    .collect(Collectors.toList());

            var updateRsMonoOrder = orderRepositoryTemplate
                    .updateOrderBatch(changedOrderListOrder)
                    .collectList();
            var updateRsMonoOrderItem = orderRepositoryTemplate
                    .updateOrderItemBatch(changedOrderListOrderItem)
                    .collectList();

            List<UpdateOrderStateDTO> updateOrderStateDTOList = changedOrderList.stream()
                    .filter(changedOrder -> DataUtil.safeEqual(changedOrder.getType(), "0"))
                    .map(item -> {
                        String orderCode = changedOrderList.stream()
                                .distinct()
                                .filter(order -> DataUtil.safeEqual(order.getOrderCode(), item.getOrderCode()))
                                .findAny()
                                .map(OrderSyncDTO::getOrderId)
                                .orElse(null);
                        return UpdateOrderStateDTO.builder()
                                .orderCode(orderCode)
                                .orderType(Constants.OrderType.PAID_ORDER)
                                .orderState(OrderState.COMPLETED.getValue())
                                .build();
                    })
                    .distinct()
                    .collect(Collectors.toList());

            UpdateOrderStateRequest updateOrderStateRequest = new UpdateOrderStateRequest();
            updateOrderStateRequest.setUpdateOrderStateDTOList(updateOrderStateDTOList);
            var updateOrderStateForRequestBanking = paymentClient.updateOrderCode(updateOrderStateRequest);

            return Mono.zip(
                            updateRsMonoOrder,
                            updateRsMonoOrderItem,
                            Mono.just(changedOrderList.size()),
                            updateOrderStateForRequestBanking)
                    .flatMap(updateRs -> Mono.just(new DataResponse<>(
                            Translator.toLocaleVi(SUCCESS),
                            updateRs.getT1().size() + updateRs.getT2().size())));
        });
    }

    @Override
    public Mono<DataResponse> createPaidOrder(CreatePaidOrderRequest createPaidOrderRequest) {

        return buildTelecomIdForPaidOrder(createPaidOrderRequest).flatMap(request -> {
            return validatePaidOrderRequest(request)
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap(rs -> {
                        CustomerIdentityDTO customerIdentityDTO = new CustomerIdentityDTO();
                        customerIdentityDTO.setIdNo(request.getIdNo());

                        // build customer pricing
                        CustomerPricingDTO customerPricingDTO = new CustomerPricingDTO();
                        customerPricingDTO.setCustomerIdentity(customerIdentityDTO);
                        customerPricingDTO.setGroupType(request.getGroupType());

                        // build productOrderItem
                        List<ProductOrderItemDTO> productOrderItemDTOList = new ArrayList<>();
                        request.getProductOrderItems().forEach(productOrderItem -> {
                            ProductOrderItemDTO productOrderItemDTO = new ProductOrderItemDTO();
                            ProductOfferingRefRequest productOfferingRefRequest = new ProductOfferingRefRequest();

                            ProductDTO productDTO = new ProductDTO();
                            ProductCharacteristicRequest productCharacteristicRequest =
                                    new ProductCharacteristicRequest();

                            productOrderItem
                                    .getProduct()
                                    .getProductCharacteristic()
                                    .forEach(characteristic -> {
                                        if ("PREPAID_DURATION_DAY".equals(characteristic.getName())
                                                || "PREPAID_DURATION".equals(characteristic.getName())) {
                                            productCharacteristicRequest.setName(characteristic.getName());
                                            productCharacteristicRequest.setValue(
                                                    Integer.valueOf(characteristic.getValue()));
                                            productCharacteristicRequest.setValueType(characteristic.getValueType());
                                        }
                                    });

                            productDTO.setProductCharacteristic(productCharacteristicRequest);
                            productOrderItemDTO.setProduct(productDTO);
                            productOfferingRefRequest.setId(String.valueOf(
                                    productOrderItem.getProductOfferingRef().getId()));
                            productOfferingRefRequest.setTelecomServiceId((long) Math.toIntExact(
                                    productOrderItem.getProductOfferingRef().getTelecomServiceId()));
                            productOfferingRefRequest.setTelecomServiceAlias(
                                    productOrderItem.getProductOfferingRef().getTelecomServiceAlias());
                            productOrderItemDTO.setProductOffering(productOfferingRefRequest);
                            productOrderItemDTOList.add(productOrderItemDTO);
                        });

                        // build pricing request
                        PricingProductRequest pricingProductRequest = new PricingProductRequest();
                        pricingProductRequest.setCustomer(customerPricingDTO);
                        pricingProductRequest.setProductOrderItem(productOrderItemDTOList);

                        AtomicReference<Long> totalFee = new AtomicReference<>(0L);
                        UUID orderId = UUID.randomUUID();
                        return SecurityUtils.getCurrentUser().flatMap(user -> {
                            String userId = user.getId();
                            String username = user.getUsername();

                            return Mono.zip(
                                            getPricingProductForPaidOrder(pricingProductRequest),
                                            authClient.findIndividualIdByUserId(userId, request.getOrganizationId()))
                                    .flatMap(tuple2 -> {
                                        String individualId = tuple2.getT2();
                                        DataResponse pricingResponse = tuple2.getT1();
                                        if (!DataUtil.isNullOrEmpty(pricingResponse.getErrorCode())
                                                || DataUtil.isNullOrEmpty(pricingResponse.getData())) {
                                            return Mono.error(new BusinessException(
                                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "order.pricing.invalid"));
                                        }

                                        PricingProductWSResponse pricingProductWSResponse =
                                                (PricingProductWSResponse) pricingResponse.getData();

                                        Map<String, Long> idPriceMap = new HashMap<>();
                                        pricingProductWSResponse
                                                .getProductOrderItem()
                                                .forEach(productOrderItemDTO -> {
                                                    totalFee.updateAndGet(v -> v
                                                            + productOrderItemDTO
                                                                    .getItemPrice()
                                                                    .getPrice());
                                                    idPriceMap.put(
                                                            productOrderItemDTO
                                                                    .getProductOffering()
                                                                    .getId(),
                                                            productOrderItemDTO
                                                                    .getItemPrice()
                                                                    .getPrice());
                                                });

                                        return saveOrderForPaidOrder(
                                                        orderId,
                                                        userId,
                                                        individualId,
                                                        Double.valueOf(totalFee.get()),
                                                        username,
                                                        request)
                                                .flatMap(saveOrder -> {
                                                    // build listItem and listCharacteristic
                                                    List<Characteristic> characteristicList = new ArrayList<>();
                                                    List<OrderItem> orderItemList = new ArrayList<>();
                                                    request.getProductOrderItems()
                                                            .forEach(productOrderItem -> {
                                                                UUID itemId = UUID.randomUUID();
                                                                orderItemList.add(OrderItem.builder()
                                                                        .id(String.valueOf(itemId))
                                                                        .productId(String.valueOf(
                                                                                productOrderItem
                                                                                        .getProductOfferingRef()
                                                                                        .getId()))
                                                                        .orderId(String.valueOf(orderId))
                                                                        .name(productOrderItem
                                                                                .getProductOfferingRef()
                                                                                .getName())
                                                                        .currency(Constants.Currency.VND)
                                                                        .quantity(1)
                                                                        .status(1)
                                                                        .telecomServiceId(String.valueOf(
                                                                                productOrderItem
                                                                                        .getProductOfferingRef()
                                                                                        .getTelecomServiceId()))
                                                                        .telecomServiceAlias(productOrderItem
                                                                                .getProductOfferingRef()
                                                                                .getTelecomServiceAlias())
                                                                        .createBy(username)
                                                                        .updateBy(username)
                                                                        .subscriberId(
                                                                                productOrderItem.getSubscriberId())
                                                                        .isBundle(
                                                                                productOrderItem
                                                                                                .getProduct()
                                                                                                .getIsBundle()
                                                                                        ? 1
                                                                                        : 0)
                                                                        .accountId(String.valueOf(
                                                                                productOrderItem
                                                                                        .getProductOfferingRef()
                                                                                        .getAccountId()))
                                                                        .productCode(productOrderItem
                                                                                .getProductOfferingRef()
                                                                                .getCode())
                                                                        .state(OrderState.NEW.getValue())
                                                                        .price(Double.valueOf(
                                                                                idPriceMap.get(
                                                                                        productOrderItem
                                                                                                .getProductOfferingRef()
                                                                                                .getId()
                                                                                                .toString())))
                                                                        .originPrice(Double.valueOf(
                                                                                idPriceMap.get(
                                                                                        productOrderItem
                                                                                                .getProductOfferingRef()
                                                                                                .getId()
                                                                                                .toString())))
                                                                        .build());
                                                                productOrderItem
                                                                        .getProduct()
                                                                        .getProductCharacteristic()
                                                                        .forEach(characteristicDTO -> {
                                                                            UUID characteristicId = UUID.randomUUID();
                                                                            characteristicList.add(
                                                                                    Characteristic.builder()
                                                                                            .id(
                                                                                                    String.valueOf(
                                                                                                            characteristicId))
                                                                                            .orderItemId(
                                                                                                    String.valueOf(
                                                                                                            itemId))
                                                                                            .code(
                                                                                                    characteristicDTO
                                                                                                            .getCode())
                                                                                            .value(
                                                                                                    characteristicDTO
                                                                                                            .getValue())
                                                                                            .valueType(
                                                                                                    characteristicDTO
                                                                                                            .getValueType())
                                                                                            .name(
                                                                                                    characteristicDTO
                                                                                                            .getName())
                                                                                            .createBy(username)
                                                                                            .updateBy(username)
                                                                                            .build());
                                                                        });
                                                                String fromStaffValue = request.getFromStaff();
                                                                if (!DataUtil.isNullOrEmpty(fromStaffValue)) {
                                                                    UUID characteristicId = UUID.randomUUID();
                                                                    Characteristic formStaffCharacteristic =
                                                                            Characteristic.builder()
                                                                                    .id(String.valueOf(
                                                                                            characteristicId))
                                                                                    .orderItemId(String.valueOf(itemId))
                                                                                    .name(
                                                                                            Constants.PreOrderInfo
                                                                                                    .FROM_STAFF)
                                                                                    .value(fromStaffValue)
                                                                                    .valueType(
                                                                                            Constants.DataType.STRING)
                                                                                    .createBy(username)
                                                                                    .updateBy(username)
                                                                                    .build();
                                                                    characteristicList.add(formStaffCharacteristic);
                                                                }
                                                            });
                                                    var saveAllItemMono = orderItemRepository
                                                            .saveAll(orderItemList)
                                                            .collectList();
                                                    var saveAllCharacteristicMono = characteristicRepository
                                                            .saveAll(characteristicList)
                                                            .collectList();

                                                    return Mono.zip(saveAllItemMono, saveAllCharacteristicMono)
                                                            .flatMap(saveAllItemAndCharacteristic -> {
                                                                ProductPaymentRequest productPaymentRequest =
                                                                        new ProductPaymentRequest();
                                                                productPaymentRequest.setOrderCode(
                                                                        String.valueOf(orderId));
                                                                productPaymentRequest.setOrderType(
                                                                        request.getOrderType());
                                                                productPaymentRequest.setReturnUrl(
                                                                        request.getReturnUrl());
                                                                productPaymentRequest.setCancelUrl(
                                                                        request.getCancelUrl());
                                                                productPaymentRequest.setTotalFee(totalFee.get());
                                                                return paymentClient.getLinkCheckOut(
                                                                        productPaymentRequest);
                                                            })
                                                            .flatMap(createLinkCheckout -> {
                                                                if (createLinkCheckout.isEmpty()) {
                                                                    return Mono.error(new BusinessException(
                                                                            CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                                            "create.link.checkout.internal"));
                                                                }
                                                                return Mono.just(new DataResponse<>(
                                                                        Translator.toLocaleVi(SUCCESS),
                                                                        createLinkCheckout
                                                                                .get()
                                                                                .getCheckoutLink()));
                                                            });
                                                });
                                    });
                        });
                    });
        });
    }

    // build telcomServiceId cho dich vu khi co Alias
    private Mono<CreatePaidOrderRequest> buildTelecomIdForPaidOrder(CreatePaidOrderRequest request) {
        List<String> telecomServiceAlias = request.getProductOrderItems().stream()
                .map(productOrderItem ->
                        productOrderItem.getProductOfferingRef().getTelecomServiceAlias())
                .collect(Collectors.toList());
        return settingClient.getTelecomData(null, telecomServiceAlias, null).flatMap(telecomDTOS -> {
            List<ProductOrderItem> lstProductOrderItem = request.getProductOrderItems();
            for (ProductOrderItem productOrderItem : lstProductOrderItem) {
                for (TelecomDTO telecomDTO : telecomDTOS) {
                    if (productOrderItem
                            .getProductOfferingRef()
                            .getTelecomServiceAlias()
                            .equals(telecomDTO.getServiceAlias())) {
                        productOrderItem
                                .getProductOfferingRef()
                                .setTelecomServiceId(Long.valueOf(telecomDTO.getOriginId()));
                        break;
                    }
                }
            }
            request.setProductOrderItems(lstProductOrderItem);
            return Mono.just(request);
        });
    }

    @Override
    public Mono<DataResponse<Object>> updateStateAndPlaceOrder(UpdateOrderStateForOrderRequest request) {

        if (DataUtil.isNullOrEmpty(request.getOrderCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order_code.required"));
        }

        if (DataUtil.isNullOrEmpty(request.getPaymentStatus())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "payment_status.required"));
        }

        if (!PAYMENT_STATUS.equals(request.getPaymentStatus())) {
            log.info("Receive payment failed");
            return orderRepositoryTemplate
                    .findCustomerSubscriberSmeInfoById(request.getOrderCode())
                    .collectList()
                    .flatMap(orderInfos -> {
                        if (DataUtil.isNullOrEmpty(orderInfos)) {
                            log.error("Order info not match");
                            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.invalid"));
                        }

                        var updateOrderItemsState = AppUtils.insertData(orderItemRepository.updateOrderItemByOrderCode(
                                request.getOrderCode(), SYSTEM, OrderState.REJECTED.getValue()));
                        var updateOrderState = orderRepository
                                .updateState(OrderState.REJECTED.getValue(), SYSTEM, request.getOrderCode())
                                .then(Mono.just(new Object()));
                        return Mono.zip(updateOrderState, updateOrderItemsState)
                                .flatMap(r -> {
                                    log.info(
                                            "after updateOrderState and updateOrderItemsState success when paymentStatus different 1");
                                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                                })
                                .doOnError(err -> log.error("Update order or item error ", err));
                    });
        }

        log.info("Pass case payment failed");
        String orderId = request.getOrderCode();
        return orderBccsDataRepository
                .findByOrderIdAndStatus(orderId, STATUS_ACTIVE)
                .collectList()
                .flatMap(orderBccsData -> {
                    if (!DataUtil.isNullOrEmpty(orderBccsData)) {
                        log.info("When orderBccsData different null");
                        OrderBccsData order = orderBccsData.get(0);
                        return orderV2Client
                                .createOrder(order.getOrderType(), StringEscapeUtils.escapeXml(order.getData()))
                                .flatMap(placeOrderResponse -> {
                                    if (DataUtil.safeEqual(
                                            placeOrderResponse.get().getErrorCode(), "0")) {
                                        var updateOrderItemsState =
                                                AppUtils.insertData(orderItemRepository.updateOrderItemByOrderCode(
                                                        request.getOrderCode(),
                                                        SYSTEM,
                                                        OrderState.IN_PROGRESS.getValue()));
                                        var updateOrderState = orderRepository
                                                .updateStateAndOrderCode(
                                                        OrderState.IN_PROGRESS.getValue(),
                                                        SYSTEM,
                                                        request.getOrderCode(),
                                                        placeOrderResponse.get().getDescription())
                                                .then(Mono.just(new Object()));
                                        return Mono.zip(updateOrderState, updateOrderItemsState)
                                                .flatMap(r -> {
                                                    log.info(
                                                            "updateOrderState and updateOrderItemsState success to 1 when payment status = 1");
                                                    return Mono.just(
                                                            new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                                                })
                                                .doOnError(err -> log.error("Update order or item error ", err));
                                    } else {
                                        log.info("call createOrder error: "
                                                + placeOrderResponse.get().getDescription());
                                        return orderRepository
                                                .updateLogs(
                                                        placeOrderResponse.get().getDescription(), SYSTEM, orderId)
                                                .then(Mono.just(new DataResponse<>(SUCCESS, null)))
                                                .doOnError(err -> log.error("Update order or item error ", err));
                                    }
                                });
                    }

                    log.info("Pass call orderV2Client");

                    return orderRepositoryTemplate
                            .findCustomerSubscriberSmeInfoById(request.getOrderCode())
                            .collectList()
                            .switchIfEmpty(Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "odder.not.found")))
                            .flatMap(orderInfos -> {
                                log.info("When call customer by order and orderItem");
                                if (DataUtil.isNullOrEmpty(orderInfos)) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.invalid"));
                                }
                                if (orderInfos.getFirst().getOrderState() >= 1) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.invalid"));
                                }

                                List<GetCustomerSubscriberSmeInfoRequest> getCustomerSubscriberSmeInfoRequestList =
                                        new ArrayList<>();
                                // tao orderItemIdSet
                                Set<String> orderItemIdSet = new HashSet<>();
                                orderInfos.forEach(o -> {
                                    orderItemIdSet.add(o.getId());
                                });

                                orderItemIdSet.forEach(orderItemId -> {
                                    GetCustomerSubscriberSmeInfoRequest getCustomerSubscriberSmeInfoRequest =
                                            new GetCustomerSubscriberSmeInfoRequest();
                                    List<Characteristic> characteristicList = new ArrayList<>();
                                    orderInfos.forEach(orderInfo -> {
                                        // bo sung lay id doanh nghiep -> de lay individualId truyen sang Order
                                        if (ORGANIZATION_ID_CHARACTERISTIC.equals(
                                                orderInfo.getCharacteristic().getName())) {
                                            getCustomerSubscriberSmeInfoRequest.setOrganizationId(orderInfo
                                                    .getCharacteristic()
                                                    .getValue());
                                            return;
                                        }
                                        Characteristic characteristic = new Characteristic();
                                        characteristic.setId(
                                                orderInfo.getCharacteristic().getId());
                                        characteristic.setName(
                                                orderInfo.getCharacteristic().getName());
                                        characteristic.setValueType(
                                                orderInfo.getCharacteristic().getValueType());
                                        characteristic.setValue(
                                                orderInfo.getCharacteristic().getValue());
                                        characteristic.setCode(
                                                orderInfo.getCharacteristic().getCode());
                                        getCustomerSubscriberSmeInfoRequest.setIsdn(orderInfo.getIsdn());
                                        if (Objects.equals(orderInfo.getId(), orderItemId)) {
                                            getCustomerSubscriberSmeInfoRequest.setIdNo(orderInfo.getIdNo());
                                            getCustomerSubscriberSmeInfoRequest.setIsdn(orderInfo.getIsdn());
                                            getCustomerSubscriberSmeInfoRequest.setTelecomServiceId(
                                                    orderInfo.getTelecomServiceId());
                                            getCustomerSubscriberSmeInfoRequest.setTelecomServiceAlias(
                                                    orderInfo.getTelecomServiceAlias()); // bo
                                            // sung
                                            // them
                                            // alias
                                            // cho
                                            // PYCXXX/LuongToanTrinhScontract
                                            getCustomerSubscriberSmeInfoRequest.setProductId(orderInfo.getProductId());
                                            getCustomerSubscriberSmeInfoRequest.setProductCode(
                                                    orderInfo.getProductCode());
                                            getCustomerSubscriberSmeInfoRequest.setName(orderInfo.getName());
                                            getCustomerSubscriberSmeInfoRequest.setSubscriberId(
                                                    orderInfo.getSubscriberId());
                                            getCustomerSubscriberSmeInfoRequest.setPrice(orderInfo.getPrice());
                                            getCustomerSubscriberSmeInfoRequest.setId(orderInfo.getId());
                                            getCustomerSubscriberSmeInfoRequest.setIsBundle(orderInfo.getIsBundle());
                                            getCustomerSubscriberSmeInfoRequest.setIndividualId(
                                                    orderInfo.getIndividualId());
                                            characteristicList.add(characteristic);
                                        }
                                    });
                                    getCustomerSubscriberSmeInfoRequest.setCharacteristicList(characteristicList);
                                    getCustomerSubscriberSmeInfoRequestList.add(getCustomerSubscriberSmeInfoRequest);
                                });
                                return placePaidOrderData(getCustomerSubscriberSmeInfoRequestList, orderId);
                            })
                            .switchIfEmpty(
                                    Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "odder.failed")))
                            .flatMap(orderItems -> {
                                log.info("After placePaidOrder {}", orderItems);
                                if (!orderItems.isEmpty()) {
                                    log.info("When orderItems ordered > 0");
                                    var updateOrderItemsState =
                                            Mono.from(orderRepositoryTemplate.updateOrderItemsState(orderItems));
                                    var updateOrderState = orderRepository
                                            .updateState(
                                                    OrderState.IN_PROGRESS.getValue(), SYSTEM, request.getOrderCode())
                                            .then(Mono.just(new Object()));
                                    return Mono.zip(updateOrderState, updateOrderItemsState);
                                } else {
                                    log.info("When not yet ordered");
                                    return Mono.just(new DataResponse<>(SUCCESS, null));
                                }
                            })
                            .flatMap(rs2 -> {
                                log.info("Pass orderV1");
                                return Mono.just(new DataResponse<>(SUCCESS, null));
                            });
                })
                .doOnError(err -> log.error("Handle order error ", err));
    }

    private Mono<List<OrderSyncDTO>> synData(SyncOrderStateRequest request, List<OrderSyncDTO> currentList) {
        if (request.getRunCount() == 0) {
            return Mono.just(currentList);
        }

        long offset = request.getOffSet();
        log.info("run sync order offset: {}, limit: {}", offset, request.getLimit());
        return orderRepository
                .findAllOrderByStateAndTime(
                        OrderState.IN_PROGRESS.getValue(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getLimit(),
                        offset)
                .collectList()
                .flatMap(orders -> {
                    if (DataUtil.isNullOrEmpty(orders)) {
                        return Mono.just(currentList);
                    }

                    List<String> orderCodeList =
                            orders.stream().map(OrderSyncDTO::getOrderCode).collect(Collectors.toList());
                    return orderClient.searchOrderState(orderCodeList).flatMap(respOptional -> {
                        log.info("respOptional {}", respOptional.get());
                        if (respOptional.isEmpty()
                                || DataUtil.isNullOrEmpty(respOptional.get().getOrderStateDataList())) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sync.order.error"));
                        }

                        List<OrderSyncDTO> orderList = respOptional.get().getOrderStateDataList().stream()
                                .filter(item -> !DataUtil.safeEqual(item.getState(), OrderState.IN_PROGRESS.getValue()))
                                .map(item -> {
                                    OrderSyncDTO orderSyncDTO = orders.stream()
                                            .filter(order -> DataUtil.safeEqual(order.getOrderCode(), item.getBpId()))
                                            .findAny()
                                            .orElse(null);
                                    return OrderSyncDTO.builder()
                                            .id(orderSyncDTO.getOrderId())
                                            .state(item.getState())
                                            .description(item.getReasonCancel())
                                            .updateBy(SYSTEM)
                                            .type(orderSyncDTO.getType())
                                            .build();
                                })
                                .collect(Collectors.toList());
                        orderList.addAll(currentList);
                        request.reduceCount();
                        return synData(request, orderList);
                    });
                });
    }

    public Mono<DataResponse> validateAndFormatCreatePreOrderRequest(CreatePreOrderRequest request) {
        if (DataUtil.isNullOrEmpty(request.getOrderType())) {
            request.setOrderType(Constants.OrderType.PRE_ORDER);
        }

        if (DataUtil.isNullOrEmpty(request.getProductList())) {
            String message = request.isGetAdvice() ? "order.product.required" : "order.product.advice.required";
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, message));
        }

        for (com.ezbuy.ordermodel.dto.OrderProductDTO product : request.getProductList()) {
            if (DataUtil.isNullOrEmpty(product.getTemplateId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, "templateId")));
            }

            if (DataUtil.isNullOrEmpty(product.getTelecomServiceId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, "telecomServiceId")));
            }

            // bổ sung thêm alias theo PYCXXX/LuongToanTrinhScontract Order-005
            if (DataUtil.isNullOrEmpty(product.getTelecomServiceAlias())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, "telecomServiceAlias")));
            }

            if (DataUtil.isNullOrEmpty(product.getQuantity())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, "quantity")));
            }

            if (DataUtil.safeToDouble(product.getQuantity()) <= 0) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.quantity.invalid"));
            }
        }

        request.getProductList().forEach(product -> {
            product.setTelecomServiceId(DataUtil.safeTrim(product.getTelecomServiceId()));
            product.setTelecomServiceAlias(DataUtil.safeTrim(product.getTelecomServiceAlias())); // bo sung alias cho
            // PYC Alias
            // PYCXXX/LuongToanTrinhScontract
            product.setTemplateId(DataUtil.safeTrim(product.getTemplateId()));
        });

        if (DataUtil.isNullOrEmpty(request.getOrderType())) {
            request.setOrderType(Constants.OrderType.PRE_ORDER);
        }

        if (!ALLOW_ORDER_TYPES.contains(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.type.invalid"));
        }

        Map<String, Object> trimCustomerInfoMap = request.getCustomerInfo().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> DataUtil.safeTrim(e.getValue())));
        request.setCustomerInfo(trimCustomerInfoMap);

        return validateRequestWithConfig(request).flatMap(validateResp -> {
            Map<String, Object> customerInfoMap = request.getCustomerInfo();
            String name = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.NAME));
            if (name.length() > 30) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "name.exceed.length"));
            }

            String email = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.EMAIL));
            if (email.length() > 50) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.exceed.length"));
            }

            if (!DataUtil.isNullOrEmpty(email) && !ValidateUtils.validateRegex(email, Regex.EMAIL_REGEX)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.invalid"));
            }

            String phone = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.PHONE));
            if (!ValidateUtils.validatePhone(phone)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "phone.invalid"));
            }

            String areaCode = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.AREA_CODE));
            if (areaCode.length() > 20) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "area.code.invalid"));
            }

            String provinceCode = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.PROVINCE));
            if (provinceCode.length() > 20) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "province.code.invalid"));
            }

            String provinceName = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.PROVINCE_NAME));
            if (provinceName.length() > 30) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "province.name.invalid"));
            }

            String districtCode = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.DISTRICT));
            if (districtCode.length() > 20) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "district.code.invalid"));
            }

            String districtName = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.DISTRICT_NAME));
            if (districtName.length() > 30) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "district.name.invalid"));
            }

            String precinctCode = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.PRECINCT));
            if (precinctCode.length() > 20) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "precinct.code.invalid"));
            }

            String precinctName = DataUtil.safeTrim(customerInfoMap.get(Constants.CustomerInfoProperty.PRECINCT_NAME));
            if (precinctName.length() > 30) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "precinct.name.invalid"));
            }

            String companyName =
                    DataUtil.safeToString(customerInfoMap.get(Constants.CustomerInfoProperty.COMPANY_NAME));
            if (companyName.length() > 100) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "company.name.invalid"));
            }

            return Mono.just(DataResponse.builder().build());
        });
    }

    private Mono<OrderFieldConfigDTO> getConfigOrder(GetOrderFieldConfigRequest request) {
        return orderFieldConfigService
                .getConfigByServiceTypeAndOrderType(request)
                .flatMap(configResp -> {
                    if (configResp.getData() == null) {
                        return Mono.just(new OrderFieldConfigDTO());
                    }
                    return Mono.just(configResp.getData());
                })
                .onErrorResume(throwable -> Mono.just(new OrderFieldConfigDTO()));
    }

    private Mono<Boolean> validateRequestWithConfig(CreatePreOrderRequest request) {
        Set<String> telecomServiceAliasSet = request.getProductList().stream()
                .map(OrderProductDTO::getTelecomServiceAlias)
                .collect(Collectors.toSet());

        GetOrderFieldConfigRequest getOrderFieldConfigRequest = GetOrderFieldConfigRequest.builder()
                .orderType(Constants.OrderType.PRE_ORDER)
                .lstTelecomServiceAlias(new ArrayList<>(telecomServiceAliasSet))
                .build();
        return getConfigOrder(getOrderFieldConfigRequest).flatMap(config -> {
            Map<String, Object> configMap = ObjectMapperFactory.getInstance().convertValue(config, Map.class);
            Map<String, Object> customerInfoMap = request.getCustomerInfo();
            for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                int value = DataUtil.safeToInt(entry.getValue());
                if (value != Constants.OrderConfigType.REQUIRED) {
                    continue;
                }
                String key = entry.getKey();
                Object requestValue = customerInfoMap.get(key);
                if (DataUtil.isNullOrEmpty(requestValue)) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, key)));
                }
            }

            int addressConfigValue = DataUtil.safeToInt(configMap.get(Constants.CustomerInfoProperty.AREA_CODE));
            if (addressConfigValue == Constants.OrderConfigType.REQUIRED) {
                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.PROVINCE))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.PROVINCE)));
                }

                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.PROVINCE_NAME))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.PROVINCE_NAME)));
                }

                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.DISTRICT))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.DISTRICT)));
                }

                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.DISTRICT_NAME))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.DISTRICT_NAME)));
                }

                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.PRECINCT))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.PRECINCT)));
                }
                if (DataUtil.isNullOrEmpty(customerInfoMap.get(Constants.CustomerInfoProperty.PRECINCT_NAME))) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi(PARAMS_REQUIRED, Constants.CustomerInfoProperty.PRECINCT_NAME)));
                }
            }
            return Mono.just(true);
        });
    }

    public Mono<Boolean> validatePaidOrderRequest(CreatePaidOrderRequest request) {

        if (DataUtil.isNullOrEmpty(request.getIdNo())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.idNo.required"));
        }

        if (DataUtil.isNullOrEmpty(request.getOrderType())) {
            request.setOrderType(Constants.OrderType.PAID_ORDER);
        }

        if (!Constants.OrderType.PAID_ORDER.equals(request.getOrderType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.type.invalid"));
        }

        if (DataUtil.isNullOrEmpty(request.getReturnUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.return.link.required"));
        }

        if (DataUtil.isNullOrEmpty(request.getCancelUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.cancel.link.required"));
        }

        if (!ValidateUtils.validateLink(request.getReturnUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.return.link.invalid"));
        }

        if (!ValidateUtils.validateLink(request.getCancelUrl())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.cancel.link.invalid"));
        }

        String name = DataUtil.safeToString(request.getName());
        if (name.length() > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "name.exceed.length"));
        }

        String email = DataUtil.safeToString(request.getEmail());
        if (email.length() > 50) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.exceed.length"));
        }

        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(request.getEmail()))
                && !ValidateUtils.validateRegex(DataUtil.safeToString(request.getEmail()), Regex.EMAIL_REGEX)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.invalid"));
        }

        String phoneNumber = DataUtil.safeToString(request.getPhoneNumber());
        if (!ValidateUtils.validatePhone(phoneNumber)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "phone.invalid"));
        }

        AddressDTO addressDTO = request.getAddress();
        String provinceName = DataUtil.safeToString(addressDTO.getProvinceName());
        String districtName = DataUtil.safeToString(addressDTO.getDistrictName());
        String precinctName = DataUtil.safeToString(addressDTO.getPrecinctName());
        if (provinceName.length() > 30) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "province.name.invalid"));
        }
        if (districtName.length() > 30) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "district.name.invalid"));
        }
        if (precinctName.length() > 30) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "precinct.name.invalid"));
        }

        if (DataUtil.isNullOrEmpty(request.getProductOrderItems())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.items.required"));
        }

        for (ProductOrderItem productOrderItem : request.getProductOrderItems()) {
            if (DataUtil.isNullOrEmpty(productOrderItem.getSubscriberId())) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.items.subscriberId.required"));
            }

            if (DataUtil.isNullOrEmpty(productOrderItem.getProduct())) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.items.product.required"));
            }

            if (DataUtil.isNullOrEmpty(productOrderItem.getProductOfferingRef())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.required"));
            }

            if (DataUtil.isNullOrEmpty(productOrderItem.getProduct().getProductCharacteristic())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productCharacteristic.required"));
            }

            ProductOfferingRef productOfferingRef = productOrderItem.getProductOfferingRef();

            if (DataUtil.isNullOrEmpty(productOfferingRef.getId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.id.required"));
            }

            if (DataUtil.isNullOrEmpty(productOfferingRef.getName())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.name.required"));
            }

            if (DataUtil.isNullOrEmpty(productOfferingRef.getTelecomServiceId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.telecomServiceId.required"));
            }

            if (DataUtil.isNullOrEmpty(productOfferingRef.getTelecomServiceAlias())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.telecomServiceAlias.required"));
            }

            if (DataUtil.isNullOrEmpty(productOfferingRef.getAccountId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.accountId.required"));
            }

            if (DataUtil.isNullOrEmpty(productOfferingRef.getCode())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "order.items.productOfferingRef.code.required"));
            }
        }

        for (ProductOrderItem productOrderItem : request.getProductOrderItems()) {
            List<CharacteristicDTO> characteristics =
                    productOrderItem.getProduct().getProductCharacteristic();
            return validateCharacteristic(characteristics);
        }

        return Mono.just(true);
    }

    private Mono<Boolean> validateCharacteristic(List<CharacteristicDTO> characteristics) {
        int countRequired = 0;
        int count = 0;
        for (CharacteristicDTO characteristic : characteristics) {
            if (Constants.ProductCharacteristic.ACCOUNT_ISDN.equals(characteristic.getCode())) {
                countRequired++;
            }

            if (Constants.ProductCharacteristic.PREPAID_DURATION_DAY.equals(characteristic.getCode())
                    || Constants.ProductCharacteristic.PREPAID_DURATION.equals(characteristic.getCode())) {
                count++;
            }
        }

        if (!(countRequired > 0 && count > 0)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.items.productCharacteristic.invalid"));
        }

        for (CharacteristicDTO characteristic : characteristics) {
            if (Constants.ProductCharacteristic.ACCOUNT_ISDN.equals(characteristic.getCode())
                    || Constants.ProductCharacteristic.PREPAID_DURATION.equals(characteristic.getCode())
                    || Constants.ProductCharacteristic.PREPAID_DURATION_DAY.equals(characteristic.getCode())) {
                if (DataUtil.isNullOrEmpty(characteristic.getName())
                        || DataUtil.isNullOrEmpty(characteristic.getValueType())
                        || DataUtil.isNullOrEmpty(characteristic.getValue())) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "order.items.productCharacteristic.item.required"));
                }
            }
        }

        return Mono.just(true);
    }

    private Mono<Boolean> saveOrderForPaidOrder(
            UUID orderId,
            String userId,
            String individualId,
            Double totalFee,
            String username,
            CreatePaidOrderRequest request) {
        var saveOrderMono = orderRepository.insertOrderForPaidOrder(
                orderId.toString(),
                userId,
                individualId,
                totalFee,
                Constants.Currency.VND,
                OrderState.NEW.getValue(),
                Constants.OrderType.PAID_ORDER,
                username,
                username,
                request.getIdNo(),
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAddress().getProvinceName(),
                request.getAddress().getDistrict(),
                request.getAddress().getPrecinct());
        return saveOrderMono.map(rs -> true).switchIfEmpty(Mono.just(true));
    }

    private Mono<Map<String, String>> placePaidOrderData(
            List<GetCustomerSubscriberSmeInfoRequest> customerSubscriberSmeInfoRequests, String orderId) {
        log.info("Call BCCS ORDER");
        Map<String, String> orderItemIdOrderCodeMap = new HashMap<>();

        return Flux.fromIterable(customerSubscriberSmeInfoRequests)
                .flatMapSequential(customerSubscriberSmeInfoRequest -> {
                    log.info("start call cm");
                    return Mono.zip(
                                    // da confirm la khi goi qua cac doi khong can bo sung alias
                                    cmClient.getCustomerSubscriberSmeInfo(
                                                    customerSubscriberSmeInfoRequest.getIdNo(),
                                                    customerSubscriberSmeInfoRequest.getIsdn(),
                                                    customerSubscriberSmeInfoRequest.getTelecomServiceId())
                                            .doOnError(err -> {
                                                log.error("Handle CM error", err);
                                            }),
                                    SecurityUtils.getCurrentUser())
                            .flatMap(responseOptionalUser -> {
                                Optional<GetCustomerSubscriberSmeInfoResponse> responseOptional =
                                        responseOptionalUser.getT1();
                                if (responseOptional.isEmpty()
                                        || DataUtil.isNullOrEmpty(
                                                responseOptional.get().getLstCustomerDTO())
                                        || !"0".equals(responseOptional.get().getCode())) {
                                    log.error("CM not find customer subscriber");
                                    return Mono.just(DataResponse.builder().build());
                                }
                                return authClient
                                        .findIndividualIdByUserIdAndOrganizationId(
                                                responseOptionalUser.getT2().getId(),
                                                customerSubscriberSmeInfoRequest.getOrganizationId())
                                        .flatMap(id -> {
                                            GetCustomerSubscriberSmeInfoResponse response = responseOptional.get();
                                            PlacePaidOrderData placePaidOrderData = buildPlacePaidOrderData(
                                                    response, customerSubscriberSmeInfoRequest, id);
                                            // da confirm voi cac doi khong bo sung
                                            return orderClient
                                                    .placeOrder(
                                                            Constants.BCCSOrderType.AS_CA_EXTEND_PACKAGE,
                                                            DataUtil.parseObjectToString(placePaidOrderData))
                                                    .doOnError(err -> {
                                                        log.error("Handle BCCS order error ", err);
                                                    })
                                                    .flatMap(placeOrderResponse -> {
                                                        log.info("Order response {}", placePaidOrderData);
                                                        if (placeOrderResponse.isEmpty()) {
                                                            return Mono.just(DataResponse.builder()
                                                                    .build());
                                                        }
                                                        if ("true"
                                                                .equals(placeOrderResponse
                                                                        .get()
                                                                        .getSuccess())) {
                                                            log.info("Order success");
                                                            orderItemIdOrderCodeMap.put(
                                                                    customerSubscriberSmeInfoRequest.getId(),
                                                                    placeOrderResponse
                                                                            .get()
                                                                            .getDescription());
                                                        } else {
                                                            log.info("call placeOrder error: "
                                                                    + placeOrderResponse
                                                                            .get()
                                                                            .getDescription());
                                                            return orderRepository
                                                                    .updateLogs(
                                                                            placeOrderResponse
                                                                                    .get()
                                                                                    .getDescription(),
                                                                            SYSTEM,
                                                                            orderId)
                                                                    .then(Mono.just(placeOrderResponse));
                                                        }
                                                        return Mono.just(placeOrderResponse);
                                                    });
                                        });
                            })
                            .doOnError(err -> log.error("Error when handle order ", err));
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "odder.failed")))
                .then(Mono.just(orderItemIdOrderCodeMap))
                .doOnError(err -> log.error("Error when handle order and cm ", err));
    }

    private PlacePaidOrderData buildPlacePaidOrderData(
            GetCustomerSubscriberSmeInfoResponse response,
            GetCustomerSubscriberSmeInfoRequest request,
            String individualId) {
        PlacePaidOrderData placePaidOrderData = new PlacePaidOrderData();

        // customer
        CustomerPaidDTO customerPaidDTO = new CustomerPaidDTO();
        customerPaidDTO.setCustId(response.getLstCustomerDTO().getCustId());
        customerPaidDTO.setCustType(response.getLstCustomerDTO().getCustType());
        customerPaidDTO.setGroupType(
                response.getLstCustomerDTO().getCustTypeDTO().getGroupType());
        customerPaidDTO.setName(response.getLstCustomerDTO().getName());
        customerPaidDTO.setBirthDate(response.getLstCustomerDTO().getBirthDate());
        customerPaidDTO.setAreaCode(response.getLstCustomerDTO().getAreaCode());
        customerPaidDTO.setProvince(response.getLstCustomerDTO().getProvince());
        customerPaidDTO.setDistrict(response.getLstCustomerDTO().getDistrict());
        customerPaidDTO.setPrecinct(response.getLstCustomerDTO().getPrecinct());
        customerPaidDTO.setAddress(response.getLstCustomerDTO().getAddress());
        customerPaidDTO.setTelMobile(response.getLstCustomerDTO().getTelMobile());
        customerPaidDTO.setContactEmail(response.getLstCustomerDTO().getContactEmail());
        customerPaidDTO.setListCustIdentity(response.getLstCustomerDTO().getListCustIdentity());
        customerPaidDTO.setRepresentativeCust(response.getLstCustomerDTO().getRepresentativeCust());
        customerPaidDTO.setPortalAccountId(individualId);

        // serviceItem
        List<ServiceItemPaidDTO> serviceItemPaidDTOList = new ArrayList<>();
        ServiceItemPaidDTO serviceItemPaidDTO = new ServiceItemPaidDTO();
        serviceItemPaidDTO.setAccountId(
                response.getLstCustomerDTO().getListSubscriber().getAccountId());
        serviceItemPaidDTO.setTelecomServiceId(
                Long.valueOf(response.getLstCustomerDTO().getListSubscriber().getTelecomServiceId()));
        serviceItemPaidDTO.setTelecomServiceAlias(
                response.getLstCustomerDTO().getListSubscriber().getTelecomServiceAlias()); // bo
        // sung
        // alias
        // PYCXXX/LuongToanTrinhScontract
        serviceItemPaidDTOList.add(serviceItemPaidDTO);

        // build product
        ProductPaidDTO productDTO = new ProductPaidDTO();
        List<ProductTemplateCharacteristicDTO> productTemplateCharacteristicDTOList = new ArrayList<>();
        request.getCharacteristicList().forEach(characteristic -> {
            ProductTemplateCharacteristicDTO product = new ProductTemplateCharacteristicDTO();
            product.setName(characteristic.getName());
            product.setValueType(characteristic.getValueType());
            product.setValue(characteristic.getValue());
            product.setDesc(characteristic.getName());
            productTemplateCharacteristicDTOList.add(product);
        });
        productDTO.setIsBundle(request.getIsBundle() != 0);
        productDTO.setProductCharacteristic(productTemplateCharacteristicDTOList);

        // build productOferring
        ProductOfferingRef productOfferingRef = new ProductOfferingRef();
        productOfferingRef.setId(Long.valueOf(request.getProductId()));
        productOfferingRef.setName(request.getName());
        productOfferingRef.setTelecomServiceId(Long.valueOf(request.getTelecomServiceId()));
        productOfferingRef.setTelecomServiceAlias(request.getTelecomServiceAlias());
        productOfferingRef.setCode(request.getProductCode());

        // productOrderItem
        List<ProductOrderItemWsDTO> productOrderItemDTOList = new ArrayList<>();
        ProductOrderItemWsDTO productOrderItem = new ProductOrderItemWsDTO();
        productOrderItem.setId(1L);
        productOrderItem.setQuantity(1L);
        productOrderItem.setAction(Constants.OrderItemActionType.ADD);
        productOrderItem.setProduct(productDTO);
        productOrderItem.setProductOffering(productOfferingRef);
        productOrderItem.setSubscriberId(Long.valueOf(request.getSubscriberId().split("_")[1]));
        productOrderItem.setQuantity(1L);
        productOrderItemDTOList.add(productOrderItem);

        // build payinfoDTO
        PayInfoPaidOrderDTO payInfoDTO = new PayInfoPaidOrderDTO();
        payInfoDTO.setImmediatePay(true);
        payInfoDTO.setCardRecords(new ArrayList<>());
        payInfoDTO.setPayMethod("CTT");

        // build dataJson
        placePaidOrderData.setCustomer(customerPaidDTO);
        placePaidOrderData.setServiceItem(serviceItemPaidDTOList);
        placePaidOrderData.setSystemType(Constants.SystemType.SME_HUB);
        placePaidOrderData.setSystemStaffCode(orderProperties.getSystemStaffCode());
        placePaidOrderData.setStaffRevenue(orderProperties.getStaffRevenue());
        placePaidOrderData.setTransactionPlace(Constants.TransactionPlace.HOME);
        placePaidOrderData.setTarget(null);
        placePaidOrderData.setProductOrderItem(productOrderItemDTOList);
        placePaidOrderData.setTotalFee(request.getPrice());
        placePaidOrderData.setPayInfo(payInfoDTO);
        placePaidOrderData.setPayStatus(1L);
        placePaidOrderData.setBusinessCode("EXTEND_PACKAGE");
        placePaidOrderData.setNeedSignCAContract(Boolean.FALSE);
        log.info("Build paid order success");
        return placePaidOrderData;
    }

    @Override
    public Mono<DataResponse> getPricingProductInternal(PricingProductRequest request) {
        return getPricingProduct(request, false);
    }

    @Override
    public Mono<DataResponse> getPricingProduct(PricingProductRequest request) {
        return getPricingProduct(request, true);
    }

    private Mono<DataResponse> getPricingProduct(PricingProductRequest request, boolean isCheckIdNo) {
        return validatePricingProductRequest(request, isCheckIdNo)
                .flatMap(validatePricingProductRequest -> pricingClient
                        .getPricingProduct(request)
                        .map(respOptional -> {
                            if (respOptional.isEmpty()) {
                                return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null);
                            }
                            PricingProductResponse response = new PricingProductResponse();
                            if (DataUtil.isNullOrEmpty(respOptional.get().getProductOrderItem())) {
                                response.setPricingProductItems(new ArrayList<>());
                            } else {
                                List<PricingProductItemResponse> pricingProductItemResponses =
                                        respOptional.get().getProductOrderItem().stream()
                                                .map(productOrderItemDTO -> {
                                                    PricingProductItemResponse pricingItem =
                                                            new PricingProductItemResponse();
                                                    pricingItem.setItemPrice(productOrderItemDTO.getItemPrice());
                                                    return pricingItem;
                                                })
                                                .collect(Collectors.toList());
                                response.setPricingProductItems(pricingProductItemResponses);
                            }
                            return new DataResponse<>(Translator.toLocaleVi(SUCCESS), response);
                        }));
    }

    private Mono<DataResponse> getPricingProductForPaidOrder(PricingProductRequest request) {
        return validatePricingProductRequest(request, false).flatMap(validatePricingProductRequest -> pricingClient
                .getPricingProduct(request)
                .map(respOptional -> {
                    if (respOptional.isEmpty()) {
                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null);
                    }

                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), respOptional.get());
                }));
    }

    private Mono<Boolean> validatePricingProductRequest(PricingProductRequest request, boolean isCheckIdNo) {
        if (DataUtil.isNullOrEmpty(request.getCustomer())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.customer.invalid"));
        }
        if (DataUtil.isNullOrEmpty(request.getCustomer().getGroupType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.group.type.invalid"));
        }

        if (DataUtil.isNullOrEmpty(request.getProductOrderItem())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.product.order.item.invalid"));
        }

        for (ProductOrderItemDTO productOrderItemDTO : request.getProductOrderItem()) {
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProduct())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.product.invalid"));
            }
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProduct().getProductCharacteristic())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.characteristic.invalid"));
            }
            if (DataUtil.isNullOrEmpty(
                    productOrderItemDTO.getProduct().getProductCharacteristic().getName())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.characteristic.name.invalid"));
            }
            if (DataUtil.isNullOrEmpty(
                    productOrderItemDTO.getProduct().getProductCharacteristic().getValue())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.characteristic.value.invalid"));
            }
            if (DataUtil.isNullOrEmpty(
                    productOrderItemDTO.getProduct().getProductCharacteristic().getValueType())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.characteristic.value.type.invalid"));
            }
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProductOffering())) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.product.offering.invalid"));
            }
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProductOffering().getId())) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.product.offering.id.invalid"));
            }
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProductOffering().getTelecomServiceId())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.offering.telecom.service.id.invalid"));
            }
            // bo sung alias
            if (DataUtil.isNullOrEmpty(productOrderItemDTO.getProductOffering().getTelecomServiceAlias())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "pricing.product.offering.telecom.service.alias.invalid"));
            }
        }
        if (isCheckIdNo) {
            if (DataUtil.isNullOrEmpty(request.getOrganizationId())) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.organization.id.invalid"));
            }
            String idNoInput = request.getCustomer().getCustomerIdentity().getIdNo();
            if (DataUtil.isNullOrEmpty(idNoInput)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.id.no.invalid"));
            } else {
                return getIdNo(request.getOrganizationId()).flatMap(idNo -> {
                    if (!idNoInput.equals(idNo)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "pricing.id.mot.match"));
                    }
                    return Mono.just(true);
                });
            }
        }
        return Mono.just(true);
    }

    private Mono<String> getIdNo(String organizationId) {
        return authClient.getTenantIdentify(organizationId).map(tenantIdentifies -> {
            Optional<TenantIdentifyDTO> primaryIdentify = tenantIdentifies.stream()
                    .filter(tenantIdentifyDTO -> tenantIdentifyDTO.getPrimaryIdentify() == 1)
                    .findFirst();
            if (primaryIdentify.isEmpty()) {
                throw new BusinessException(ErrorCode.ReNew.TRUST_MST_01, "organization.primary.empty");
            }
            if (!Integer.valueOf(1).equals(primaryIdentify.get().getTrustStatus())) {
                throw new BusinessException(ErrorCode.ReNew.TRUST_MST_02, "organization.not.trusted");
            }
            return primaryIdentify.get().getIdNo();
        });
    }

    @Override
    public Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsCAInfo(GetGroupsCAinfoRequest request) {
        return authClient
                .getTrustedIdNoOrganization(request.getOrganizationId())
                .flatMap(lstIdNo -> {
                    if (DataUtil.isNullOrEmpty(lstIdNo)) {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    }
                    request.setOrganizationId(null);
                    request.setIdNo(lstIdNo.get(0));
                    return cmPortalClient.getGroupsInfo(request).flatMap(result -> getDataGroupInfo(result, true));
                });
    }

    @Override
    public Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsMemberCAInfo(GetGroupsCAinfoRequest request) {
        return authClient
                .getTrustedIdNoOrganization(request.getOrganizationId())
                .flatMap(lstIdNo -> {
                    if (DataUtil.isNullOrEmpty(lstIdNo)) {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    }
                    request.setOrganizationId(null);
                    request.setIdNo(lstIdNo.get(0));
                    return cmPortalClient
                            .getGroupsMembersInfo(request)
                            .flatMap(result -> getDataGroupInfo(result, false));
                });
    }

    Mono<DataResponse<GetGroupCAInfoResponse>> getDataGroupInfo(Optional<ResponseCM> result, boolean isGetGroup) {
        if (result.isEmpty()) {
            return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
        }
        ResponseCM responseCM = result.get();
        // neu khong phai ket qua thanh cong thi tra list rong
        if (!Constants.BCCSCmSystem.SUCCESS_CODE.equals(responseCM.getCode())) {
            return Mono.error(new BusinessException(responseCM.getCode(), responseCM.getDescription()));
        }
        if (DataUtil.isNullOrEmpty(responseCM.getGroupsDTOList())) {
            return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
        }
        Long numberGroup = responseCM.getNumberGroupAll(); // so luong nhom
        Long numberSub = responseCM.getNumberSubAll(); // so luong sub
        List<GroupsDTO> groupsDTOList = responseCM.getGroupsDTOList();
        List<GroupCAInfo> lstGroupCa = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (GroupsDTO groupsDTO : groupsDTOList) {
            GroupCAInfo groupCa = new GroupCAInfo();
            groupCa.setGroupCode(groupsDTO.getCode());
            groupCa.setGroupId(groupsDTO.getGroupId());
            groupCa.setGroupName(groupsDTO.getName());
            groupCa.setNumberSub(groupsDTO.getNumSubGroup());
            if (!DataUtil.isNullOrEmpty(groupsDTO.getMemberList())) {
                List<SubGroupCaDTO> lstMember = new ArrayList<>();
                for (GroupsMemberDTO groupsMemberDTO : groupsDTO.getMemberList()) {
                    SubGroupCaDTO subGroupCaDTO = new SubGroupCaDTO();
                    subGroupCaDTO.setIsdn(groupsMemberDTO.getIsdn());
                    subGroupCaDTO.setStatus(Translator.toLocaleVi("status." + groupsMemberDTO.getStatus()));
                    subGroupCaDTO.setCreateDate(formatter.format(groupsMemberDTO.getStaDatetime()));
                    if (!DataUtil.isNullOrEmpty(groupsMemberDTO.getListSubAttDTO())) {
                        for (SubAttDTO subAttDTO : groupsMemberDTO.getListSubAttDTO()) {
                            if (Constants.CharacteristicKey.ADD_MONTHS.equals(subAttDTO.getAttCode())) {
                                if (!DataUtil.isNullOrEmpty(subAttDTO.getListSubAttDetailDTO())) {
                                    SubAttDetailDTO subAttDetailDTO = subAttDTO.getListSubAttDetailDTO().stream()
                                            .filter(x ->
                                                    Constants.CharacteristicKey.ADD_MONTHS.equals(x.getAttDetailCode()))
                                            .findFirst()
                                            .orElse(null);
                                    if (subAttDetailDTO != null) {
                                        subGroupCaDTO.setAddMonth(
                                                DataUtil.safeToLong(subAttDetailDTO.getAttDetailValue()));
                                    }
                                }
                            }
                            if (Constants.CharacteristicKey.NUMBER_OF_SIGNATURES.equals(subAttDTO.getAttCode())) {
                                if (!DataUtil.isNullOrEmpty(subAttDTO.getListSubAttDetailDTO())) {
                                    SubAttDetailDTO subAttDetailDTO = subAttDTO.getListSubAttDetailDTO().stream()
                                            .filter(x -> Constants.CharacteristicKey.NUMBER_OF_SIGNATURES.equals(
                                                    x.getAttDetailCode()))
                                            .findFirst()
                                            .orElse(null);
                                    if (subAttDetailDTO != null) {
                                        subGroupCaDTO.setTotalSign(
                                                DataUtil.safeToLong(subAttDetailDTO.getAttDetailValue()));
                                    }
                                }
                            }
                        }
                    }
                    lstMember.add(subGroupCaDTO);
                }
                groupCa.setLstSubscriberCA(lstMember);
            }
            if (!DataUtil.isNullOrEmpty(groupsDTO.getListGroupsExtDTO())) {
                for (GroupsExtDTO groupsExtDTO : groupsDTO.getListGroupsExtDTO()) {
                    if (Constants.CharacteristicKey.NUMBER_OF_SIGNATURES.equals(groupsExtDTO.getExtKey())) {
                        groupCa.setTotalSign(DataUtil.safeToLong(groupsExtDTO.getExtValue()));
                    }
                    if (Constants.CharacteristicKey.MAX_SUB.equals(groupsExtDTO.getExtKey())) {
                        groupCa.setMaxSub(DataUtil.safeToLong(groupsExtDTO.getExtValue()));
                    }
                    if (Constants.CharacteristicKey.GROUP_ISDN_SME.equals(groupsExtDTO.getExtKey())) {
                        groupCa.setMainIsdnOcs(groupsExtDTO.getExtValue());
                    }
                    if (Constants.CharacteristicKey.GROUP_ID_CREATE_OCS.equals(groupsExtDTO.getExtKey())) {
                        groupCa.setGroupIDOcs(groupsExtDTO.getExtValue());
                    }
                    if (Constants.CharacteristicKey.GROUP_TYPE_CA_OCS.equals(groupsExtDTO.getExtKey())) {
                        groupCa.setGroupTypeOcs(groupsExtDTO.getExtValue());
                    }
                }
            }
            groupCa.setStatus(Translator.toLocaleVi("status." + groupsDTO.getStatus()));
            groupCa.setCreateDate(formatter.format(groupsDTO.getStaDatetime()));
            if (!isGetGroup) {
                // neu la truong hop lay thue bao theo nhom
                numberSub = groupsDTO.getNumSubGroup();
            }
            lstGroupCa.add(groupCa);
        }
        GetGroupCAInfoResponse response = new GetGroupCAInfoResponse();
        response.setGroupCAInfoList(lstGroupCa);
        if (!isGetGroup) {
            // neu la truong hop lay thue bao theo nhom
            response.setTotalRecord(numberSub);
        } else {
            response.setTotalRecord(numberGroup);
        }

        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
    }

    @Override
    public Mono<DataResponse> createNotPaidOrder(
            String orderType, PlaceOrderData orderData, String userId, String username, String individualId) {
        if (DataUtil.isNullOrEmpty(orderType)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "order.type.null"));
        }
        if (orderData == null) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "order.data.null"));
        }
        String dataJson = DataUtil.parseObjectToString(orderData);
        return orderV2Client.createOrder(orderType, dataJson).flatMap(placeOrderResponse -> {
            if (placeOrderResponse.isEmpty()) {
                return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "create.order.fail"));
            }
            if (!"true".equals(placeOrderResponse.get().getSuccess())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.BAD_REQUEST, placeOrderResponse.get().getDescription()));
            }
            // Neu don hang thanh cong thi luu them thong tin don hang
            UUID orderId = UUID.randomUUID();
            String orderCode = placeOrderResponse.get().getDescription();
            List<Characteristic> characteristicList = new ArrayList<>();
            List<OrderItem> orderItemList = new ArrayList<>();
            orderData.getProductOrderItem().forEach(productOrderItem -> {
                UUID itemId = UUID.randomUUID();
                orderItemList.add(OrderItem.builder()
                        .id(String.valueOf(itemId))
                        .productId(String.valueOf(
                                productOrderItem.getProductOfferingRef().getId()))
                        .orderId(String.valueOf(orderId))
                        .orderCode(orderCode)
                        .name(
                                DataUtil.isNullOrEmpty(productOrderItem
                                                .getProductOfferingRef()
                                                .getName())
                                        ? productOrderItem
                                                .getProductOfferingRef()
                                                .getName()
                                        : null)
                        .currency(Constants.Currency.VND)
                        .quantity(1)
                        .status(1)
                        .telecomServiceId(String.valueOf(
                                productOrderItem.getProductOfferingRef().getTelecomServiceId()))
                        .createBy(username)
                        .updateBy(username)
                        .subscriberId(productOrderItem.getSubscriberId())
                        .isBundle(productOrderItem.getProduct().getIsBundle() ? 1 : 0)
                        .accountId(String.valueOf(
                                productOrderItem.getProductOfferingRef().getAccountId()))
                        .productCode(productOrderItem.getProductOfferingRef().getCode())
                        .state(OrderState.COMPLETED.getValue())
                        .price(0D)
                        .build());
                productOrderItem.getProduct().getProductCharacteristic().forEach(characteristicDTO -> {
                    UUID characteristicId = UUID.randomUUID();
                    characteristicList.add(Characteristic.builder()
                            .id(String.valueOf(characteristicId))
                            .orderItemId(String.valueOf(itemId))
                            .code(characteristicDTO.getCode())
                            .value(characteristicDTO.getValue())
                            .valueType(characteristicDTO.getValueType())
                            .name(characteristicDTO.getName())
                            .createBy(username)
                            .updateBy(username)
                            .build());
                });
            });
            var saveAllItemMono = orderItemRepository.saveAll(orderItemList).collectList();
            var saveAllCharacteristicMono =
                    characteristicRepository.saveAll(characteristicList).collectList();
            var saveOrder = saveOrderV2(
                    orderId,
                    orderCode,
                    userId,
                    individualId,
                    0D,
                    Constants.Currency.VND,
                    null,
                    null,
                    null,
                    null,
                    OrderState.COMPLETED.getValue(),
                    Constants.OrderType.PAID_ORDER,
                    username,
                    username);
            return Mono.zip(saveAllItemMono, saveAllCharacteristicMono, saveOrder)
                    .flatMap(saveAllItemAndCharacteristic -> {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), placeOrderResponse.get()));
                    })
                    .doOnError(err -> log.error("Exception when saveOrder ", err))
                    .onErrorResume(
                            throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "")));
        });
    }

    /**
     * Ham xy ly lay thong tin product tu CM sau khi call getGroupsInfo
     *
     * @param responseCM
     * @return
     */
    private HandleCharacteristicDTO handleProductCharacteristic(ResponseCM responseCM, Long groupId) {
        GroupsDTO groupsDTO = responseCM.getGroupsDTOList().stream()
                .filter(s -> DataUtil.safeEqual(s.getGroupId(), groupId))
                .findFirst()
                .orElse(null);
        if (groupsDTO == null) {
            throw new BusinessException("GROUP_INFO_NOT_FOUND_2", "group.info.not.found");
        }
        ProductOfferingRef productOfferingRef = new ProductOfferingRef();
        productOfferingRef.setTelecomServiceId(Constants.Common.CA_TELECOM_SERVICE_ID);

        List<CharacteristicDTO> productCharacteristic = new ArrayList<>();
        productCharacteristic.add(new CharacteristicDTO(
                Constants.CharacteristicKey.GROUP_NAME, Constants.DataType.STRING, groupsDTO.getName(), null, null));
        productCharacteristic.add(new CharacteristicDTO(
                Constants.CharacteristicKey.GROUP_ID,
                Constants.DataType.LONG,
                DataUtil.safeToString(groupsDTO.getGroupId()),
                null,
                null));
        if (!DataUtil.isNullOrEmpty(groupsDTO.getListGroupsExtDTO())) {
            for (GroupsExtDTO groupsExtDTO : groupsDTO.getListGroupsExtDTO()) {
                // lay thong tin thuoc tinh mo rong tu response
                if (Constants.CharacteristicKey.GROUP_ISDN_SME.equals(groupsExtDTO.getExtKey())) {
                    productCharacteristic.add(new CharacteristicDTO(
                            Constants.CharacteristicKey.MAIN_ISDN,
                            Constants.DataType.STRING,
                            groupsExtDTO.getExtValue(),
                            null,
                            null));
                }
                // lay thong tin thuoc tinh san pham tren bccs product
                if (Constants.CharacteristicKey.GROUP_PRODUCT_OFERRING_CODE.equals(groupsExtDTO.getExtKey())) {
                    productOfferingRef.setCode(groupsExtDTO.getExtValue());
                }
            }
        }
        HandleCharacteristicDTO result = new HandleCharacteristicDTO();
        result.setProductCharacteristic(productCharacteristic);
        result.setProductOfferingRef(productOfferingRef);
        result.setGroupsDTO(groupsDTO);
        return result;
    }

    /**
     * Ham xu ly tao don hang chua thanh toan
     *
     * @param productCharacteristic
     * @param productOfferingRef
     * @param orderType
     * @param userId
     * @param userName
     * @return
     */
    private Mono<DataResponse> handleCreateNotPaidOrder(
            List<CharacteristicDTO> productCharacteristic,
            ProductOfferingRef productOfferingRef,
            String orderType,
            String userId,
            String userName,
            String portalAccountId) {
        Product product = new Product();
        product.setProductCharacteristic(productCharacteristic);
        product.setIsBundle(false);

        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setProduct(product);
        productOrderItem.setId(1L);
        productOrderItem.setQuantity(1L);
        productOrderItem.setAction(Constants.OrderProductOrderItem.ACTION_ADD);
        productOrderItem.setProductOfferingRef(productOfferingRef);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPortalAccountId(portalAccountId);

        PlaceOrderData placeOrderData = new PlaceOrderData();
        placeOrderData.setProductOrderItem(Collections.singletonList(productOrderItem));
        placeOrderData.setTransactionPlace(Constants.TransactionPlace.HOME);
        placeOrderData.setSystemType(Constants.SystemType.SME_HUB);
        placeOrderData.setSystemStaffCode(systemUser);
        placeOrderData.setCustomer(customerDTO);

        return createNotPaidOrder(orderType, placeOrderData, userId, userName, portalAccountId)
                .flatMap(Mono::just);
    }

    @Override
    public Mono<DataResponse> updateCAgroupMember(AfterSaleGroupCARequest request) {
        // validate thong tin
        String orderType = Constants.OrderType.CHANGE_MEMBER_GROUP_CA;
        validateCreateOrderRequest(request, orderType);
        return Mono.zip(
                        SecurityUtils.getCurrentUser(),
                        authClient.getTrustedIdNoOrganization(request.getOrganizationId()))
                .flatMap(tuple1 -> {
                    // kiem tra so giay to khac null
                    if (DataUtil.isNullOrEmpty(tuple1.getT2())) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                    }
                    GetGroupsCAinfoRequest requestGroupInfo = new GetGroupsCAinfoRequest();
                    String groupCode = request.getGroupCode();
                    requestGroupInfo.setGroupCode(groupCode);
                    requestGroupInfo.setGroupId(request.getGroupId());
                    if (request.getGroupId() == null) {
                        requestGroupInfo.setGroupId(DataUtil.safeToLong(groupCode.replace("CA", "")));
                    }
                    requestGroupInfo.setIdNo(tuple1.getT2().get(0));
                    requestGroupInfo.setStartAt(1);
                    requestGroupInfo.setRowNum(10);

                    TokenUser tokenUser = tuple1.getT1();
                    String userName = tokenUser.getUsername();
                    String userId = tokenUser.getId();

                    return Mono.zip(
                                    cmPortalClient.getGroupsInfo(requestGroupInfo),
                                    authClient.findIndividualIdByUserId(userId, request.getOrganizationId()))
                            .flatMap(result -> {
                                if (result.getT1().isEmpty()) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                                }
                                ResponseCM responseCM = result.getT1().get();
                                // neu khong phai ket qua thanh cong thi tra list rong
                                if (!Constants.BCCSCmSystem.SUCCESS_CODE.equals(responseCM.getCode())
                                        || DataUtil.isNullOrEmpty(responseCM.getGroupsDTOList())) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                                }
                                HandleCharacteristicDTO handleCharacteristicDTO =
                                        handleProductCharacteristic(responseCM, requestGroupInfo.getGroupId());
                                handleCharacteristicDTO
                                        .getProductCharacteristic()
                                        .add(new CharacteristicDTO(
                                                SUB_ISDN, Constants.DataType.STRING, request.getIsdn(), null, null));
                                handleCharacteristicDTO
                                        .getProductCharacteristic()
                                        .add(new CharacteristicDTO(
                                                Constants.CharacteristicKey.NUMBER_OF_SIGNATURES,
                                                Constants.DataType.LONG,
                                                DataUtil.parseObjectToString(request.getTotalSign()),
                                                null,
                                                null));
                                // thuc hien tao don hang chua thanh toan
                                return handleCreateNotPaidOrder(
                                        handleCharacteristicDTO.getProductCharacteristic(),
                                        handleCharacteristicDTO.getProductOfferingRef(),
                                        orderType,
                                        userId,
                                        userName,
                                        result.getT2());
                            });
                });
    }

    @Override
    public Mono<DataResponse> removeCAgroupMember(AfterSaleGroupCARequest request) {
        // validate thong tin
        String orderType = Constants.OrderType.DELETE_MEMBER_GROUP_CA;
        validateCreateOrderRequest(request, orderType);
        return Mono.zip(
                        SecurityUtils.getCurrentUser(),
                        authClient.getTrustedIdNoOrganization(request.getOrganizationId()))
                .flatMap(tuple1 -> {
                    // kiem tra so giay to khac null
                    if (DataUtil.isNullOrEmpty(tuple1.getT2())) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                    }
                    GetGroupsCAinfoRequest requestGroupInfo = new GetGroupsCAinfoRequest();
                    requestGroupInfo.setGroupId(request.getGroupId());
                    requestGroupInfo.setIdNo(tuple1.getT2().get(0));
                    requestGroupInfo.setIsdn(request.getIsdn());
                    requestGroupInfo.setStartAt(1);
                    requestGroupInfo.setRowNum(10);

                    return Mono.zip(
                                    cmPortalClient.getGroupsMembersInfo(requestGroupInfo),
                                    authClient.findIndividualIdByUserId(
                                            tuple1.getT1().getId(), request.getOrganizationId()))
                            .flatMap(result -> {
                                if (result.getT1().isEmpty()) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                                }
                                ResponseCM responseCM = result.getT1().get();
                                // neu khong phai ket qua thanh cong thi tra list rong
                                if (!Constants.BCCSCmSystem.SUCCESS_CODE.equals(responseCM.getCode())
                                        || DataUtil.isNullOrEmpty(responseCM.getGroupsDTOList())) {
                                    return Mono.error(
                                            new BusinessException(CommonErrorCode.BAD_REQUEST, PARAMS_INVALID));
                                }
                                HandleCharacteristicDTO handleCharacteristicDTO =
                                        handleProductCharacteristic(responseCM, requestGroupInfo.getGroupId());
                                if (!DataUtil.isNullOrEmpty(
                                        handleCharacteristicDTO.getGroupsDTO().getMemberList())) {
                                    GroupsMemberDTO memberDTO = handleCharacteristicDTO
                                            .getGroupsDTO()
                                            .getMemberList()
                                            .get(0);
                                    handleCharacteristicDTO
                                            .getProductCharacteristic()
                                            .add(new CharacteristicDTO(
                                                    Constants.CharacteristicKey.GROUP_MEMBER_ID,
                                                    Constants.DataType.STRING,
                                                    DataUtil.safeToString(memberDTO.getGroupMemberId()),
                                                    null,
                                                    null));
                                }
                                handleCharacteristicDTO
                                        .getProductCharacteristic()
                                        .add(new CharacteristicDTO(
                                                SUB_ISDN, Constants.DataType.STRING, request.getIsdn(), null, null));

                                TokenUser tokenUser = tuple1.getT1();
                                String userName = tokenUser.getUsername();
                                String userId = tokenUser.getId();
                                // thuc hien tao don hang chua thanh toan
                                return handleCreateNotPaidOrder(
                                        handleCharacteristicDTO.getProductCharacteristic(),
                                        handleCharacteristicDTO.getProductOfferingRef(),
                                        orderType,
                                        userId,
                                        userName,
                                        result.getT2());
                            });
                });
    }

    private void validateCreateOrderRequest(AfterSaleGroupCARequest request, String orderType) {
        if (request == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_INVALID);
        }
        if (DataUtil.isNullOrEmpty(request.getOrganizationId())) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "validate.create.afterSale.CA.order.null.organizationId");
        }
        if (DataUtil.isNullOrEmpty(request.getGroupCode())) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "validate.create.afterSale.CA.order.null.groupCode");
        }
        if (Constants.OrderType.ADD_MEMBER_GROUP_CA.equals(orderType)) {
            if (DataUtil.isNullOrEmpty(request.getLstSubscriberCA())) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "validate.create.afterSale.CA.order.null.lstSubscriberCA");
            }
        } else if (Constants.OrderType.CHANGE_MEMBER_GROUP_CA.equals(orderType)) {
            if (DataUtil.isNullOrEmpty(request.getIsdn())) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "validate.update.afterSale.CA.order.null.isdn");
            }
            if (DataUtil.isNullOrEmpty(request.getTotalSign())) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "validate.update.afterSale.CA.order.null.totalSign");
            }
        } else if (Constants.OrderType.DELETE_MEMBER_GROUP_CA.equals(orderType)) {
            if (DataUtil.isNullOrEmpty(request.getIsdn())) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "validate.delete.afterSale.CA.order.null.isdn");
            }
        }
    }

    private Mono<Boolean> saveOrderV2(
            UUID orderId,
            String orderCode,
            String customerId,
            String individualId,
            Double totalFee,
            String currency,
            String areaCode,
            String provinceName,
            String districtName,
            String precinctName,
            int state,
            String type,
            String createBy,
            String updateBy) {
        var saveOrderMono = orderRepository.insertOrder(
                orderId.toString(),
                orderCode,
                customerId,
                individualId,
                totalFee,
                Constants.Currency.VND,
                areaCode,
                provinceName,
                districtName,
                precinctName,
                state,
                Constants.OrderType.PAID_ORDER,
                createBy,
                updateBy);
        return saveOrderMono.map(rs -> true).switchIfEmpty(Mono.just(true));
    }

    @Override
    public Mono<DataResponse> connectCASelfcare(CreateOrderPaidRequest request) {
        return Mono.zip(validateConnectCARequest(request), SecurityUtils.getCurrentUser())
                .flatMap(validateUser -> authClient
                        .findIndividualIdByUserId(validateUser.getT2().getId(), request.getOrganizationId())
                        .flatMap(individualId -> {
                            PlacePaidOrderData placePaidOrderData =
                                    OrderClientUtils.mapDataOrderBccs(request, systemUser, individualId);
                            UUID orderId = UUID.randomUUID();
                            TokenUser user = validateUser.getT2();
                            String userId = user.getId();
                            String username = user.getUsername();
                            CustomerDTO customerDTO = placePaidOrderData.getCustomer();
                            return saveOrderV2(
                                            orderId,
                                            null,
                                            userId,
                                            individualId,
                                            request.getTotalFee(),
                                            "VND",
                                            customerDTO.getAreaCode(),
                                            customerDTO.getProvince(),
                                            customerDTO.getDistrict(),
                                            customerDTO.getPrecinct(),
                                            OrderState.NEW.getValue(),
                                            Constants.OrderType.PAID_ORDER,
                                            username,
                                            username)
                                    .flatMap(order -> {
                                        UUID itemId = UUID.randomUUID();
                                        OrderItem orderItem = OrderItem.builder()
                                                .id(itemId.toString())
                                                .orderId(orderId.toString())
                                                .price(placePaidOrderData.getTotalFee())
                                                .productId(request.getTemplateId())
                                                .telecomServiceId(DataUtil.safeToString(request.getServiceId()))
                                                // Bo sung alias
                                                .telecomServiceAlias(DataUtil.safeToString(request.getServiceAlias()))
                                                .telecomServiceName(DataUtil.safeToString(request.getServiceName()))
                                                .quantity(1)
                                                .currency("VND")
                                                .status(ACTIVE)
                                                .createBy(username)
                                                .updateBy(username)
                                                .state(OrderState.NEW.getValue()) // update: bo sung
                                                // insert state
                                                // = 0 (NEW)
                                                .build();
                                        var saveAllItemMono = AppUtils.insertData(orderItemRepository.save(orderItem));

                                        OrderBccsData orderBccsData = new OrderBccsData();
                                        orderBccsData.setOrderId(orderId.toString());
                                        placePaidOrderData.setExtCode(orderId.toString());
                                        orderBccsData.setData(DataUtil.parseObjectToString(placePaidOrderData));
                                        orderBccsData.setOrderType(CONNECT_EZBUY);
                                        orderBccsData.setStatus(ACTIVE);
                                        orderBccsData.setCreateBy(username);
                                        orderBccsData.setCreateAt(LocalDateTime.now());
                                        var saveBccsOrder =
                                                AppUtils.insertData(orderBccsDataRepository.save(orderBccsData));
                                        // lay link redirect sang payment de thanh toan
                                        ProductPaymentRequest createLinkRequest = new ProductPaymentRequest();
                                        createLinkRequest.setOrderCode(DataUtil.safeToString(orderId));
                                        createLinkRequest.setCancelUrl(cancelUrl + orderId);
                                        createLinkRequest.setReturnUrl(returnUrl + orderId);
                                        createLinkRequest.setOrderType(SELFCARE_CONNECT_CA);
                                        createLinkRequest.setTelecomServiceAlias(request.getTelecomServiceAlias());
                                        createLinkRequest.setTotalFee(DataUtil.safeToLong(request.getTotalFee()));
                                        return Mono.zip(
                                                        paymentClient.getLinkCheckOut(createLinkRequest),
                                                        saveBccsOrder,
                                                        saveAllItemMono)
                                                .flatMap(rs -> {
                                                    if (rs.getT1().isEmpty()) {
                                                        return Mono.error(new BusinessException(
                                                                CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                                "create.link.checkout.internal"));
                                                    }
                                                    String createLinkCheckout =
                                                            rs.getT1().get().getCheckoutLink();
                                                    return Mono.just(new DataResponse<>(
                                                            Translator.toLocaleVi(SUCCESS), createLinkCheckout));
                                                });
                                    });
                        }));
    }

    public Mono<Boolean> validateConnectCARequest(CreateOrderPaidRequest request) {
        // trim customer info
        if (DataUtil.isNullOrEmpty(request.getOrganizationId())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "connect.ca.error.organization.id.empty"));
        }
        if (request.getTotalFee() == null) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi(PARAMS_REQUIRED, "totalFee")));
        }
        Long telecomServiceId = request.getServiceId();
        if (Constants.TelecomServiceId.CA.equals(telecomServiceId)) {
            if (request.getTotalFee() <= 0D) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "connect.ca.error.order.item.price.invalid"));
            }
        }
        // validate customer info

        String name = DataUtil.safeTrim(request.getCompanyName());
        if (DataUtil.isNullOrEmpty(name)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.name.empty"));
        }
        if (name.length() > 100) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.name.invalid"));
        }
        String email = DataUtil.safeTrim(request.getCompanyEmail());
        if (DataUtil.isNullOrEmpty(email)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.email.empty"));
        }
        if (email.length() > 50) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.email.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(email) && !ValidateUtils.validateRegex(email, Regex.EMAIL_REGEX)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.email.invalid"));
        }
        String phone = DataUtil.safeTrim(request.getCompanyPhone());
        if (DataUtil.isNullOrEmpty(phone)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.phone.empty"));
        }
        if (!ValidateUtils.validatePhone(phone)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.phone.invalid"));
        }
        String provinceCode = DataUtil.safeTrim(request.getCompanyProvince());
        if (DataUtil.isNullOrEmpty(provinceCode)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.province.code.empty"));
        }
        if (provinceCode.length() > 4) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "province.code.invalid"));
        }
        String districtCode = DataUtil.safeTrim(request.getCompanyDistrict());
        if (DataUtil.isNullOrEmpty(districtCode)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.district.code.empty"));
        }
        if (districtCode.length() > 4) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "district.code.invalid"));
        }
        String precinctCode = DataUtil.safeTrim(request.getCompanyPrecinct());
        if (DataUtil.isNullOrEmpty(precinctCode)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.precinct.code.empty"));
        }
        if (precinctCode.length() > 4) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "precinct.code.invalid"));
        }
        String address = DataUtil.safeTrim(request.getCompanyDetailAddress());
        if (DataUtil.isNullOrEmpty(address)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.address.empty"));
        }
        if (address.getBytes().length > 500) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.address.invalid"));
        }

        if (DataUtil.isNullOrEmpty(request.getBusinessLicense())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.idNo.empty"));
        }
        if (request.getBusinessLicense().length() > 36) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "connect.ca.error.customer.info.idNo.invalid"));
        }
        // bo sung dynamic validate cho tham so chinh sach xu ly bao ve du lieu
        // lay cau hinh chinh sach
        if (!DataUtil.safeEqual(request.getIsBusinessAuth(), true)) {
            return settingClient.getConfDataPolicy(DATA_POLICY).flatMap(policyConfList -> {
                if (DataUtil.isNullOrEmpty(policyConfList)) {
                    return Mono.just(true);
                }
                if (request.getDataPolicy() == null) {
                    request.setDataPolicy(new HashMap<>());
                }
                // validate cau hinh chinh sach voi danh sach chinh sach truyen tu param
                for (OptionSetValueDTO policyConf : policyConfList) {
                    if (policyConf.getRequired()) {
                        if (!request.getDataPolicy().containsKey(policyConf.getCode())
                                || DataUtil.isNullOrEmpty(
                                        request.getDataPolicy().get(policyConf.getCode())) // neu
                        // khong
                        // truyen
                        // chinh
                        // sach
                        // required
                        // =>
                        // loi
                        ) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    "connect.ca.error.data.policy.required",
                                    policyConf.getCode()));
                        }
                        if (!DataUtil.safeEqual(
                                request.getDataPolicy().get(policyConf.getCode()), VALUE_1_STRING)) { // neu
                            // truyen
                            // chinh
                            // sach
                            // required
                            // voi
                            // value
                            // !=
                            // 1
                            // =>
                            // loi
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    "connect.ca.error.data.policy.value.invalid",
                                    policyConf.getCode()));
                        }
                    } else if (request.getDataPolicy().containsKey(policyConf.getCode())
                            && !DataUtil.safeEqual(request.getDataPolicy().get(policyConf.getCode()), VALUE_0_STRING)
                            && !DataUtil.safeEqual(
                                    request.getDataPolicy().get(policyConf.getCode()), VALUE_1_STRING) // neu
                    // truyen
                    // chinh
                    // sach
                    // not
                    // required
                    // nhung
                    // value
                    // khong
                    // hop
                    // le
                    // =>
                    // loi
                    ) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS,
                                "connect.ca.error.data.policy.value.invalid",
                                policyConf.getCode()));
                    }
                }
                return Mono.just(true);
            });
        }
        return Mono.just(true);
    }

    @Override
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getProfileKHDN(CreateOrderPaidRequest createOrderPaidRequest) {
        PlacePaidOrderData placePaidOrderData =
                OrderClientUtils.mapDataOrderBccs(createOrderPaidRequest, systemUser, null);
        String data = DataUtil.parseObjectToString(placePaidOrderData);
        return orderV2Client
                .getProfileKHDN(data, null, null)
                .map(response -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
    }

    @Override
    public Mono<DataResponse> getAdvice(CreatePreOrderRequest request) {
        return preOrderFlow(request, false);
    }

    @Transactional
    @Override
    public Mono<DataResponse> createOrderHistory(CreateOrderHistoryRequest requestBegin) {
        return validateAndFormatCreateOrderHistoryRequest(requestBegin)
                .flatMap(customerId -> Mono.zip(
                                convertRequest(requestBegin),
                                orderRepository.findFirstByOrderIdAndOrderCode(
                                                requestBegin.getOrder().getExtCode(),
                                                requestBegin.getOrder().getOrderCode())
                                        .collectList())
                        .flatMap(tuple -> {
                            CreateOrderHistoryRequest request = tuple.getT1();
                            List<Order> lstOrderDb = tuple.getT2();

                            if (DataUtil.isNullOrEmpty(lstOrderDb)) {
                                // gen order id
                                UUID orderId = UUID.randomUUID();
                                // insert order
                                Order order = request.getOrder();
                                String name = order.getName();
                                return saveOrderExt(
                                                orderId,
                                                order.getOrderCode(),
                                                customerId,
                                                request.getIndividualId(),
                                                order.getTotalFee(),
                                                request.getOrder().getSystemType(),
                                                order.getAreaCode(),
                                                order.getProvince(),
                                                order.getDistrict(),
                                                order.getPrecinct(),
                                                order.getDetailAddress(),
                                                order.getDescription(),
                                                order.getIdNo(),
                                                name,
                                                order.getEmail(),
                                                order.getPhone(),
                                                order.getState())
                                        .flatMap(insertOrder -> {
                                            if (insertOrder.isEmpty()) {
                                                return Mono.error(new BusinessException(
                                                        CommonErrorCode.SQL_ERROR,
                                                        "create.order.history.common.error"));
                                            }
                                            // tao order item list de insert
                                            List<OrderItem> itemList = request.getOrderItemList().stream()
                                                    .map(item -> OrderItem.builder()
                                                            .id(UUID.randomUUID()
                                                                    .toString())
                                                            .orderId(orderId.toString())
                                                            .name(item.getName())
                                                            .price(item.getPrice())
                                                            .originPrice(item.getOriginPrice())
                                                            .productId(item.getProductId())
                                                            .telecomServiceId(
                                                                    DataUtil.safeToString(item.getTelecomServiceId()))
                                                            // Bo sung alias
                                                            .telecomServiceAlias(DataUtil.safeToString(
                                                                    item.getTelecomServiceAlias()))
                                                            .telecomServiceName(
                                                                    DataUtil.safeToString(item.getTelecomServiceName()))
                                                            .quantity(item.getQuantity())
                                                            .currency(CURRENCY_VND)
                                                            .status(ACTIVE)
                                                            .duration(item.getDuration())
                                                            .createBy(name)
                                                            .updateBy(name)
                                                            // bo sung cac truong
                                                            .description(item.getDescription())
                                                            .reviewContent(item.getReviewContent())
                                                            .rating(item.getRating())
                                                            .orderCode(item.getOrderCode())
                                                            .subscriberId(item.getSubscriberId())
                                                            .isBundle(item.getIsBundle())
                                                            .accountId(item.getAccountId())
                                                            .state(item.getState())
                                                            .productCode(item.getProductCode())
                                                            .createAt(LocalDateTime.now())
                                                            .updateAt(LocalDateTime.now())
                                                            .build())
                                                    .collect(Collectors.toList());
                                            // tao response
                                            DataResponse response = new DataResponse<>(
                                                    Translator.toLocale(SUCCESS), orderId.toString());
                                            // insert order item vao DB
                                            return orderItemRepository
                                                    .saveAll(itemList)
                                                    .collectList()
                                                    .flatMap(insertOrderItem -> {
                                                        if (DataUtil.isNullOrEmpty(insertOrderItem)) {
                                                            return Mono.error(new BusinessException(
                                                                    CommonErrorCode.SQL_ERROR,
                                                                    "create.order.history.common.error"));
                                                        }
                                                        return Mono.just(response)
                                                                .switchIfEmpty(Mono.just(response));
                                                    });
                                        });
                            } else {
                                OrderDTO order = requestBegin.getOrder();
                                DataResponse response =
                                        new DataResponse<>(Translator.toLocale(SUCCESS), order.getExtCode());
                                return Mono.zip(
                                                orderRepository.updateStateAndOrderCode(
                                                        order.getState(),
                                                        "ORDER_SYSTEM",
                                                        order.getExtCode(),
                                                        order.getOrderCode()),
                                                handleUpdateTrustStatus(order.getOrderCode(), order.getState()))
                                        .flatMap(updateOrder -> {
                                            String rs = updateOrder.getT2();
                                            return Mono.just(response).switchIfEmpty(Mono.just(response));
                                        });
                            }
                        }));
    }

    public Mono<String> handleUpdateTrustStatus(String orderCode, Integer orderState) {
        return orderExtRepository
                .findByOrderCodeAndCodeAndStatus(orderCode, ORDER_TRUST_IDENTITY)
                .collectList()
                .flatMap(orderExtList -> {
                    if (DataUtil.isNullOrEmpty(orderExtList)) {
                        log.info("OrderExt not found");
                        return Mono.just(EMPTY);
                    }
                    OrderExt orderExt = orderExtList.getFirst();
                    Integer trustStatus;
                    if (DataUtil.safeEqual(orderState, 3)) {
                        trustStatus = NOT_SIGN;
                    } else {
                        trustStatus = WAIT_CREATE_CTS;
                    }
                    // orderExt.getValue() la id cua tenant_identify -> lay ra create_by de gui mail
                    UpdateTenantTrustStatusRequest request =
                            new UpdateTenantTrustStatusRequest(orderExt.getValue(), trustStatus);
                    return authClient.updateTrustStatusNotSign(request);
                });
    }

    private Mono<CreateOrderHistoryRequest> convertRequest(CreateOrderHistoryRequest request) {
        List<String> lstTelecomServiceId = request.getOrderItemList().stream()
                .map(OrderItem::getTelecomServiceId)
                .collect(Collectors.toList());
        List<String> lstTelecomServiceAlias = request.getOrderItemList().stream()
                .map(OrderItem::getTelecomServiceAlias)
                .filter(telecomServiceAlias -> !DataUtil.isNullOrEmpty(telecomServiceAlias))
                .collect(Collectors.toList());
        if (lstTelecomServiceAlias.size() < request.getOrderItemList().size()) {
            return settingClient.getTelecomData(null, null, lstTelecomServiceId).flatMap(telecomDTOS -> {
                if (DataUtil.isNullOrEmpty(telecomDTOS)) {
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INVALID_PARAMS, "telecomServiceId khong ton tai"));
                }
                List<OrderItem> orderItemList = request.getOrderItemList();
                for (OrderItem orderItem : orderItemList) {
                    for (TelecomDTO telecom : telecomDTOS) {
                        if (orderItem.getTelecomServiceId().equals(telecom.getOriginId())) {
                            orderItem.setTelecomServiceAlias(telecom.getServiceAlias());
                            break; // break khi da tim duoc telecomServiceDTO phu hop
                        }
                    }
                }
                request.setOrderItemList(orderItemList);
                return Mono.just(request);
            });
        }
        return Mono.just(request);
    }

    private Mono<Optional<UUID>> saveOrderExt(
            UUID orderId,
            String orderCode,
            String customerId,
            String individualId,
            Double price,
            String systemType,
            String areaCode,
            String provinceName,
            String districtName,
            String precinctName,
            String detailAddress,
            String description,
            String idNo,
            String name,
            String email,
            String phone,
            Integer state) {
        // insert order
        var saveOrderMono = orderRepository.insertOrderExt(
                orderId.toString(),
                orderCode,
                customerId,
                individualId,
                price,
                CURRENCY_VND,
                areaCode,
                provinceName,
                districtName,
                precinctName,
                systemType,
                systemType,
                detailAddress,
                state,
                description,
                Constants.OrderType.ORDER,
                idNo,
                name,
                email,
                phone);
        return saveOrderMono
                .map(order -> Optional.of(orderId))
                .switchIfEmpty(Mono.just(Optional.of(orderId)))
                .doOnError(err -> log.error("SOE_0000001: Exception when insert Order: " + err.getMessage()))
                .onErrorResume(throwable -> Mono.just(Optional.empty()));
    }

    public Mono<String> validateAndFormatCreateOrderHistoryRequest(CreateOrderHistoryRequest request) {
        // validate order null
        OrderDTO order = request.getOrder();
        if (order == null) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.order.history.order.empty"));
        }
        // validate order item trong
        List<OrderItem> orderItemList = request.getOrderItemList();
        if (DataUtil.isNullOrEmpty(orderItemList)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.product.required"));
        }
        if (DataUtil.isNullOrEmpty(request.getIndividualId())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.order.history.individual.id.empty"));
        }
        // trim order
        trimOrder(request.getOrder());
        List<String> telecomServiceAliasList =
                orderItemList.stream().map(OrderItem::getTelecomServiceAlias).collect(Collectors.toList());
        GetOrderFieldConfigRequest getOrderFieldConfigRequest = GetOrderFieldConfigRequest.builder()
                .orderType(Constants.OrderType.ORDER)
                .lstTelecomServiceAlias(telecomServiceAliasList)
                .build();
        return Mono.zip(
                        getConfigOrder(getOrderFieldConfigRequest),
                        authClient.findUserIdByIndividualIdAndActive(
                                request.getIndividualId().trim())) // update lay
                // user id tu
                // individual id
                .flatMap(orderConfig -> {
                    // validate cac truong cua order
                    if (orderConfig.getT2().isEmpty()
                            || DataUtil.isNullOrEmpty(orderConfig.getT2().get())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "create.order.history.user.id.not.found"));
                    }
                    OrderFieldConfigDTO orderFieldConfig = orderConfig.getT1();
                    // check bat buoc truyen area code, province, district, precinct data
                    boolean areaCodeRequired =
                            DataUtil.safeEqual(orderFieldConfig.getAreaCode(), Constants.OrderConfigType.REQUIRED);
                    if (areaCodeRequired) {
                        if (DataUtil.isNullOrEmpty(order.getAreaCode())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    PARAMS_REQUIRED,
                                    Constants.CustomerInfoProperty.AREA_CODE));
                        }
                        if (DataUtil.isNullOrEmpty(order.getProvince())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    PARAMS_REQUIRED,
                                    Constants.CustomerInfoProperty.PROVINCE));
                        }
                        if (DataUtil.isNullOrEmpty(order.getDistrict())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    PARAMS_REQUIRED,
                                    Constants.CustomerInfoProperty.DISTRICT));
                        }
                        if (DataUtil.isNullOrEmpty(order.getPrecinct())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    PARAMS_REQUIRED,
                                    Constants.CustomerInfoProperty.PRECINCT));
                        }
                    }
                    if (DataUtil.safeEqual(orderFieldConfig.getName(), Constants.OrderConfigType.REQUIRED)
                            && DataUtil.isNullOrEmpty(order.getName())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "name"));
                    }
                    if (DataUtil.safeEqual(orderFieldConfig.getDetailAddress(), Constants.OrderConfigType.REQUIRED)
                            && DataUtil.isNullOrEmpty(order.getDetailAddress())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "detailAddress"));
                    }
                    if (DataUtil.safeEqual(orderFieldConfig.getEmail(), Constants.OrderConfigType.REQUIRED)
                            && DataUtil.isNullOrEmpty(order.getEmail())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "email"));
                    }
                    if (DataUtil.safeEqual(orderFieldConfig.getPhone(), Constants.OrderConfigType.REQUIRED)
                            && DataUtil.isNullOrEmpty(order.getPhone())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "phone"));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getName())
                            && order.getName().getBytes().length > 500) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "name.exceed.length"));
                    }
                    if (DataUtil.isNullOrEmpty(order.getSystemType())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS,
                                PARAMS_REQUIRED,
                                Constants.CustomerInfoProperty.SYSTEM_TYPE));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getSystemType())
                            && order.getSystemType().getBytes().length > 30) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS,
                                "create.order.history.error.system.type.exceed.length"));
                    }
                    // bo sung validate state tu request
                    if (DataUtil.isNullOrEmpty(order.getState())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "order.state"));
                    }
                    if (!containsState(order.getState())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "create.order.history.error.order.state.invalid"));
                    }
                    String email = order.getEmail();
                    if (!DataUtil.isNullOrEmpty(email)) {
                        if (email.getBytes().length > 50) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.exceed.length"));
                        }
                        if (!ValidateUtils.validateRegex(email, Regex.EMAIL_REGEX)) {
                            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "email.invalid"));
                        }
                    }
                    if (!DataUtil.isNullOrEmpty(order.getPhone()) && !ValidateUtils.validatePhone(order.getPhone())) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "phone.invalid"));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getAreaCode())
                            && order.getAreaCode().getBytes().length > 20) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "area.code.invalid"));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getProvince())
                            && order.getProvince().getBytes().length > 50) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "province.code.invalid"));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getDistrict())
                            && order.getDistrict().getBytes().length > 50) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "district.code.invalid"));
                    }
                    if (!DataUtil.isNullOrEmpty(order.getPrecinct())
                            && order.getPrecinct().getBytes().length > 50) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "precinct.code.invalid"));
                    }
                    // validate order item
                    for (OrderItem orderItem : orderItemList) {
                        // trim order item
                        trimOrderItem(orderItem);
                        if (DataUtil.isNullOrEmpty(orderItem.getProductId())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "productId"));
                        }
                        if (DataUtil.isNullOrEmpty(orderItem.getTelecomServiceId())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "telecomServiceId"));
                        }
                        if (orderItem.getQuantity() == null) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "quantity"));
                        }
                        if (DataUtil.safeToInt(orderItem.getQuantity()) <= 0) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.quantity.invalid"));
                        }
                        if (!DataUtil.isNullOrEmpty(orderItem.getProductCode())
                                && orderItem.getProductCode().getBytes().length > 50) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "create.order.history.error.product.code.invalid"));
                        }
                        // bo sung validate state tu request
                        if (DataUtil.isNullOrEmpty(orderItem.getState())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, PARAMS_REQUIRED, "orderItem.state"));
                        }
                        if (!containsState(orderItem.getState())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    "create.order.history.error.order.item.state.invalid"));
                        }
                    }
                    return Mono.just(orderConfig.getT2().get());
                });
    }

    private boolean containsState(int state) {
        for (OrderState orderState : OrderState.values()) {
            if (DataUtil.safeEqual(orderState.getValue(), state)) {
                return true;
            }
        }
        return false;
    }

    private void trimOrder(OrderDTO order) {
        order.setOrderCode(DataUtil.safeTrim(order.getOrderCode()));
        order.setCustomerId(DataUtil.safeTrim(order.getCustomerId()));
        order.setCustomerId(DataUtil.safeTrim(order.getCustomerId()));
        order.setAreaCode(DataUtil.safeTrim(order.getAreaCode()));
        order.setProvince(DataUtil.safeTrim(order.getProvince()));
        order.setDistrict(DataUtil.safeTrim(order.getDistrict()));
        order.setPrecinct(DataUtil.safeTrim(order.getPrecinct()));
        order.setDescription(DataUtil.safeTrim(order.getDescription()));
        order.setDetailAddress(DataUtil.safeTrim(order.getDetailAddress()));
        order.setIdNo(DataUtil.safeTrim(order.getIdNo()));
        order.setName(DataUtil.safeTrim(order.getName()));
        order.setEmail(DataUtil.safeTrim(order.getEmail()));
        order.setPhone(DataUtil.safeTrim(order.getPhone()));
        order.setSystemType(DataUtil.safeTrim(order.getSystemType()));
    }

    private void trimOrderItem(OrderItem orderItem) {
        orderItem.setProductId(DataUtil.safeTrim(orderItem.getProductId()));
        orderItem.setTelecomServiceId(DataUtil.safeTrim(orderItem.getTelecomServiceId()));
        orderItem.setName(DataUtil.safeTrim(orderItem.getName()));
        orderItem.setCurrency(DataUtil.safeTrim(orderItem.getCurrency()));
        orderItem.setDescription(DataUtil.safeTrim(orderItem.getDescription()));
        orderItem.setReviewContent(DataUtil.safeTrim(orderItem.getReviewContent()));
        orderItem.setDuration(DataUtil.safeTrim(orderItem.getDuration()));
        orderItem.setTelecomServiceName(DataUtil.safeTrim(orderItem.getTelecomServiceName()));
        orderItem.setOrderCode(DataUtil.safeTrim(orderItem.getOrderCode()));
        orderItem.setSubscriberId(DataUtil.safeTrim(orderItem.getSubscriberId()));
        orderItem.setAccountId(DataUtil.safeTrim(orderItem.getAccountId()));
        orderItem.setProductCode(DataUtil.safeTrim(orderItem.getProductCode()));
    }

    private Mono<DataResponse> preOrderFlow(CreatePreOrderRequest request, boolean getAdvice) {

        request.setGetAdvice(getAdvice);
        return Mono.zip(validateAndFormatCreatePreOrderRequest(request), SecurityUtils.getCurrentUser())
                .flatMap(validateResp -> {
                    List<String> templateIds = request.getProductList().stream()
                            .map(OrderProductDTO::getTemplateId)
                            .collect(Collectors.toList());
                    return Mono.zip(
                            productClient.getProductInfo(templateIds),
                            authClient.findIndividualIdByUserId(
                                    validateResp.getT2().getId(), request.getOrganizationId()));
                })
                .flatMap(tuple2 -> {
                    List<ProductOfferTemplateDTO> listProductResp = tuple2.getT1();
                    if (DataUtil.isNullOrEmpty(listProductResp)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.item.info.invalid"));
                    }

                    List<ProductTemplateDTO> producTemplateList = getProducTemplateList(request, listProductResp);
                    String individualId = tuple2.getT2();
                    PlaceOrderData placeOrderData =
                            buildPlaceOrderData(request, listProductResp, producTemplateList, individualId);
                    String dataJson = DataUtil.parseObjectToString(placeOrderData);

                    // build set alias de goi sang setting lay thong tin cua dich vu
                    // PYCXXX/LuongToanTrinhScontract Order-005
                    Set<String> telecomServiceAliasSet = producTemplateList.stream()
                            .map(productTemplate -> DataUtil.safeToString(productTemplate.getTelecomServiceAlias()))
                            .collect(Collectors.toSet());

                    return orderClient
                            .placeOrder(CONNECT_EZBUY, dataJson)
                            .flatMap(respOptional -> {
                                if (respOptional.isEmpty()) {
                                    return Mono.error(new BusinessException(
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, "create.order.fail"));
                                }
                                if (!DataUtil.isTrue(respOptional.get().getSuccess())
                                        || !DataUtil.safeEqual(
                                                respOptional.get().getErrorCode(), 0)) {
                                    String message = DataUtil.safeToString(
                                            respOptional.get().getDescription());
                                    log.error("placeOrderError message: {}", message);
                                    return Mono.error(new BusinessException(ErrorCode.CREATE_ORDER_ERROR, message));
                                }

                                var userMono = getAdvice
                                        ? Mono.just(TokenUser.builder()
                                                .username("SYSTEM")
                                                .build())
                                        : SecurityUtils.getCurrentUser();

                                ProductPriceRequest priceRequest = new ProductPriceRequest();
                                List<ProductItem> productItemPrices = producTemplateList.stream()
                                        .map(template -> ProductItem.builder()
                                                .templateId(template.getTemplateId())
                                                .price(DataUtil.safeToDouble(template.getPrice())
                                                        .longValue())
                                                .quantity(template.getQuantity())
                                                .build())
                                        .collect(Collectors.toList());
                                priceRequest.setProductItems(productItemPrices);

                                var totalFeeMono = paymentClient.getTotalFee(priceRequest);
                                var telecomServiceMono = settingClient.getTelecomData(
                                        null, new ArrayList<>(telecomServiceAliasSet), null);
                                return Mono.zip(
                                        userMono,
                                        totalFeeMono,
                                        telecomServiceMono,
                                        Mono.just(respOptional.get().getDescription()));
                            })
                            .flatMap(userTotalFeeGroupData -> {
                                UUID orderId = UUID.randomUUID();

                                String username = userTotalFeeGroupData.getT1().getUsername();
                                Double price = userTotalFeeGroupData.getT2().isPresent()
                                        ? userTotalFeeGroupData.getT2().get().doubleValue()
                                        : null;

                                String orderCode = userTotalFeeGroupData.getT4();

                                String userId = userTotalFeeGroupData.getT1().getId();
                                List<TelecomDTO> telecomList = userTotalFeeGroupData.getT3();
                                // insert order data
                                return (getAdvice
                                                ? saveOrderWithoutLogin(orderId, orderCode, price, request)
                                                : saveOrder(
                                                        orderId,
                                                        orderCode,
                                                        userId,
                                                        individualId,
                                                        price,
                                                        username,
                                                        request))
                                        .flatMap(savers -> {
                                            List<OrderItem> itemList = producTemplateList.stream()
                                                    .map(item -> {
                                                        TelecomDTO telecomDto = telecomList.stream()
                                                                .filter(telecom -> DataUtil.safeEqual(
                                                                        telecom.getOriginId(),
                                                                        item.getTelecomServiceId()))
                                                                .findAny()
                                                                .orElse(new TelecomDTO());

                                                        UUID itemId = UUID.randomUUID();
                                                        return OrderItem.builder()
                                                                .id(itemId.toString())
                                                                .orderId(orderId.toString())
                                                                .name(item.getName())
                                                                .price(item.getPrice())
                                                                .originPrice(item.getOriginPrice())
                                                                .productId(item.getTemplateId())
                                                                .telecomServiceId(DataUtil.safeToString(
                                                                        item.getTelecomServiceId()))
                                                                .telecomServiceName(
                                                                        DataUtil.safeToString(telecomDto.getName()))
                                                                .telecomServiceAlias(DataUtil.safeToString(
                                                                        telecomDto.getServiceAlias())) // bo sung
                                                                // alias
                                                                // cho
                                                                // PYCXXX/LuongToanTrinhScontract
                                                                .quantity(item.getQuantity())
                                                                .currency("VND")
                                                                .status(ACTIVE)
                                                                .duration(item.getDuration())
                                                                .createBy(username)
                                                                .updateBy(username)
                                                                .state(OrderState.IN_PROGRESS.getValue())
                                                                .build();
                                                    })
                                                    .collect(Collectors.toList());

                                            DataResponse response =
                                                    new DataResponse<>(Translator.toLocaleVi(SUCCESS), orderCode);

                                            var saveAllItemMono = AppUtils.insertData(orderItemRepository
                                                    .saveAll(itemList)
                                                    .collectList());
                                            var deleteCartMono =
                                                    deleteCartItem(producTemplateList, userId, request.isFromCart());
                                            return Mono.zip(saveAllItemMono, deleteCartMono)
                                                    .flatMap(rs -> Mono.just(response))
                                                    .switchIfEmpty(Mono.just(response));
                                        });
                            });
                });
    }

    private Mono<List<PartnerLicenseKeyDTO>> setLstAliasCreateDTO(
            List<ServiceItemDTO> lstTelecomService, String alias, Long telecomServiceId) {
        List<PartnerLicenseKeyDTO> lstLicenseKeyDTOS = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(lstTelecomService)) {
            PartnerLicenseKeyDTO partnerLicenseKeyDTO = PartnerLicenseKeyDTO.builder()
                    .telecomServiceId(telecomServiceId)
                    .serviceAlias(alias)
                    .build();
            lstLicenseKeyDTOS.add(partnerLicenseKeyDTO);
            return Mono.just(lstLicenseKeyDTOS);
        }
        for (ServiceItemDTO serviceItemDTO : lstTelecomService) {
            PartnerLicenseKeyDTO partnerLicenseKeyDTO = PartnerLicenseKeyDTO.builder()
                    .telecomServiceId(telecomServiceId)
                    .serviceAlias(serviceItemDTO.getTelecomServiceAlias())
                    .build();
            lstLicenseKeyDTOS.add(partnerLicenseKeyDTO);
        }
        return Mono.just(lstLicenseKeyDTOS);
    }

    @Override
    public Mono<DataResponse> validateDataOrder(CreateOrderPaidRequest data) {
        return Mono.zip(
                        SecurityUtils.getCurrentUser(),
                        setLstAliasCreateDTO(
                                data.getLstTelecomService(), data.getTelecomServiceAlias(), data.getServiceId()))
                .flatMap(tuple -> {
                    TokenUser validateUser = tuple.getT1();
                    List<PartnerLicenseKeyDTO> lstAliasCreateDTO = tuple.getT2();
                    return authClient
                            .findIndividualIdByUserId(validateUser.getId(), data.getOrganizationId())
                            .flatMap(
                                    id -> // Set licenseKey
                                    partnerLicenseKeyService
                                            .createLicenseKey(id, data.getOrganizationId(), lstAliasCreateDTO)
                                            .flatMap(licenseKey -> {
                                                data.setLstLicenseKey(licenseKey);
                                                PlacePaidOrderData placePaidOrderData =
                                                        OrderClientUtils.mapDataOrderBccs(data, systemUser, id);
                                                String dataJson = DataUtil.parseObjectToString(placePaidOrderData);
                                                boolean isCombo = data.getIsCombo() != null ? data.getIsCombo() : false;
                                                boolean isSingle =
                                                        data.getIsSingle() != null ? data.getIsSingle() : false;
                                                String orderType =
                                                        isCombo && !isSingle ? COMBO_SME_HUB : CONNECT_EZBUY;

                                                return orderClient
                                                        .validateDataOrder(orderType, dataJson, null)
                                                        .map(rs -> new DataResponse<>(
                                                                CommonErrorCode.SUCCESS,
                                                                Translator.toLocaleVi(SUCCESS),
                                                                rs));
                                            }));
                });
    }

    @Override
    public Mono<DataResponse<Object>> updateStatePreOrder(UpdateSatePreOrderRequest request) {
        String orderCode = DataUtil.safeTrim(request.getOrderCode());
        if (DataUtil.isNullOrEmpty(orderCode)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order_code.required"));
        }
        Integer status = request.getStatus();
        if (DataUtil.isNullOrEmpty(status)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "status.required"));
        }
        if (!Constants.ORDER_STATE.COMPLETED.equals(status) && !Constants.ORDER_STATE.REJECTED.equals(status)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "status.invalid"));
        }
        // tim thong tin order theo order_code
        return orderRepository.findByOrderCode(orderCode).collectList().flatMap(orderInfos -> {
            if (!DataUtil.isNullOrEmpty(orderInfos)) {
                // neu ton tai order theo order_code thuc hien cap nhat state bang order va
                // order_item
                String id = orderInfos.get(0).getId();
                var updateOrderItemsState =
                        AppUtils.insertData(orderItemRepository.updateOrderItemByOrderCode(id, SYSTEM, status));
                var updateOrderState =
                        orderRepository.updateState(status, SYSTEM, id).then(Mono.just(new Object()));
                return Mono.zip(updateOrderState, updateOrderItemsState)
                        .flatMap(r -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null)))
                        .doOnError(err -> log.error("Update order or item error ", err));
            } else {
                // nguoc lai tim trong order_item theo order_code
                return orderItemRepository
                        .findAllByOrderCode(orderCode)
                        .collectList()
                        .flatMap(orderItemInfos -> {
                            if (DataUtil.isNullOrEmpty(orderItemInfos)) {
                                log.error("Order info not match");
                                return Mono.error(new BusinessException(
                                        CommonErrorCode.INVALID_PARAMS, "pre.order.invalid", orderCode));
                            }
                            OrderItem orderItemByOrderCode = orderItemInfos.get(0);
                            String id = orderItemByOrderCode.getOrderId();
                            // cap nhat bang order_item
                            return AppUtils.insertData(orderItemRepository.updateOrderItemById(
                                            orderItemByOrderCode.getId(), SYSTEM, status))
                                    .flatMap(tuple2 -> {
                                        // tim kiem danh sach don hang con theo order_id
                                        return orderItemRepository
                                                .findAllByOrderIds(Collections.singletonList(id))
                                                .collectList()
                                                .flatMap(orderItemsByOrderId -> {
                                                    if (DataUtil.isNullOrEmpty(orderItemsByOrderId)) {
                                                        return Mono.error(new BusinessException(
                                                                CommonErrorCode.INVALID_PARAMS,
                                                                "pre.order.invalid",
                                                                orderCode));
                                                    }
                                                    Boolean checkState = null;
                                                    for (OrderItem orderItem : orderItemsByOrderId) {
                                                        Integer state = orderItem.getState();
                                                        // neu ton tai don hang con (order_item) dang o trang thai 1 -
                                                        // dang thuc
                                                        // hien, 0 - moi thi khong cap nhat don hang cha
                                                        if (Constants.ORDER_STATE.IN_PROGRESS.equals(state)
                                                                || Constants.ORDER_STATE.NEW.equals(state)) {
                                                            checkState = null;
                                                            break;
                                                            // neu chi ton tai don hang con (order_item) o trang thai 3
                                                            // - hoan
                                                            // thanh, 4 - huy thi kiem tra
                                                        } else if (Constants.ORDER_STATE.COMPLETED.equals(
                                                                state)) { // neu co 1
                                                            // order_item
                                                            // hoan
                                                            // thanh thi
                                                            // cap nhat
                                                            // state don
                                                            // hang cha
                                                            // (order) =
                                                            // 3
                                                            checkState = true;
                                                            break;
                                                        } else if (Constants.ORDER_STATE.REJECTED.equals(
                                                                state)) { // neu tat ca
                                                            // order_item
                                                            // bi huy
                                                            // thi cap
                                                            // nhat
                                                            // state don
                                                            // hang cha
                                                            // (order) =
                                                            // 4
                                                            checkState = false;
                                                        }
                                                    }
                                                    var updateOrderState = Mono.just(new Object());
                                                    if (Boolean.TRUE.equals(checkState)) {
                                                        updateOrderState = orderRepository
                                                                .updateState(
                                                                        Constants.ORDER_STATE.COMPLETED, SYSTEM, id)
                                                                .then(Mono.just(new Object()));
                                                    } else if (Boolean.FALSE.equals(checkState)) {
                                                        updateOrderState = orderRepository
                                                                .updateState(Constants.ORDER_STATE.REJECTED, SYSTEM, id)
                                                                .then(Mono.just(new Object()));
                                                    }
                                                    return updateOrderState
                                                            .flatMap(r -> Mono.just(new DataResponse<>(
                                                                    Translator.toLocaleVi(SUCCESS), null)))
                                                            .doOnError(err ->
                                                                    log.error("Update order or item error ", err));
                                                });
                                    });
                        });
            }
        });
    }

    @Override
    public Mono<DataResponse> createOrderSelfCare(CreateOrderPaidRequest request) {
        return Mono.zip(
                        validateConnectCARequest(request),
                        SecurityUtils.getCurrentUser(),
                        setLstAliasCreateDTO(
                                request.getLstTelecomService(),
                                request.getTelecomServiceAlias(),
                                request.getServiceId()))
                .flatMap(validateUser -> authClient
                        .findIndividualIdByUserId(validateUser.getT2().getId(), request.getOrganizationId())
                        .flatMap(individualId -> partnerLicenseKeyService
                                .createLicenseKey(individualId, request.getOrganizationId(), validateUser.getT3())
                                .flatMap(licenseKey -> {
                                    request.setLstLicenseKey(licenseKey);
                                    PlacePaidOrderData placePaidOrderData =
                                            OrderClientUtils.mapDataOrderBccs(request, systemUser, individualId);
                                    UUID orderId = UUID.randomUUID();
                                    TokenUser user = validateUser.getT2();
                                    String userId = user.getId();
                                    String username = user.getUsername();
                                    CustomerDTO customerDTO = placePaidOrderData.getCustomer();
                                    return saveOrderV2(
                                                    orderId,
                                                    null,
                                                    userId,
                                                    individualId,
                                                    request.getTotalFee(),
                                                    "VND",
                                                    customerDTO.getAreaCode(),
                                                    customerDTO.getProvince(),
                                                    customerDTO.getDistrict(),
                                                    customerDTO.getPrecinct(),
                                                    OrderState.NEW.getValue(),
                                                    Constants.OrderType.PAID_ORDER,
                                                    username,
                                                    username)
                                            .flatMap(order -> {
                                                List<OrderItem> lstSaveOrderItem = new ArrayList<>();
                                                List<PaymentOrderDetailDTO> lstPaymentOrderDetail = new ArrayList<>();
                                                // luong don le
                                                if (DataUtil.isNullOrEmpty(request.getLstProductOrderItemPricing())) {
                                                    UUID itemId = UUID.randomUUID();
                                                    OrderItem orderItem = OrderItem.builder()
                                                            .id(itemId.toString())
                                                            .orderId(orderId.toString())
                                                            .price(placePaidOrderData.getTotalFee())
                                                            .productId(request.getTemplateId())
                                                            .telecomServiceId(
                                                                    DataUtil.safeToString(request.getServiceId()))
                                                            .telecomServiceName(
                                                                    DataUtil.safeToString(request.getServiceName()))
                                                            .quantity(1)
                                                            .currency("VND")
                                                            .status(ACTIVE)
                                                            .createBy(username)
                                                            .updateBy(username)
                                                            .state(OrderState.NEW.getValue()) // update: bo sung insert
                                                            // state = 0 (NEW)
                                                            .build();
                                                    lstSaveOrderItem.add(orderItem);
                                                } else {
                                                    // luong combo
                                                    for (com.ezbuy.ordermodel.dto.pricing.ProductOrderItem priceItem :
                                                            request.getLstProductOrderItemPricing()) {
                                                        UUID itemId = UUID.randomUUID();
                                                        OrderPrice totalPrice = priceItem.getTotalPrice();
                                                        ProductOfferingRef productOffering =
                                                                priceItem.getProductOffering();
                                                        Long telecomServiceId = productOffering.getTelecomServiceId();
                                                        List<ServiceItemDTO> lstTelecomService =
                                                                request.getLstTelecomService();
                                                        String telecomServiceAlias = null;
                                                        if (!DataUtil.isNullOrEmpty(lstTelecomService)) {
                                                            ServiceItemDTO serviceItemDTO = lstTelecomService.stream()
                                                                    .filter(x -> DataUtil.safeEqual(
                                                                            x.getTelecomServiceId(), telecomServiceId))
                                                                    .findFirst()
                                                                    .orElse(null);
                                                            telecomServiceAlias = serviceItemDTO != null
                                                                    ? serviceItemDTO.getTelecomServiceAlias()
                                                                    : null;
                                                        }
                                                        OrderItem orderItem = OrderItem.builder()
                                                                .id(itemId.toString())
                                                                .orderId(orderId.toString())
                                                                .price(totalPrice
                                                                        .getPrice()
                                                                        .doubleValue())
                                                                .originPrice(totalPrice
                                                                        .getOriginalPrice()
                                                                        .doubleValue())
                                                                .productId(
                                                                        DataUtil.safeToString(productOffering.getId()))
                                                                .name(DataUtil.safeToString(productOffering.getName()))
                                                                .productCode(DataUtil.safeToString(
                                                                        productOffering.getCode()))
                                                                .telecomServiceId(DataUtil.safeToString(
                                                                        productOffering.getTelecomServiceId()))
                                                                .telecomServiceName(DataUtil.safeToString(
                                                                        priceItem.getTelecomServiceName()))
                                                                .quantity(priceItem
                                                                        .getQuantity()
                                                                        .intValue())
                                                                .telecomServiceAlias(telecomServiceAlias)
                                                                .currency("VND")
                                                                .status(ACTIVE)
                                                                .createBy(username)
                                                                .updateBy(username)
                                                                .state(OrderState.NEW.getValue()) // update:
                                                                // bo
                                                                // sung
                                                                // insert
                                                                // state
                                                                // =
                                                                // 0
                                                                // (NEW)
                                                                .build();
                                                        lstSaveOrderItem.add(orderItem);
                                                        PaymentOrderDetailDTO paymentDetail =
                                                                PaymentOrderDetailDTO.builder()
                                                                        .merchantCodeCombo(telecomServiceAlias)
                                                                        .orderCodeCombo(itemId.toString())
                                                                        .amount(totalPrice
                                                                                .getPrice()
                                                                                .doubleValue())
                                                                        .build();
                                                        lstPaymentOrderDetail.add(paymentDetail);
                                                    }
                                                }

                                                OrderBccsData orderBccsData = new OrderBccsData();
                                                orderBccsData.setOrderId(orderId.toString());
                                                placePaidOrderData.setExtCode(orderId.toString());
                                                boolean isCombo =
                                                        request.getIsCombo() != null ? request.getIsCombo() : false;
                                                boolean isSingle =
                                                        request.getIsSingle() != null ? request.getIsSingle() : false;
                                                if (isCombo && !isSingle) {
                                                    orderBccsData.setOrderType(COMBO_SME_HUB);
                                                } else {
                                                    orderBccsData.setOrderType(CONNECT_EZBUY);
                                                }
                                                orderBccsData.setStatus(ACTIVE);
                                                orderBccsData.setCreateBy(username);
                                                orderBccsData.setCreateAt(LocalDateTime.now());
                                                return orderItemRepository
                                                        .saveAll(lstSaveOrderItem)
                                                        .collectList()
                                                        .flatMap(rs -> {
                                                            double D = 0;
                                                            //
                                                            if (request.getTotalFee() != D
                                                                    && (request.getRevenueForAm() == null
                                                                            || !request.getRevenueForAm())) {
                                                                // lay link redirect sang payment de thanh toan
                                                                ProductPaymentRequest createLinkRequest =
                                                                        new ProductPaymentRequest();
                                                                createLinkRequest.setOrderCode(
                                                                        DataUtil.safeToString(orderId));
                                                                createLinkRequest.setCancelUrl(cancelOrderUrl);
                                                                createLinkRequest.setReturnUrl(
                                                                        returnOrderUrl + orderId + RETURN_URL_SINGLE);
                                                                Long telecomServiceId = request.getServiceId();
                                                                if (isSingle
                                                                        && !DataUtil.isNullOrEmpty(
                                                                                request.getLstTelecomService())) {
                                                                    telecomServiceId = request.getLstTelecomService()
                                                                            .get(0)
                                                                            .getTelecomServiceId();
                                                                }
                                                                if (Constants.TelecomServiceId.CA.equals(
                                                                        telecomServiceId)) {
                                                                    createLinkRequest.setOrderType(SELFCARE_CONNECT_CA);
                                                                } else if (Constants.TelecomServiceId.EASY_BOOK.equals(
                                                                        telecomServiceId)) {
                                                                    createLinkRequest.setOrderType(
                                                                            SELFCARE_CONNECT_ESB);
                                                                } else if (Constants.TelecomServiceId.SINVOICE.equals(
                                                                        telecomServiceId)) {
                                                                    createLinkRequest.setOrderType(
                                                                            SELFCARE_CONNECT_SINVOICE);
                                                                } else if (Constants.TelecomServiceId.VCONTRACT.equals(
                                                                        telecomServiceId)) {
                                                                    createLinkRequest.setOrderType(
                                                                            SELFCARE_CONNECT_VCONTRACT);
                                                                } else if (Constants.TelecomServiceId.VBHXH.equals(
                                                                        telecomServiceId)) {
                                                                    createLinkRequest.setOrderType(
                                                                            SELFCARE_CONNECT_VBHXH);
                                                                }
                                                                if (isCombo && !isSingle) {
                                                                    createLinkRequest.setOrderType(COMBO_SME_HUB);
                                                                    createLinkRequest.setCancelUrl(cancelOrderUrl);
                                                                    String packageName = !DataUtil.isNullOrEmpty(
                                                                                    request.getComboPackageName())
                                                                            ? request.getComboPackageName()
                                                                            : RETURN_URL_COMBO;
                                                                    createLinkRequest.setReturnUrl(returnOrderUrl
                                                                            + orderId + "/" + packageName);
                                                                }
                                                                createLinkRequest.setTotalFee(
                                                                        DataUtil.safeToLong(request.getTotalFee()));
                                                                createLinkRequest.setTelecomServiceAlias(
                                                                        request.getTelecomServiceAlias());
                                                                if (!DataUtil.isNullOrEmpty(lstPaymentOrderDetail)) {
                                                                    createLinkRequest.setLstPaymentOrderDetail(
                                                                            lstPaymentOrderDetail);
                                                                }
                                                                return paymentClient
                                                                        .getLinkCheckOut(createLinkRequest)
                                                                        .flatMap(obcheckOut -> {
                                                                            if (obcheckOut.isEmpty()) {
                                                                                return Mono.error(
                                                                                        new BusinessException(
                                                                                                CommonErrorCode
                                                                                                        .INTERNAL_SERVER_ERROR,
                                                                                                "create.link.checkout.internal"));
                                                                            }
                                                                            // luu them thong tin transactionIdNo
                                                                            ProductPaymentResponse paymentResponse =
                                                                                    obcheckOut.get();
                                                                            String createLinkCheckout =
                                                                                    paymentResponse.getCheckoutLink();
                                                                            String requestBankingId =
                                                                                    paymentResponse
                                                                                            .getRequestBankingId();
                                                                            PayInfoPaidOrderDTO payInfoPaidOrderDTO =
                                                                                    placePaidOrderData.getPayInfo();
                                                                            payInfoPaidOrderDTO.setTransactionIdNo(
                                                                                    requestBankingId);
                                                                            placePaidOrderData.setPayInfo(
                                                                                    payInfoPaidOrderDTO);
                                                                            orderBccsData.setData(
                                                                                    DataUtil.parseObjectToString(
                                                                                            placePaidOrderData));
                                                                            return (orderBccsDataRepository.save(
                                                                                            orderBccsData))
                                                                                    .flatMap(
                                                                                            rsa -> Mono.just(
                                                                                                    new DataResponse<>(
                                                                                                            Translator
                                                                                                                    .toLocaleVi(
                                                                                                                            SUCCESS),
                                                                                                            createLinkCheckout)));
                                                                        });
                                                            }
                                                            orderBccsData.setData(
                                                                    DataUtil.parseObjectToString(placePaidOrderData));
                                                            return orderBccsDataRepository
                                                                    .save(orderBccsData)
                                                                    .flatMap(saveOrderBccsData -> {
                                                                        UpdateOrderStateForOrderRequest
                                                                                updateOrderStateForOrderRequest =
                                                                                        new UpdateOrderStateForOrderRequest();
                                                                        updateOrderStateForOrderRequest.setOrderCode(
                                                                                DataUtil.safeToString(orderId));
                                                                        updateOrderStateForOrderRequest
                                                                                .setPaymentStatus(PAYMENT_STATUS);
                                                                        String url = returnOrderUrl
                                                                                + orderId
                                                                                + RETURN_URL_SINGLE;
                                                                        if (isCombo && !isSingle) {
                                                                            String packageName =
                                                                                    !DataUtil.isNullOrEmpty(
                                                                                                    request
                                                                                                            .getComboPackageName())
                                                                                            ? request
                                                                                                    .getComboPackageName()
                                                                                            : RETURN_URL_COMBO;
                                                                            url = returnOrderUrl + orderId + "/"
                                                                                    + packageName;
                                                                        }
                                                                        String finalUrl = url;
                                                                        return updateStateAndPlaceOrder(
                                                                                        updateOrderStateForOrderRequest)
                                                                                .flatMap(updateOrder -> Mono.just(
                                                                                        new DataResponse<>(
                                                                                                Translator.toLocaleVi(
                                                                                                        SUCCESS),
                                                                                                finalUrl)));
                                                                    });
                                                        });
                                            });
                                })));
    }

    @Override
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getDocDataPolicy(
            CreateOrderPaidRequest createOrderPaidRequest) {
        PlacePaidOrderData placePaidOrderData =
                OrderClientUtils.mapDataOrderBccs(createOrderPaidRequest, systemUser, null);
        String data = DataUtil.parseObjectToString(placePaidOrderData);
        return orderV2Client
                .getProfileXNDLKH(data)
                .map(response -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
    }

    /**
     * Ham lay danh sach lich su don hang tu Order
     *
     * @param request
     * @return
     */
    @Override
    public Mono<DataResponse> searchOrderHistory(SearchOrderRequest request) {
        return validateRequestOrderHistory(request).flatMap(validate -> Mono.zip(
                        settingClient.getAllActiveOptionSetValueByOptionSetCode("ORDER_HISTORY_SYSTEM_TYPE"),
                        settingClient.getAllActiveOptionSetValueByOptionSetCode("ORDER_HISTORY_ORDER_TYPE"))
                .flatMap(systemOrderType -> {
                    List<String> systemType = systemOrderType.getT1().stream()
                            .map(OptionSetValue::getValue)
                            .collect(Collectors.toList());
                    List<String> orderType = systemOrderType.getT2().stream()
                            .map(OptionSetValue::getValue)
                            .collect(Collectors.toList());
                    return orderV2Client
                            .getOrderHistoryHub(request, systemType, orderType)
                            .flatMap(response -> {
                                if (response.isEmpty() || DataUtil.isNullOrEmpty(response.get())) {
                                    response = Optional.of(Collections.emptyList());
                                }
                                // set page index for get order from db
                                request.setPageIndex(request.getPageIndex() + 1);

                                List<GetOrderHistoryResponse> finalResponse = response.get().stream()
                                        .filter(item -> !item.getOrder().getIsPreOrder())
                                        .collect(Collectors.toList());
                                List<GetOrderHistoryResponse.OrderDTO> preOrderList = response.get().stream()
                                        .filter(item -> item.getOrder().getIsPreOrder())
                                        .map(GetOrderHistoryResponse::getOrder)
                                        .collect(Collectors.toList());
                                List<String> preOrderCodeList = new ArrayList<>();
                                if (!DataUtil.isNullOrEmpty(preOrderList)) {
                                    preOrderCodeList = preOrderList.stream()
                                            .map(GetOrderHistoryResponse.OrderDTO::getOrderCode)
                                            .collect(Collectors.toList());
                                }
                                return Mono.zip(
                                                settingClient.getAllTelecomService(),
                                                searchOrderV2(request, preOrderCodeList))
                                        .map(telecomOrderDb -> {
                                            // map data from ws
                                            List<OrderDetailDTO> orderDetailList = new ArrayList<>();
                                            for (GetOrderHistoryResponse orderHistory : finalResponse) {
                                                orderDetailList.add(
                                                        mappingOrderHistory(orderHistory, telecomOrderDb.getT1()));
                                            }
                                            SearchOrderHistoryResponse result = new SearchOrderHistoryResponse();

                                            // merge order data tu db va ws
                                            SearchOrderHistoryResponse orderHistoryDb =
                                                    !DataUtil.isNullOrEmpty(telecomOrderDb
                                                                    .getT2()
                                                                    .getData())
                                                            ? (SearchOrderHistoryResponse) telecomOrderDb
                                                                    .getT2()
                                                                    .getData()
                                                            : new SearchOrderHistoryResponse();
                                            List<OrderDetailDTO> orderDetailDbList = orderHistoryDb.getData();
                                            // merge danh sach don hang db va danh sach don hang ws
                                            orderDetailList.addAll(orderDetailDbList);
                                            orderDetailList.sort(Comparator.comparing(
                                                    OrderDetailDTO::getCreateAt, (s1, s2) -> DataUtil.safeToString(s2)
                                                            .compareTo(DataUtil.safeToString(s1))));
                                            orderDetailList = orderDetailList.stream()
                                                    .limit(request.getPageSize())
                                                    .collect(Collectors.toList());
                                            result.setData(orderDetailList);

                                            return new DataResponse<>(Translator.toLocaleVi(SUCCESS), result);
                                        });
                            });
                }));
    }

    /**
     * map data from ws to response
     *
     * @param orderHistory
     * @return
     */
    private OrderDetailDTO mappingOrderHistory(GetOrderHistoryResponse orderHistory, List<Telecom> telecomList) {
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        GetOrderHistoryResponse.OrderDTO order =
                orderHistory.getOrder() != null ? orderHistory.getOrder() : new GetOrderHistoryResponse.OrderDTO();
        List<GetOrderHistoryResponse.OrderItemDTO> orderItem = !DataUtil.isNullOrEmpty(orderHistory.getOrderItem())
                ? orderHistory.getOrderItem()
                : Collections.emptyList();
        // map order
        orderDetail.setOrderCode(order.getOrderCode());
        orderDetail.setCreateAt(order.getCreateDate());
        orderDetail.setState(order.getState());
        orderDetail.setIsPreOrder(order.getIsPreOrder());
        // map order item
        List<OrderItemDTO> orderItemList = new ArrayList<>();
        for (GetOrderHistoryResponse.OrderItemDTO orderItemDTO : orderItem) {
            OrderItemDTO item = new OrderItemDTO();
            item.setName(orderItemDTO.getName());
            item.setPrice(orderItemDTO.getPrice());
            item.setOriginPrice(orderItemDTO.getOriginPrice());
            item.setQuantity(orderItemDTO.getQuantity());
            item.setTelecomServiceId(orderItemDTO.getTelecomServiceId());
            item.setTelecomServiceName(getTelecomServiceNameById(orderItemDTO.getTelecomServiceId(), telecomList));
            // update set duration
            if (orderItemDTO.getDuration() != null) {
                String durationStr = orderItemDTO.getDurationAsMonth() ? DURATION_MONTH : DURATION_DAY;
                StringBuilder duration = new StringBuilder(DataUtil.safeToString(orderItemDTO.getDuration()))
                        .append(" ")
                        .append(Translator.toLocaleVi(durationStr));
                item.setDuration(duration.toString());
            } else {
                item.setDuration(null);
            }
            orderItemList.add(item);
        }
        orderDetail.setItemList(orderItemList);

        return orderDetail;
    }

    private String getTelecomServiceNameById(String id, List<Telecom> telecomList) {
        if (DataUtil.isNullOrEmpty(telecomList)) {
            return "";
        }
        return telecomList.stream()
                .filter(item -> DataUtil.safeEqual(item.getOriginId(), id))
                .findFirst()
                .orElse(new Telecom())
                .getName();
    }

    private Mono<Boolean> validateRequestOrderHistory(SearchOrderRequest request) {
        request.setPageSize(DataUtil.safeToInt(request.getPageSize(), 20));
        if (request.getPageSize() <= 0 || request.getPageSize() > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "size.invalid"));
        }
        request.setPageIndex(DataUtil.safeToInt(request.getPageIndex(), 0));
        if (request.getPageIndex() < 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "page.invalid"));
        }
        if (DataUtil.isNullOrEmpty(request.getIndividualId())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "search.order.history.individual.id.empty"));
        }
        if (!ValidateUtils.validateUUID(request.getIndividualId())) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "search.order.history.individual.id.invalid"));
        }
        return Mono.just(true);
    }

    @Override
    public Mono<DataResponse> searchOrderV2(SearchOrderRequest request, List<String> preOrderCodeList) {
        int size = DataUtil.safeToInt(request.getPageSize(), 20);
        int page = DataUtil.safeToInt(request.getPageIndex(), 1);
        Integer state = Constants.ORDER_STATE_MAP.get(DataUtil.safeTrim(request.getState()));
        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> {
            int offset = PageUtils.getOffset(page, size);
            return orderRepositoryTemplate
                    .searchOrderDetailV2(tokenUser.getId(), state, offset, size, sort, preOrderCodeList)
                    .collectList()
                    .flatMap(orderGroupData -> {
                        Map<String, OrderDetailDTO> orderMap = new LinkedHashMap<>();
                        for (OrderDetailDTO order : orderGroupData) {
                            if (orderMap.containsKey(order.getId())) {
                                orderMap.get(order.getId()).addItem(order.getItemList());
                            } else {
                                orderMap.put(order.getId(), order);
                            }
                        }
                        List<OrderDetailDTO> orderList = new ArrayList<>(orderMap.values());
                        PaginationDTO pagination = PaginationDTO.builder()
                                .pageIndex(page)
                                .pageSize(size)
                                .build();
                        SearchOrderHistoryResponse response = SearchOrderHistoryResponse.builder()
                                .data(orderList)
                                .pagination(pagination)
                                .build();
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
                    });
        });
    }

    @Override
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getFileContractToView(
            CreateOrderPaidRequest createOrderPaidRequest) {
        PlacePaidOrderData placePaidOrderData =
                OrderClientUtils.mapDataOrderBccs(createOrderPaidRequest, systemUser, null);
        boolean isCombo = createOrderPaidRequest.getIsCombo() != null ? createOrderPaidRequest.getIsCombo() : false;
        boolean isSingle = createOrderPaidRequest.getIsSingle() != null ? createOrderPaidRequest.getIsSingle() : false;
        String orderType = isCombo && !isSingle ? COMBO_SME_HUB : CONNECT_EZBUY;
        String data = DataUtil.parseObjectToString(placePaidOrderData);
        return orderV2Client
                .getFileContractToView(orderType, data)
                .map(response -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), response));
    }

    @Override
    public Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate() {
        try {
            Resource resource = new ClassPathResource(IMPORT_GROUP_MEMBER_TEMPLATE_PATH);
            InputStream inputStream = resource.getInputStream();
            byte[] fileData = inputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", IMPORT_GROUP_MEMBER_TEMPLATE_NAME);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return Mono.just(ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileData.length)
                    .body(resource));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    /**
     * Ham lay danh sach cac phan tu bi trung trong list
     *
     * @param list
     * @return
     */
    private List<String> listDuplicateItems(List<String> list) {
        List<String> duplicates = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (String i : list) {
            if (set.contains(i)) {
                duplicates.add(i);
            } else {
                set.add(i);
            }
        }
        return duplicates;
    }

    @Override
    public Mono<GroupMemberImportListDTO> validateImportGroupMember(FilePart filePart, String totalSign) {
        // validate file trong
        if (filePart == null || filePart.filename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be provided.");
        }
        // validate dinh dang file
        String filename = filePart.filename();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.file.type.invalid"));
        }
        Flux<GroupMemberImportDTO> groupMemberImportRequests = getCreateGroupMemberRequestFlux(filePart);
        return groupMemberImportRequests.collectList().flatMap(groupMemberList -> {
            // lay danh sach cac phan tu bi trung
            List<String> isdnList = listDuplicateItems(groupMemberList.stream()
                    .map(GroupMemberImportDTO::getSubIsdn)
                    .collect(Collectors.toList()));
            Flux<GroupMemberImportDTO> results =
                    Flux.fromIterable(groupMemberList).flatMap(item -> validateImport(item, isdnList));
            return results.collectList().flatMap(memberList -> {
                // check tong so luot ky cac thanh vien trong nhom vuot qua tong so luot ky cua
                // nhom
                long signSum = 0L;
                for (GroupMemberImportDTO member : memberList) {
                    signSum = signSum + DataUtil.safeToLong(member.getNumberOfSign());
                }
                ;
                if (signSum > DataUtil.safeToLong(totalSign)) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "order.import.error.sum.sign.reach.limit"));
                }
                GroupMemberImportListDTO result = new GroupMemberImportListDTO();
                // neu validate 1 ban ghi khong hop le, set invalid tat ca cac ban ghi con lai
                // de import lai
                Boolean validateResult = true;
                for (GroupMemberImportDTO member : memberList) {
                    if (!member.isResult()) {
                        validateResult = false;
                    }
                }
                ;
                if (!validateResult) {
                    memberList.forEach(member -> {
                        if (member.isResult()) {
                            member.setResult(false);
                        }
                    });
                    result.setItems(memberList);
                    result.setTotalFailedItems(DataUtil.safeToLong(memberList.size()));
                    result.setTotalSucceedItems(VALUE_0_LONG);
                    return Mono.just(result);
                } else {
                    result.setItems(memberList);
                    result.setTotalSucceedItems(DataUtil.safeToLong(memberList.size()));
                    result.setTotalFailedItems(VALUE_0_LONG);
                    return Mono.just(result);
                }
            });
        });
    }

    /**
     * validate import item
     *
     * @param request
     *            request
     * @return
     */
    private Mono<GroupMemberImportDTO> validateImport(GroupMemberImportDTO request, List<String> isdnList) {
        return validateImportGroupMember(request, isdnList)
                .map(validateOrg -> {
                    request.setErrMsg(EMPTY);
                    request.setResult(true);
                    return request;
                })
                .onErrorResume(error -> {
                    request.setErrMsg(Translator.toLocaleVi(error.getMessage()));
                    request.setResult(false);
                    return Mono.just(request);
                });
    }

    private Mono<Boolean> validateImportGroupMember(GroupMemberImportDTO groupMember, List<String> isdnList) {
        groupMember.trim();
        // check empty
        if (DataUtil.isNullOrEmpty(groupMember.getSubIsdn())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.sub.isdn.empty"));
        }
        if (DataUtil.isNullOrEmpty(groupMember.getNumberOfSign())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.number.of.sign.empty"));
        }
        if (groupMember.getSubIsdn().length() > 20) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "order.import.error.sub.isdn.length.invalid"));
        }
        // check gia tri
        if (!groupMember.getSubIsdn().matches(SUB_ISDN_REGEX)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.sub.isdn.invalid"));
        }
        if (!groupMember.getNumberOfSign().matches(NUM_REGEX)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.number.of.sign.invalid"));
        }
        if (DataUtil.safeToLong(groupMember.getNumberOfSign()) < 0L
                || DataUtil.safeToLong(groupMember.getNumberOfSign(), 9223372036854775807L) > 9223372036854775806L) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.sign.length.invalid"));
        }
        if (isdnList.contains(groupMember.getSubIsdn())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "order.import.error.sub.isdn.duplicate"));
        }
        return Mono.just(true);
    }

    @Override
    public Mono<ResponseEntity<byte[]>> downloadImportResult(List<GroupMemberImportDTO> items) {
        try {
            Resource resource = new ClassPathResource(IMPORT_GROUP_MEMBER_RESULT_TEMPLATE_PATH);
            String fileName = IMPORT_GROUP_MEMBER_RESULT_TEMPLATE_NAME;
            // Process the Excel data and modify it if needed
            try (Workbook workbook = WorkbookFactory.create(resource.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                Row row = sheet.getRow(0);
                row.getCell(5).setCellValue(Translator.toLocaleVi("order.import.column.result"));
                row.getCell(6).setCellValue(Translator.toLocaleVi("order.import.column.error.message"));
                // ... Process the data and modify the sheet if needed
                for (var i = 0; i < items.size(); i++) {
                    var item = items.get(i);
                    buildRowWithProduct(sheet, i, item);
                }
                // Create a new Excel file
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                workbook.close();
                // Return the new Excel file as a byte array
                byte[] byteArray = outputStream.toByteArray();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                return Mono.just(new ResponseEntity<>(byteArray, headers, HttpStatus.OK));
            }
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private void buildRowWithProduct(Sheet sheet, int i, GroupMemberImportDTO item) {
        var row = sheet.createRow(i + 1);
        row.createCell(0).setCellValue(i + 1);
        row.createCell(1).setCellValue(Optional.ofNullable(item.getSubIsdn()).orElse(EMPTY));
        row.createCell(2).setCellValue(Optional.ofNullable(item.getStatus()).orElse(EMPTY));
        row.createCell(3)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getAddGroupDate()))
                        .orElse(EMPTY));
        row.createCell(4)
                .setCellValue(Optional.ofNullable(DataUtil.safeToString(item.getNumberOfSign()))
                        .orElse(EMPTY));
        // Ket qua
        row.createCell(5)
                .setCellValue(Optional.ofNullable(item.isResult())
                        .map(x -> x.equals(true) ? Translator.toLocaleVi(SUCCESS) : Translator.toLocaleVi(FAIL))
                        .orElse(EMPTY));
        row.createCell(6)
                .setCellValue(Optional.ofNullable(item.isResult())
                        .map(x -> x.equals(true) ? EMPTY : item.getErrMsg())
                        .orElse(EMPTY));
    }

    private Flux<GroupMemberImportDTO> getCreateGroupMemberRequestFlux(FilePart filePart) {
        return filePart.content()
                .filter(buffer -> buffer.readableByteCount() > 0)
                .flatMap(buffer -> {
                    try (Workbook workbook = WorkbookFactory.create(buffer.asInputStream())) {
                        Sheet sheet = workbook.getSheetAt(0);
                        Iterator<Row> rowIterator = sheet.iterator();
                        // Skip the first row (header row)
                        if (rowIterator.hasNext()) {
                            Row row = rowIterator.next();

                            int i = 0;
                            DataFormatter formatter = new DataFormatter();

                            String subIsdnTemplateRow = DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));
                            String numberOfSignTemplateRow =
                                    DataUtil.safeTrim(this.getValueInCell(row, formatter, i++));

                            if (!(Translator.toLocaleVi(SUB_ISDN)
                                                    + Translator.toLocaleVi(OBLIGATORY)
                                                    + SPACE
                                                    + Translator.toLocaleVi(SUB_ISDN_HINT))
                                            .equals(DataUtil.safeTrim(subIsdnTemplateRow))
                                    || !(Translator.toLocaleVi(NUMBER_OF_SIGN) + Translator.toLocaleVi(OBLIGATORY))
                                            .equals(DataUtil.safeTrim(numberOfSignTemplateRow))) {
                                return Mono.error(new BusinessException(
                                        String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                        Translator.toLocaleVi("order.import.error.template.invalid")));
                            }
                        }
                        List<GroupMemberImportDTO> items = new ArrayList<>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            GroupMemberImportDTO createProductRequest = readRowData(row);
                            items.add(createProductRequest);
                        }
                        // Validate size
                        if (items.size() > 100) {
                            return Flux.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS,
                                    Translator.toLocaleVi("file.import.item.exceed", "100")));
                        }
                        return Flux.fromIterable(items);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(new BusinessException(
                                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                Translator.toLocaleVi("order.import.error.file.invalid")));
                    }
                });
    }

    private GroupMemberImportDTO readRowData(Row row) {
        // unit
        int i = 0;
        DataFormatter formatter = new DataFormatter();
        GroupMemberImportDTO productImportDTO = new GroupMemberImportDTO();
        productImportDTO.setSubIsdn(this.getValueInCell(row, formatter, i++));
        productImportDTO.setNumberOfSign(this.getValueInCell(row, formatter, i));
        return productImportDTO;
    }

    private String getValueInCell(Row row, DataFormatter formatter, int i) {
        return Optional.ofNullable(row.getCell(i))
                .map(value -> formatter.formatCellValue(value).trim())
                .orElse(EMPTY);
    }

    @Override
    public Mono<DataResponse<GetOrderReportResponse>> getOrderReport(OrderReportRequest request) {

        if (DataUtil.isNullOrEmpty(request.getDateReport())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "dateReport.not.empty"));
        }
        return Mono.zip(
                        orderRepository.getAllOrderReport(request.getDateReport()),
                        orderRepository.getOrderReport(request.getDateReport()))
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "orderReport.not.found")))
                .flatMap(tuple2 -> {
                    // case khong co ban ghi nao
                    if (DataUtil.isNullOrEmpty(tuple2.getT2().getSuccessOrderCount())) {
                        tuple2.getT2().setSuccessOrderCount(0);
                        tuple2.getT2().setFailOrderCount(0);
                        tuple2.getT2().setFeeCount(0L);
                    }
                    tuple2.getT2().setOrderCount(tuple2.getT1());
                    return Mono.just(DataResponse.success(tuple2.getT2()));
                });
    }

    @Override
    public Mono<DataResponse> createOrderCts(CreateOrderPaidRequest request) {
        return Mono.zip(
                        validateConnectCARequest(request), // validate input
                        SecurityUtils.getCurrentUser())
                .flatMap(validateUser -> authClient
                        .findIndividualIdByUserId(validateUser.getT2().getId(), request.getOrganizationId())
                        .flatMap(individualId -> {
                            UploadFileBase64Request uploadFileBase64Request = new UploadFileBase64Request();
                            uploadFileBase64Request.setFolderName("EZBUY");
                            uploadFileBase64Request.setLstFile(request.getCustomerFile());
                            // upload file ftp
                            return apiGatewayClient
                                    .uploadFileBase64(uploadFileBase64Request)
                                    .flatMap(uploadFtp -> {
                                        request.setCustomerFile(uploadFtp);
                                        PlacePaidOrderData placePaidOrderData =
                                                OrderClientUtils.mapDataOrderBccs(request, systemUser, individualId);
                                        String dataPlacePaidOrder = DataUtil.parseObjectToString(placePaidOrderData);
                                        // lay ban ghi tenant
                                        var getTenantID = authClient.getByTypeAndTenantIdAndTrustStatus(
                                                "ORGANIZATION", request.getOrganizationId(), "2");
                                        // call order tao don cts
                                        var createOrder =
                                                orderV2Client.createOrder(CONNECT_EZBUY, dataPlacePaidOrder);
                                        return Mono.zip(getTenantID, createOrder)
                                                .flatMap(response -> {
                                                    if (DataUtil.safeEqual(
                                                            response.getT2()
                                                                    .get()
                                                                    .getErrorCode(),
                                                            "0")) {
                                                        UUID orderId = UUID.randomUUID();
                                                        TokenUser user = validateUser.getT2();
                                                        String userId = user.getId();
                                                        String username = user.getUsername();
                                                        CustomerDTO customerDTO = placePaidOrderData.getCustomer();
                                                        // save order
                                                        return saveOrderV2(
                                                                        orderId,
                                                                        response.getT2()
                                                                                .get()
                                                                                .getDescription(),
                                                                        userId,
                                                                        individualId,
                                                                        request.getTotalFee(),
                                                                        "VND",
                                                                        customerDTO.getAreaCode(),
                                                                        customerDTO.getProvince(),
                                                                        customerDTO.getDistrict(),
                                                                        customerDTO.getPrecinct(),
                                                                        OrderState.NEW.getValue(),
                                                                        Constants.OrderType.PAID_ORDER,
                                                                        username,
                                                                        username)
                                                                .flatMap(order -> {
                                                                    List<OrderItem> lstSaveOrderItem =
                                                                            new ArrayList<>();
                                                                    // luu thong tin order item
                                                                    if (DataUtil.isNullOrEmpty(
                                                                            request.getLstProductOrderItemPricing())) {
                                                                        UUID itemId = UUID.randomUUID();
                                                                        OrderItem orderItem = OrderItem.builder()
                                                                                .id(itemId.toString())
                                                                                .orderId(orderId.toString())
                                                                                .price(placePaidOrderData.getTotalFee())
                                                                                .productId(request.getTemplateId())
                                                                                .telecomServiceId(DataUtil.safeToString(
                                                                                        request.getServiceId()))
                                                                                .telecomServiceName(
                                                                                        DataUtil.safeToString(
                                                                                                request
                                                                                                        .getServiceName()))
                                                                                .quantity(1)
                                                                                .currency("VND")
                                                                                .status(ACTIVE)
                                                                                .createBy(username)
                                                                                .updateBy(username)
                                                                                .state(
                                                                                        OrderState.NEW
                                                                                                .getValue()) // update:
                                                                                // bo sung
                                                                                // insert state = 0
                                                                                // (NEW)
                                                                                .build();
                                                                        lstSaveOrderItem.add(orderItem);
                                                                    } else {
                                                                        for (com.ezbuy.ordermodel.dto.pricing
                                                                                        .ProductOrderItem
                                                                                priceItem :
                                                                                        request
                                                                                                .getLstProductOrderItemPricing()) {
                                                                            UUID itemId = UUID.randomUUID();
                                                                            OrderPrice totalPrice =
                                                                                    priceItem.getTotalPrice();
                                                                            ProductOfferingRef productOffering =
                                                                                    priceItem.getProductOffering();
                                                                            Long telecomServiceId =
                                                                                    productOffering
                                                                                            .getTelecomServiceId();
                                                                            List<ServiceItemDTO> lstTelecomService =
                                                                                    request.getLstTelecomService();
                                                                            String telecomServiceAlias = null;
                                                                            if (!DataUtil.isNullOrEmpty(
                                                                                    lstTelecomService)) {
                                                                                ServiceItemDTO serviceItemDTO =
                                                                                        lstTelecomService.stream()
                                                                                                .filter(
                                                                                                        x ->
                                                                                                                DataUtil
                                                                                                                        .safeEqual(
                                                                                                                                x
                                                                                                                                        .getTelecomServiceId(),
                                                                                                                                telecomServiceId))
                                                                                                .findFirst()
                                                                                                .orElse(null);
                                                                                telecomServiceAlias =
                                                                                        serviceItemDTO != null
                                                                                                ? serviceItemDTO
                                                                                                        .getTelecomServiceAlias()
                                                                                                : null;
                                                                            }
                                                                            OrderItem orderItem = OrderItem.builder()
                                                                                    .id(itemId.toString())
                                                                                    .orderId(orderId.toString())
                                                                                    .price(totalPrice
                                                                                            .getPrice()
                                                                                            .doubleValue())
                                                                                    .originPrice(totalPrice
                                                                                            .getOriginalPrice()
                                                                                            .doubleValue())
                                                                                    .productId(DataUtil.safeToString(
                                                                                            productOffering.getId()))
                                                                                    .name(DataUtil.safeToString(
                                                                                            productOffering.getName()))
                                                                                    .productCode(DataUtil.safeToString(
                                                                                            productOffering.getCode()))
                                                                                    .telecomServiceId(
                                                                                            DataUtil.safeToString(
                                                                                                    productOffering
                                                                                                            .getTelecomServiceId()))
                                                                                    .telecomServiceName(
                                                                                            DataUtil.safeToString(
                                                                                                    priceItem
                                                                                                            .getTelecomServiceName()))
                                                                                    .quantity(priceItem
                                                                                            .getQuantity()
                                                                                            .intValue())
                                                                                    .telecomServiceAlias(
                                                                                            telecomServiceAlias)
                                                                                    .currency("VND")
                                                                                    .status(ACTIVE)
                                                                                    .createBy(username)
                                                                                    .updateBy(username)
                                                                                    .state(
                                                                                            OrderState.NEW
                                                                                                    .getValue()) // update:
                                                                                    // bo
                                                                                    // sung
                                                                                    // insert
                                                                                    // state
                                                                                    // =
                                                                                    // 0
                                                                                    // (NEW)
                                                                                    .build();
                                                                            lstSaveOrderItem.add(orderItem);
                                                                        }
                                                                    }
                                                                    // luu order bccs data
                                                                    OrderBccsData orderBccsData = new OrderBccsData();
                                                                    orderBccsData.setOrderId(orderId.toString());
                                                                    placePaidOrderData.setExtCode(orderId.toString());
                                                                    orderBccsData.setData(DataUtil.parseObjectToString(
                                                                            placePaidOrderData));
                                                                    orderBccsData.setOrderType(CONNECT_EZBUY);
                                                                    orderBccsData.setStatus(ACTIVE);
                                                                    orderBccsData.setCreateBy(username);
                                                                    orderBccsData.setCreateAt(LocalDateTime.now());
                                                                    var saveBccsOrder = AppUtils.insertData(
                                                                            orderBccsDataRepository.save(
                                                                                    orderBccsData));
                                                                    // save to table order_ext
                                                                    OrderExt orderExt = new OrderExt();
                                                                    orderExt.setId(UUID.randomUUID()
                                                                            .toString());
                                                                    orderExt.setOrderId(orderId.toString());
                                                                    orderExt.setCode("ORDER_TRUST_IDENTITY");
                                                                    orderExt.setValue(response.getT1()
                                                                            .get(0)
                                                                            .getTenantId());
                                                                    orderExt.setStatus(ACTIVE);
                                                                    orderExt.setCreateBy(username);
                                                                    orderExt.setCreateAt(LocalDateTime.now());
                                                                    orderExt.setUpdateBy(username);
                                                                    orderExt.setNew(true);
                                                                    var saveOrderExt = AppUtils.insertData(
                                                                            orderExtRepository.save(orderExt));
                                                                    // update tenant_identify
                                                                    UpdateTenantTrustStatusRequest
                                                                            updateTenantTrustStatus =
                                                                                    new UpdateTenantTrustStatusRequest();
                                                                    updateTenantTrustStatus.setTenantId(response.getT1()
                                                                            .get(0)
                                                                            .getTenantId());
                                                                    updateTenantTrustStatus.setTrustStatus(
                                                                            WAIT_APPROVE);
                                                                    var updateTrustStatusTenant =
                                                                            authClient.updateTrustStatusByTenantId(
                                                                                    updateTenantTrustStatus);
                                                                    return Mono.zip(
                                                                                    saveBccsOrder,
                                                                                    orderItemRepository
                                                                                            .saveAll(lstSaveOrderItem)
                                                                                            .collectList(),
                                                                                    saveOrderExt,
                                                                                    updateTrustStatusTenant)
                                                                            .map(responseData -> new DataResponse<>(
                                                                                    Translator.toLocaleVi(SUCCESS),
                                                                                    null));
                                                                });
                                                    } else {
                                                        UpdateTenantTrustStatusRequest updateTenantTrustStatus =
                                                                new UpdateTenantTrustStatusRequest();
                                                        updateTenantTrustStatus.setTenantId(response.getT1()
                                                                .get(0)
                                                                .getTenantId());
                                                        updateTenantTrustStatus.setTrustStatus(WAIT_CREATE_CTS);
                                                        // update trang thai xac thuc doanh nghiep
                                                        return authClient
                                                                .updateTrustStatusByTenantId(updateTenantTrustStatus)
                                                                .map(responseData -> new DataResponse<>(
                                                                        "fail",
                                                                        response.getT2()
                                                                                .get()
                                                                                .getDescription(),
                                                                        null));
                                                    }
                                                })
                                                .onErrorResume(throwable -> {
                                                    log.error("createOrderCts error: " + throwable.getMessage());
                                                    return Mono.error(new BusinessException(
                                                            CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                            Translator.toLocaleVi("common.error")));
                                                });
                                    });
                        }));
    }

    @Override
    public Mono<DataResponse> getOrderTransactionFromTo(GetOrderTransactionToRequest request) {
        LocalDateTime to = null;
        LocalDateTime from = null;
        if (!DataUtil.isNullOrEmpty(request.getFrom())) {
            from = DataUtil.convertDateStrToLocalDateTime(request.getFrom(), FORMAT_DATE_DMY_HYPHEN);
        }
        if (!DataUtil.isNullOrEmpty(request.getTo())) {
            to = DataUtil.convertDateStrToLocalDateTime(request.getTo(), FORMAT_DATE_DMY_HYPHEN);
        }
        if (DataUtil.isNullOrEmpty(request.getUsername())) {
            // lay 30 ngay gan nhat
            if (to == null) {
                to = LocalDateTime.now();
            }
            if (DataUtil.isNullOrEmpty(request.getFrom())) {
                from = to.minusDays(30);
            }
        }

        int offset = (request.getPageIndex() - 1) * request.getPageSize();
        String sort = request.getSort();
        String sortQuery =
                (sort.contains("-")) ? " order by " + sort.substring(1) + " desc " : " order by " + sort.substring(1);
        if (!DataUtil.isNullOrEmpty(request.getUsername())) {
            LocalDateTime finalFrom = from;
            LocalDateTime finalTo = to;
            return authClient
                    .getEmailsByUsername(request.getUsername())
                    .flatMap(email -> {
                        // neu email tim theo username khong khop -> tra ve ds trong
                        if (DataUtil.isNullOrEmpty(email)) {
                            return Mono.just(this.mapPaginationResults(
                                    new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L));
                        } else {
                            // tim kiem theo email lay duoc
                            return Mono.zip(
                                            orderTransactionRepositoryTemplate
                                                    .searchOrderTransmission(
                                                            email,
                                                            request.getOrderCode(),
                                                            request.getIdNo(),
                                                            request.getPhone(),
                                                            finalFrom,
                                                            finalTo,
                                                            offset,
                                                            request.getLimit(),
                                                            sortQuery)
                                                    .collectList(),
                                            orderTransactionRepositoryTemplate.countOrderTransmission(
                                                    email,
                                                    request.getOrderCode(),
                                                    request.getIdNo(),
                                                    request.getPhone(),
                                                    finalFrom,
                                                    finalTo))
                                    .map(tuple2 -> this.mapPaginationResults(
                                            tuple2.getT1(), request.getPageIndex(), request.getLimit(), tuple2.getT2()))
                                    .switchIfEmpty(Mono.just(this.mapPaginationResults(
                                            new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
                        }
                    })
                    .switchIfEmpty(Mono.just(this.mapPaginationResults(
                            new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
        } else {
            // tim kiem theo input dau vao
            return Mono.zip(
                            orderTransactionRepositoryTemplate
                                    .searchOrderTransmission(
                                            null,
                                            request.getOrderCode(),
                                            request.getIdNo(),
                                            request.getPhone(),
                                            from,
                                            to,
                                            offset,
                                            request.getLimit(),
                                            sortQuery)
                                    .collectList(),
                            orderTransactionRepositoryTemplate.countOrderTransmission(
                                    null, request.getOrderCode(), request.getIdNo(), request.getPhone(), from, to))
                    .map(tuple2 -> this.mapPaginationResults(
                            tuple2.getT1(), request.getPageIndex(), request.getLimit(), tuple2.getT2()))
                    .switchIfEmpty(Mono.just(this.mapPaginationResults(
                            new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
        }
    }

    private DataResponse mapPaginationResults(
            List<OrderTransmissionDTO> orderTransmissionDTOS, Integer pageIndex, Integer limit, Long totalRecords) {
        var pagination = PaginationDTO.builder()
                .pageSize(limit)
                .pageIndex(pageIndex)
                .totalRecords(totalRecords)
                .build();
        var pageResponse = OrderTransmissionPageDTO.builder()
                .pagination(pagination)
                .results(orderTransmissionDTOS)
                .build();
        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null, pageResponse);
    }
}
