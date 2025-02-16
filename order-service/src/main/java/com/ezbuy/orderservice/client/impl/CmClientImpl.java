package com.ezbuy.orderservice.client.impl;

import com.ezbuy.ordermodel.dto.ws.GetCustomerSubscriberSmeInfoResponse;
import com.ezbuy.orderservice.client.CmClient;
import com.ezbuy.orderservice.client.properties.CmProperties;
import com.ezbuy.orderservice.client.utils.CmClientUtils;
import com.reactify.client.BaseSoapClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
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
public class CmClientImpl implements CmClient {

    private final BaseSoapClient soapClient;

    private final WebClient cmClient;

    private final CmProperties cmProperties;

    public CmClientImpl(
            BaseSoapClient soapClient, @Qualifier("cmClient") WebClient cmClient, CmProperties cmProperties) {
        this.soapClient = soapClient;
        this.cmClient = cmClient;
        this.cmProperties = cmProperties;
    }

    // lay thong tin cua customer
    @Override
    public Mono<Optional<GetCustomerSubscriberSmeInfoResponse>> getCustomerSubscriberSmeInfo(
            String idNo, String isdn, String telecomServiceId) {
        String payload = CmClientUtils.getGetCustomerSubscriberSmeInfoTemplate(
                idNo, isdn, telecomServiceId, cmProperties.getUsername(), cmProperties.getPassword());
        log.info("Start call CM {}", payload);
        return soapClient
                .callRaw(cmClient, null, payload)
                .map(response -> {
                    log.info("Response CM {}", response);
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Optional.empty();
                    }

                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            "<return>",
                            "</return>");
                    String xmlData = "<return>" + realData + "</return>";
                    GetCustomerSubscriberSmeInfoResponse getCustomerSubscriberSmeInfoResponse =
                            DataWsUtil.xmlToObj(xmlData, GetCustomerSubscriberSmeInfoResponse.class);
                    log.info("getCustomerSubscriberSmeInfoResponse {}", getCustomerSubscriberSmeInfoResponse);
                    return Optional.ofNullable(getCustomerSubscriberSmeInfoResponse);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "cm_error")))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.just(Optional.empty());
                });
    }
}
