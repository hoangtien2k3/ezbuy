// package com.ezbuy.productservice.client.impl;
//
// import com.ezbuy.productmodel.request.ApiUtils;
// import
// com.ezbuy.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
// import com.ezbuy.productmodel.request.GetProductTemplateDetailRequest;
// import com.ezbuy.productmodel.response.*;
// import com.ezbuy.productservice.client.ProductClient;
// import com.ezbuy.productservice.client.properties.ProductProperties;
// import com.ezbuy.productservice.client.utils.ProductClientUtils;
// import com.reactify.DataUtil;
// import com.reactify.DataWsUtil;
// import com.reactify.constants.CommonErrorCode;
// import com.reactify.constants.Constants;
// import com.reactify.exception.BusinessException;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.annotation.DependsOn;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
//
// @Slf4j
// @Service
// @DependsOn("webClientFactory")
// @RequiredArgsConstructor
// public class ProductClientImpl implements ProductClient {
//
// private final BaseSoapClient baseSoapClient;
//
// private final WebClient productClient;
//
// private final ProductProperties productProperties;
//
//
// @Override
// public Mono<Optional<ListProductOfferResponse>>
// getListProductOfferTemplateByListIds(List<String> ids) {
// String payload =
// ProductClientUtils.getSearchProductTemplateByIdsPayloadV2(ids,
// productProperties.getUsername(), productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return "";
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String realData = DataWsUtil.getDataByTag(
// formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER)
// .replaceAll(TAG_OPEN_RETURN, "<data>")
// .replaceAll(TAG_CLOSE_RETURN, "</data>"),
// "<ns2:getListProductOfferTemplateByListIdsResponse
// xmlns:ns2=\"http://service.product.bccs.viettel.com/\">",
// "</ns2:getListProductOfferTemplateByListIdsResponse>");
//
// String xmlData = TAG_OPEN_RETURN + realData + TAG_CLOSE_RETURN;
// ListProductOfferResponse productResp =
// DataWsUtil.xmlToObj(xmlData, ListProductOfferResponse.class);
// return Optional.ofNullable(productResp);
// }).doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// log.error("Exception when call soap");
// return Mono.error(new
// BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
// "call.api.product.error"));
// });
// }
//
// @Override
// public Mono<Optional<ListProductOfferResponse>> getLstTemplateOffer(String
// telecomServiceId, ApiUtils utils, List<String> priceTypes) {
// String payload =
// ProductClientUtils.getSearchProductTemplatePayload(telecomServiceId, utils,
// productProperties.getUsername(), productProperties.getPassword(),
// priceTypes);
// return baseSoapClient.call(productClient, null, payload,
// ListProductOfferResponse.class)
// .doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// log.error("Exception when call soap");
// return Mono.error(new
// BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
// "call.api.product.error"));
// });
// }
//
// @Override
// public Mono<Optional<LstServiceCharacteristicResponse>>
// getLstProductSpec(String telecomServicesId) {
// List<String> telecomServicesIds = new ArrayList<>();
// telecomServicesIds.add(telecomServicesId);
// String payload = ProductClientUtils.getLstProductSpec(telecomServicesIds,
// productProperties.getUsername(), productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return Optional.empty();
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String realData = DataWsUtil.getDataByTag(
// formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER)
// .replaceAll(TAG_OPEN_RETURN, "<data>")
// .replaceAll(TAG_CLOSE_RETURN, "</data>"),
// "<ns2:getLstServiceCharacteristicResponse
// xmlns:ns2=\"http://service.product.bccs.viettel.com/\">",
// "</ns2:getLstServiceCharacteristicResponse>");
//
// String xmlData = TAG_OPEN_RETURN + realData + TAG_CLOSE_RETURN;
// LstServiceCharacteristicResponse productResp =
// DataWsUtil.xmlToObj(xmlData, LstServiceCharacteristicResponse.class);
// return Optional.ofNullable(productResp);
// })
// .onErrorResume(throwable -> {
// log.error("Exception when call ws BCCS_PRODUCT: {}", throwable);
// return Mono.just(Optional.empty());
// });
// }
//
// @Override
// public Mono<Optional<ProductOfferingSpecificationResponse>>
// getProductOfferingSpecification(String productId) {
// String payload = ProductClientUtils.getProductOffering(productId,
// productProperties.getStaffCode(), productProperties.getUsername(),
// productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return "";
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String realData = DataWsUtil.getDataByTag(
// formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER),
// "productSpecificationDTO>",
// "</productSpecificationDTO>");
//
// String xmlData = TAG_OPEN_RETURN + realData + TAG_CLOSE_RETURN;
// ProductOfferingSpecificationResponse productResp =
// DataWsUtil.xmlToObj(xmlData, ProductOfferingSpecificationResponse.class);
// return Optional.ofNullable(productResp);
// }).doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// ProductOfferingSpecificationResponse response = new
// ProductOfferingSpecificationResponse();
// response.setLstProductSpecCharUseDTO(new ArrayList<>());
// return Mono.just(Optional.of(response));
// });
// }
//
// @Override
// public Mono<Optional<GetListProductOfferingComboForHubSmeResponse>>
// getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest
// request) {
// String payload =
// ProductClientUtils.getListProductOfferingComboForHubSme(request,
// productProperties.getUsername(), productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return "";
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String replaceReturn = formattedSOAPResponse.replace("return",
// "listProductOfferingCombo");
// String realData = DataWsUtil.getDataByTag(
// replaceReturn.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER),
// "ns2:getListProductOfferingComboForHubSmeResponse
// xmlns:ns2=\"http://service.product.bccs.viettel.com/\">",
// "</ns2:getListProductOfferingComboForHubSmeResponse>");
// String xmlData = TAG_OPEN_RETURN + realData + TAG_CLOSE_RETURN;
// GetListProductOfferingComboForHubSmeResponse data =
// DataWsUtil.xmlToObj(xmlData,
// GetListProductOfferingComboForHubSmeResponse.class);
// return Optional.ofNullable(data);
// }).doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// GetListProductOfferingComboForHubSmeResponse response = new
// GetListProductOfferingComboForHubSmeResponse();
// response.setListProductOfferingCombo(new ArrayList<>());
// return Mono.just(Optional.of(response));
// });
// }
//
// @Override
// public Mono<Optional<GetListTemplateComboForHubSmeResponse>>
// getListTemplateComboForHubSme(String productOfferingId) {
// String payload =
// ProductClientUtils.getListTemplateComboForHubSmeBody(productOfferingId,
// productProperties.getUsername(), productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return "";
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String replaceReturn = formattedSOAPResponse.replace("return",
// "listTemplateCombo");
// String realData = DataWsUtil.getDataByTag(
// replaceReturn.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER),
// "ns2:getListTemplateComboForHubSmeResponse
// xmlns:ns2=\"http://service.product.bccs.viettel.com/\">",
// "</ns2:getListTemplateComboForHubSmeResponse>");
//
// String xmlData = TAG_OPEN_RETURN + realData + TAG_CLOSE_RETURN;
// GetListTemplateComboForHubSmeResponse data =
// DataWsUtil.xmlToObj(xmlData, GetListTemplateComboForHubSmeResponse.class);
// return Optional.ofNullable(data);
// }).doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// GetListTemplateComboForHubSmeResponse response = new
// GetListTemplateComboForHubSmeResponse();
// response.setListTemplateCombo(new ArrayList<>());
// return Mono.just(Optional.of(response));
// });
// }
//
// @Override
// public Mono<Optional<GetProductTemplateDetailResponse>>
// getProductTemplateDetail(GetProductTemplateDetailRequest
// getProductTemplateDetailRequest) {
// String payload =
// ProductClientUtils.getProductTemplateDetailPayload(getProductTemplateDetailRequest,
// productProperties.getUsername(), productProperties.getPassword());
// return baseSoapClient.callRaw(productClient, null, payload)
// .map(response -> {
// if (DataUtil.isNullOrEmpty(response)) {
// return "";
// }
// String formattedSOAPResponse =
// DataWsUtil.formatXML(DataUtil.safeToString(response));
// String realData = DataWsUtil.getDataByTag(
// formattedSOAPResponse.replaceAll(Constants.XmlConst.AND_LT_SEMICOLON,
// Constants.XmlConst.LT_CHARACTER)
// .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON,
// Constants.XmlConst.GT_CHARACTER),
// "<ns2:getProductTemplateDetailResponse
// xmlns:ns2=\"http://service.product.bccs.viettel.com/\">",
// "</ns2:getProductTemplateDetailResponse>");
// List<String> returnList = DataWsUtil.getListDataByTag(realData,
// TAG_OPEN_RETURN, TAG_CLOSE_RETURN);
// List<GetProductTemplateDetailResponseItemDTO> listTemplateDetail = new
// ArrayList<>();
// returnList.forEach(item -> {
// GetProductTemplateDetailResponseItemDTO productResp =
// DataWsUtil.xmlToObj(TAG_OPEN_RETURN + item + TAG_CLOSE_RETURN,
// GetProductTemplateDetailResponseItemDTO.class);
// listTemplateDetail.add(productResp);
// });
// GetProductTemplateDetailResponse result = new
// GetProductTemplateDetailResponse();
// result.setProductTemplateDetailItemList(listTemplateDetail);
// return Optional.ofNullable(result);
// }).doOnError(err -> log.error(CommonErrorCode.CALL_SOAP_ERROR, err))
// .onErrorResume(throwable -> {
// log.error("Exception when call soap");
// return Mono.error(new
// BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
// "call.api.product.error"));
// });
// }
// }
