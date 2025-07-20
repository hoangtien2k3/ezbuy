package com.ezbuy.orderservice.client.impl;

import static com.reactify.constants.Constants.XmlConst.TAG_CLOSE_RETURN;
import static com.reactify.constants.Constants.XmlConst.TAG_OPEN_RETURN;

import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import com.ezbuy.ordermodel.dto.request.SearchOrderRequest;
import com.ezbuy.ordermodel.dto.response.CreateProfileKHDNResponse;
import com.ezbuy.ordermodel.dto.response.GetOrderHistoryResponse;
import com.ezbuy.ordermodel.dto.ws.CreateOrderResponse;
import com.ezbuy.orderservice.client.OrderV2Client;
import com.ezbuy.orderservice.client.properties.OrderV2Properties;
import com.ezbuy.orderservice.client.utils.OrderClientUtils;
import com.ezbuy.orderservice.client.utils.OrderV2ClientUtils;
import com.reactify.client.BaseSoapClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import com.reactify.util.DataWsUtil;
import com.reactify.util.Translator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class OrderV2ClientImpl implements OrderV2Client {

    private final BaseSoapClient soapClient;

    private final WebClient orderV2Client;

    private final OrderV2Properties orderV2Properties;

    public OrderV2ClientImpl(
            BaseSoapClient soapClient,
            @Qualifier("orderV2Client") WebClient orderV2Client,
            OrderV2Properties orderV2Properties) {
        this.soapClient = soapClient;
        this.orderV2Client = orderV2Client;
        this.orderV2Properties = orderV2Properties;
    }

    @Override
    public Mono<ProfileForBusinessCustDTO> getProfileKHDN(
            String data, Long telecomServiceId, HashMap<String, Object> metaData) {
        String payload = OrderClientUtils.getProfileKHDNRequest(
                data, orderV2Properties.getUsername(), orderV2Properties.getPassword());
        return soapClient
                .callRaw(orderV2Client, null, payload)
                .flatMap(response -> {
                    if (response == Optional.empty()) {
                        return new CreateProfileKHDNResponse();
                    }
                    CreateProfileKHDNResponse createProfileKHDNResponse =
                            (CreateProfileKHDNResponse) ((Optional) response).get();
                    if (createProfileKHDNResponse == null
                            || DataUtil.isNullOrEmpty(createProfileKHDNResponse.getLstProfileForBusinessCust())) {
                        return new ProfileForBusinessCustDTO();
                    }
                    ProfileForBusinessCustDTO profile = createProfileKHDNResponse
                            .getLstProfileForBusinessCust()
                            .get(0);
                    return Mono.just(profile);
                })
                .onErrorResume(throwable -> Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, ((BusinessException) throwable).getMessage())));
    }

    @Override
    public Mono<Optional<CreateOrderResponse>> createOrder(String orderType, String data) {
        String payload = OrderClientUtils.createOrder(
                orderType, data, orderV2Properties.getUsername(), orderV2Properties.getPassword());
        return soapClient
                .callRaw(orderV2Client, null, payload)
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<ProfileForBusinessCustDTO> getProfileXNDLKH(String data) {
        String payload = OrderClientUtils.getProfileXNDLKHRequest(
                data, orderV2Properties.getUsername(), orderV2Properties.getPassword());
        return handleGetProfileOrder(payload);
    }

    public Mono<ProfileForBusinessCustDTO> handleGetProfileOrder(String payload) {
        return soapClient
                .call(orderV2Client, null, payload, CreateProfileKHDNResponse.class)
                .flatMap(response -> {
                    if (response == Optional.empty()) {
                        return new CreateProfileKHDNResponse();
                    }
                    CreateProfileKHDNResponse createProfileKHDNResponse =
                            (CreateProfileKHDNResponse) ((Optional) response).get();
                    if (createProfileKHDNResponse == null
                            || DataUtil.isNullOrEmpty(createProfileKHDNResponse.getLstProfileForBusinessCust())) {
                        return new ProfileForBusinessCustDTO();
                    }
                    ProfileForBusinessCustDTO profile = createProfileKHDNResponse
                            .getLstProfileForBusinessCust()
                            .get(0);
                    return Mono.just(profile);
                })
                .onErrorResume(throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.soap.order.error")));
                });
    }

    @Override
    public Mono<Optional<List<GetOrderHistoryResponse>>> getOrderHistoryHub(
            SearchOrderRequest request, List<String> systemTypeList, List<String> orderTypeList) {
        String payload = OrderV2ClientUtils.getOrderHistoryHubPayload(
                null,
                request.getIndividualId(),
                systemTypeList,
                orderTypeList,
                request.getPageSize(),
                request.getPageIndex(),
                request.getState(),
                orderV2Properties.getUsername(),
                orderV2Properties.getPassword());
        return soapClient
                .callRaw(orderV2Client, null, payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Optional.empty();
                    }
                    List<String> realData =
                            getListDataByTag(DataUtil.safeToString(response), TAG_OPEN_RETURN, TAG_CLOSE_RETURN);
                    if (!DataUtil.isNullOrEmpty(realData)) {
                        List<GetOrderHistoryResponse> result = new ArrayList<>();
                        realData.forEach(dataItem -> result.add(DataWsUtil.xmlToObj(
                                TAG_OPEN_RETURN + dataItem + TAG_CLOSE_RETURN, GetOrderHistoryResponse.class)));
                        return Optional.of(result);
                    } else {
                        return Optional.empty();
                    }
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.just(Optional.empty());
                });
    }

    public List<String> getListDataByTag(String realData, String fromKey, String toKey) {
        List<String> list = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(realData)) {
            return list;
        }
        int index;
        while (realData.contains(toKey)) {
            String data = DataWsUtil.getDataByTag(realData, fromKey, toKey);
            list.add(data);
            index = realData.indexOf(toKey) + toKey.length();
            realData = realData.substring(index);
        }
        return list;
    }

    @Override
    public Mono<ProfileForBusinessCustDTO> getFileContractToView(String orderType, String data) {
        String payload = OrderClientUtils.getFileContractToView(
                data, orderType, orderV2Properties.getUsername(), orderV2Properties.getPassword());
        return soapClient
                .call(orderV2Client, null, payload, CreateProfileKHDNResponse.class)
                .flatMap(response -> {
                    if (response == Optional.empty()) {
                        return new CreateProfileKHDNResponse();
                    }
                    CreateProfileKHDNResponse createProfileKHDNResponse =
                            (CreateProfileKHDNResponse) ((Optional) response).get();
                    if (createProfileKHDNResponse == null
                            || DataUtil.isNullOrEmpty(createProfileKHDNResponse.getLstProfileForBusinessCust())) {
                        return new ProfileForBusinessCustDTO();
                    }
                    ProfileForBusinessCustDTO profile = createProfileKHDNResponse
                            .getLstProfileForBusinessCust()
                            .get(0);
                    return Mono.just(profile);
                })
                .onErrorResume(throwable -> Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, ((BusinessException) throwable).getMessage())));
    }
}
