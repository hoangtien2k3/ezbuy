package com.ezbuy.orderservice.client.impl;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import com.ezbuy.orderservice.client.PricingClient;
import com.ezbuy.orderservice.client.properties.PricingProperties;
import com.ezbuy.orderservice.client.utils.PricingClientUtils;
import com.reactify.client.BaseSoapClient;
import com.reactify.constants.Constants;
import com.reactify.util.DataUtil;
import com.reactify.util.DataWsUtil;
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
public class PricingClientImpl implements PricingClient {

    private final BaseSoapClient soapClient;

    private final WebClient pricingClient;

    private final PricingProperties pricingProperties;

    public PricingClientImpl(
            BaseSoapClient soapClient,
            @Qualifier("pricingClient") WebClient pricingClient,
            PricingProperties pricingProperties) {
        this.soapClient = soapClient;
        this.pricingClient = pricingClient;
        this.pricingProperties = pricingProperties;
    }

    @Override
    public Mono<Optional<PricingProductWSResponse>> getPricingProduct(PricingProductRequest request) {
        String payload = PricingClientUtils.getPricingProduct(
                request, pricingProperties.getUsername(), pricingProperties.getPassword());
        return soapClient
                .callRaw(pricingClient, null, payload)
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
}
