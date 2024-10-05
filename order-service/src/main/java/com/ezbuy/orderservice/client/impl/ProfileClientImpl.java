package com.ezbuy.orderservice.client.impl;

import com.ezbuy.orderservice.client.ProfileClient;
import com.ezbuy.orderservice.client.properties.ProfileProperties;
import com.ezbuy.orderservice.client.utils.ProfileClientUtils;
import com.ezbuy.sme.framework.client.BaseSoapClient;
import com.ezbuy.sme.framework.constants.Constants;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.DataWsUtil;
import com.ezbuy.sme.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.sme.ordermodel.dto.response.GetListProductOfferingRecordResponse;
import com.ezbuy.sme.ordermodel.dto.response.GetListServiceRecordResponse;
import com.ezbuy.sme.ordermodel.dto.ws.GetCustomerSubscriberSmeInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class ProfileClientImpl implements ProfileClient {
    private final BaseSoapClient soapClient;

    private final WebClient profileClient;

    private final ProfileProperties profileProperties;

    public ProfileClientImpl(BaseSoapClient soapClient, @Qualifier("profileClient") WebClient profileClient, ProfileProperties profileProperties) {
        this.soapClient = soapClient;
        this.profileClient = profileClient;
        this.profileProperties = profileProperties;
    }

    @Override
    public Mono<Optional<GetListProductOfferingRecordResponse>> getListProductOfferingRecordResponse(GetListProductOfferingRecordRequest request) {
        String payload = ProfileClientUtils.getListProductOfferingRecord(request,profileProperties.getUsername(), profileProperties.getPassword());
        return soapClient.callRaw(profileClient, null,payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Mono.empty();
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                            .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER), "<return>", "</return>");
                    String xmlData = "<return>" + realData + "</return>";
                    GetListProductOfferingRecordResponse GetListProductOfferingRecordResponse =
                            DataWsUtil.xmlToObj(xmlData, GetListProductOfferingRecordResponse.class);
                    return Optional.ofNullable(GetListProductOfferingRecordResponse);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Optional<GetListServiceRecordResponse>> getListServiceRecord(GetListServiceRecordRequest request) {
        String payload = ProfileClientUtils.getListServiceRecord(request,profileProperties.getUsername(), profileProperties.getPassword());
        return soapClient.callRaw(profileClient, null,payload)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Mono.empty();
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
                    String realData = DataWsUtil.getDataByTag(formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                            .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER), "<return>", "</return>");
                    String xmlData = "<return>" + realData + "</return>";
                    GetListServiceRecordResponse getListServiceRecordResponse =
                            DataWsUtil.xmlToObj(xmlData, GetListServiceRecordResponse.class);
                    return Optional.ofNullable(getListServiceRecordResponse);
                })
                .doOnError(err -> log.error("Exception when call soap: ", err))
                .onErrorResume(throwable -> {
                    log.error("Exception when call soap");
                    return Mono.empty();
                });
    }
}
