package com.ezbuy.orderservice.client.impl;

import com.ezbuy.ordermodel.dto.ExtKeyDTO;
import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.PlaceOrderResponse;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import com.ezbuy.ordermodel.dto.ws.SearchOrderStateResponse;
import com.ezbuy.ordermodel.dto.ws.ValidateDataOrderResponse;
import com.ezbuy.orderservice.client.OrderClient;
import com.ezbuy.orderservice.client.properties.OrderProperties;
import com.ezbuy.orderservice.client.utils.OrderClientUtils;
import com.reactify.client.BaseSoapClient;
import com.reactify.constants.Constants;
import com.reactify.util.DataUtil;
import com.reactify.util.DataWsUtil;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {

    private final BaseSoapClient soapClient;

    private final WebClient orderClient;

    private final OrderProperties orderProperties;

    public OrderClientImpl(
            BaseSoapClient soapClient,
            @Qualifier("orderClient") WebClient orderClient,
            OrderProperties orderProperties) {
        this.soapClient = soapClient;
        this.orderClient = orderClient;
        this.orderProperties = orderProperties;
    }

    @Override
    public Mono<Optional<PlaceOrderResponse>> placeOrder(String type, String dataJson) {
        String payload = OrderClientUtils.getPlaceOrderPayload(
                type, dataJson, orderProperties.getUsername(), orderProperties.getPassword());
        return soapClient
                .call(orderClient, null, payload, PlaceOrderResponse.class)
                .doOnSuccess(rs -> {
                    log.info("ORDER SOAP {}", rs);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<Optional<SearchOrderStateResponse>> searchOrderState(List<String> orderCodeList) {
        String payload = OrderClientUtils.getSearchOrderStatePayload(
                orderCodeList, orderProperties.getUsername(), orderProperties.getPassword());
        return soapClient
                .callRaw(orderClient, null, payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return "";
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            "<ns2:searchOrderStateByBpIdResponse xmlns:ns2=\"http://service.order.bccs.ezbuy.com/\">",
                            "</ns2:searchOrderStateByBpIdResponse>");

                    String xmlData = "<data>" + realData + "</data>";
                    SearchOrderStateResponse searchOrderStateResponse =
                            DataWsUtil.xmlToObj(xmlData, SearchOrderStateResponse.class);
                    return Optional.ofNullable(searchOrderStateResponse);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<Optional<PricingProductWSResponse>> getPricingProduct(PricingProductRequest request) {
        String payload = OrderClientUtils.getPricingProduct(
                request, orderProperties.getUsername(), orderProperties.getPassword());
        return soapClient
                .callRaw(orderClient, null, payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return "";
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            "<ns2:pricingProductsWithViewModeExtResponse xmlns:ns2=\"http://service.order.bccs.ezbuy.com/\">",
                            "</ns2:pricingProductsWithViewModeExtResponse>");

                    PricingProductWSResponse pricingProductWSResponse =
                            DataWsUtil.xmlToObj(realData, PricingProductWSResponse.class);
                    return Optional.ofNullable(pricingProductWSResponse);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<Optional<ValidateDataOrderResponse>> validateDataOrder(
            String orderType, String dataJson, List<ExtKeyDTO> lstExtKey) {
        String payload = OrderClientUtils.getValidateDataOrderRequest(
                orderType,
                StringEscapeUtils.escapeXml(dataJson),
                orderProperties.getUsername(),
                orderProperties.getPassword());
        return soapClient
                .call(orderClient, null, payload, ValidateDataOrderResponse.class)
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }
}
