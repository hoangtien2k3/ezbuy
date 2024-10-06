//package com.ezbuy.orderservice.client.impl;
//
//import com.ezbuy.ordermodel.dto.request.GetGroupsCAinfoRequest;
//import com.ezbuy.ordermodel.dto.sale.ResponseCM;
//import com.ezbuy.orderservice.client.CmPortalClient;
//import com.ezbuy.orderservice.client.properties.CmPortalProperties;
//import com.ezbuy.orderservice.client.utils.CmClientUtils;
//import io.hoangtien2k3.reactify.DataUtil;
//import io.hoangtien2k3.reactify.DataWsUtil;
//import io.hoangtien2k3.reactify.constants.Constants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.Optional;
//
//@Service
//@Slf4j
//@DependsOn("webClientFactory")
//public class CmPortalClientImpl implements CmPortalClient {
//
//    private final BaseSoapClient soapClient;
//
//    private final WebClient cmPortalClient;
//
//    private final CmPortalProperties cmPortalProperties;
//
//    public CmPortalClientImpl(BaseSoapClient soapClient, @Qualifier("cmPortalClient") WebClient cmPortalClient, CmPortalProperties cmPortalProperties) {
//        this.soapClient = soapClient;
//        this.cmPortalClient = cmPortalClient;
//        this.cmPortalProperties = cmPortalProperties;
//    }
//
//    @Override
//    public Mono<Optional<ResponseCM>> getGroupsInfo(GetGroupsCAinfoRequest request) {
//        String payload = CmClientUtils.getGroupsInfo(request, cmPortalProperties.getUsername(), cmPortalProperties.getPassword());
//        return soapClient.callRaw(cmPortalClient, null, payload)
//                .map(response -> {
//                    if (DataUtil.isNullOrEmpty(response)) {
//                        return Optional.empty();
//                    }
//
//                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
//                    String realData = DataWsUtil.getDataByTag(formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
//                            .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER), "<return>", "</return>");
//                    String xmlData = "<return>" + realData + "</return>";
//                    ResponseCM responseResult = DataWsUtil.xmlToObj(xmlData, ResponseCM.class);
//                    return Optional.ofNullable(responseResult);
//                })
//                .doOnError(err -> log.error("Exception when call soap: ", err))
//                .onErrorResume(throwable -> {
//                    log.error("Exception when call soap");
//                    return Mono.just(Optional.empty());
//                });
//    }
//
//    @Override
//    public Mono<Optional<ResponseCM>> getGroupsMembersInfo(GetGroupsCAinfoRequest request) {
//        if (!DataUtil.isNullOrEmpty(request.getIdNo())) {
//            request.setIdNo(DataUtil.safeTrim(request.getIdNo()));
//        }
//        String payload = CmClientUtils.getGroupsMemberInfo(request, cmPortalProperties.getUsername(), cmPortalProperties.getPassword());
//        return soapClient.callRaw(cmPortalClient, null, payload)
//                .map(response -> {
//                    if (DataUtil.isNullOrEmpty(response)) {
//                        return Optional.empty();
//                    }
//
//                    String formattedSOAPResponse = DataWsUtil.formatXML(DataUtil.safeToString(response));
//                    String realData = DataWsUtil.getDataByTag(formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
//                            .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER), "<return>", "</return>");
//                    String xmlData = "<return>" + realData + "</return>";
//                    ResponseCM responseResult = DataWsUtil.xmlToObj(xmlData, ResponseCM.class);
//                    return Optional.ofNullable(responseResult);
//                })
//                .doOnError(err -> log.error("Exception when call soap: ", err))
//                .onErrorResume(throwable -> {
//                    log.error("Exception when call soap");
//                    return Mono.just(Optional.empty());
//                });
//    }
//}
