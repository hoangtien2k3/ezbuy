package com.ezbuy.productservice.client.impl;

import com.ezbuy.sme.framework.client.BaseSoapClient;
import com.ezbuy.sme.framework.constants.CommonErrorCode;
import com.ezbuy.sme.framework.constants.Constants;
import com.ezbuy.sme.framework.exception.BusinessException;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.DataWsUtil;
import com.ezbuy.sme.productmodel.dto.ws.CustomerSubscribeInfoCMResponse;
import com.ezbuy.sme.productmodel.dto.ws.SubscriberCMResponse;
import com.ezbuy.productservice.client.CmClient;
import com.ezbuy.productservice.client.properties.CmProperties;
import com.ezbuy.productservice.client.utils.CMClientUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@DependsOn("webClientFactory")
@Data
public class CmClientImpl implements CmClient {

    private final BaseSoapClient baseSoapClient;

    @Qualifier(value = "cmClient")
    private final WebClient cmClient;
    private final CmProperties cmProperties;

    @Override
    public Mono<List<SubscriberCMResponse>> getListSubscriberByIdNo(String idNo) {
        log.info("before payload getListSubscriberByIdNo");

        String payload = CMClientUtils.getListSubscriberByIdNo(idNo, cmProperties.getUsername(), cmProperties.getPassword());

        return baseSoapClient.call(cmClient, null, payload, CustomerSubscribeInfoCMResponse.class)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return new ArrayList<>();
                    }
                    CustomerSubscribeInfoCMResponse cmObject = (CustomerSubscribeInfoCMResponse) ((Optional) response).get();
                    if (DataUtil.isNullOrEmpty(cmObject.getListSubscriber())) {
                        return new ArrayList<>();
                    }
                    return cmObject.getListSubscriber();
                })
                .doOnError(err -> log.error("Exception when call soap getListSubscriberByIdno: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<CustomerSubscribeInfoCMResponse> getCustomerSubscriberSmeInfo(Long telecomServiceId, String idNo, String isdn) {
        String payload = CMClientUtils.getCustomerSubscriberSmeInfo(telecomServiceId, idNo, isdn, cmProperties.getUsername(), cmProperties.getPassword());

        log.info("before payload getCustomerSubscriberSmeInfo {}", payload);
        return baseSoapClient.callRaw(cmClient, null, payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return null;
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            "lstCustomerDTO>",
                            "</lstCustomerDTO>");

                    String xmlData = "<return>" + realData + "</return>";
                    CustomerSubscribeInfoCMResponse customerSubscribeInfoCMResponse =
                            DataWsUtil.xmlToObj(xmlData, CustomerSubscribeInfoCMResponse.class);
                    return customerSubscribeInfoCMResponse;
                })
                .doOnError(err -> {
                    log.info("Exception when call soap getCustomerSubscriberSmeInfo: ", err);
                }).onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "cm.service.error")));
    }
}
