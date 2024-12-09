package com.ezbuy.notificationsend.service.impl;

import com.ezbuy.notificationmodel.dto.SmsResultDTO;
import com.ezbuy.notificationmodel.dto.request.SendMessageRequest;
import com.ezbuy.notificationmodel.model.SendSms;
import com.ezbuy.notificationsend.client.SettingClient;
import com.ezbuy.notificationsend.client.SmsBrandNameClient;
import com.ezbuy.notificationsend.service.SmsService;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final SettingClient settingClient;
    private final SmsBrandNameClient smsBrandNameClient;
    private final HttpCodeStatusMapper healthHttpCodeStatusMapper;
    private final StatusAggregator healthStatusAggregator;

    @Value("${client.sms-brandname.username}")
    private String username;
    @Value("${client.sms-brandname.password}")
    private String password;

    @Override
    public Mono<List<SmsResultDTO>> sendSmsBrandNameOnline(List<SendSms> lstSendSms) {
        List<SmsResultDTO> results = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(lstSendSms)) {
            return Mono.just(results);
        }
        logger.info("send {} sms brandname", lstSendSms.size());
        //neu alias = 155 thì gui offline
//        if (ConstValue.SEND_SMS.DEFAULT_SMS_ALIAS.equals(sms.getAlias())
//                || ConstValue.SEND_SMS.DEFAULT_SMS_CP_CODE.equals(sms.getCpCode())) {
//            return sendSmsOffline(sms).then(Mono.empty());
//        }

//        if (!DataUtil.isNullOrEmpty(mode) && !Const.Common.STRING_ACTIVE_STATUS.equals(mode) && !isViettelIsdn(sms.getIsdn())) {
//
//        }

        return Flux.fromIterable(lstSendSms)
                .flatMap(sendSms -> {
//                    String content = stripAccents(sendSms.getContent());
                    String content = sendSms.getContent();
                    Integer convertVN = 0; //0: khong dau, 1: co dau
                    return sendSmsBrandNameApi(sendSms.getAlias(), List.of(sendSms.getIsdn()), content, convertVN)
                            .map(isOk -> new SmsResultDTO(sendSms.getId(), isOk));
                })
                .collectList();
    }

    private Mono<Boolean> sendSmsBrandNameApi(String aliasName, List<String> msisdn, String content, Integer convertVN) {
        return smsBrandNameClient.login(username, password)
                .flatMap(loginResponse -> {
                    if (loginResponse.getData() == null) {
                        return Mono.error(new BusinessException("SSO00001", "Access token is missing"));
                    }
                    //kiem tra isdn truyen vao
                    SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                            .aliasName(aliasName)
                            .msisdn(msisdn)
                            .content(content)
                            .convertVN(convertVN)
                            .build();
                    String token = loginResponse.getData().getToken();
                    logger.info("Start call api send sms message");
                    return smsBrandNameClient.sendMessage(sendMessageRequest, token)
                            .map(sendMessageResponse -> {
                                if (sendMessageResponse == null
                                        || sendMessageResponse.getCode() == null
                                        || !"GECO_002".equals(sendMessageResponse.getCode())) {
                                    log.warn("Failed to send SMS: {}", sendMessageResponse);
                                    return false;
                                }
                                logger.info("Call api send message successfully.");
                                return true;
                            });
                })
                .doOnError(err -> log.error("Error sending SMS: ", err));
    }

    //Kiem tra so thue bao co phai so cua viettel hay khong
    public boolean isViettelIsdn(String isdn) {
        return !DataUtil.isNullOrEmpty(isdn) && checkIsdnAddPrefix(isdn) != null;
    }

    public static String checkIsdnAddPrefix(String isdn) {
        if(DataUtil.isNullOrEmpty(isdn)) {
            return isdn;
        }
        if (isdn.length() <= 9 && isdn.startsWith("84")) {
            return isdn;
        }
        return isdn.replaceFirst("^0+(?!$)|^84(?!$)", "");
    }

    //                                for(SubMessageDTO subMessageDTO : sendMessageResponse.getSubMessageDTOS()) {
//                                    Integer status = subMessageDTO.getStatus();
//                                    if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.SUCCESS)) {
//                                        continue;
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.BRAND_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00001", Translator.toLocaleVi("sms.submit.error.status.one")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.DUPLICATE_BRAND)) {
//                                        return Mono.error(new BusinessException("SSO00002", Translator.toLocaleVi("sms.submit.error.status.two")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.TEMPLATE_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00003", Translator.toLocaleVi("sms.submit.error.status.three")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.PRICE_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00004", Translator.toLocaleVi("sms.submit.error.status.four")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.INSUFFICIENT_BALANCE)) {
//                                        return Mono.error(new BusinessException("SSO00005", Translator.toLocaleVi("sms.submit.error.status.five")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.BLOCKED_KEYWORDS)) {
//                                        return Mono.error(new BusinessException("SSO00006", Translator.toLocaleVi("sms.submit.error.status.six")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.UNKNOWN_SUBSCRIBER)) {
//                                        return Mono.error(new BusinessException("SSO00007", Translator.toLocaleVi("sms.submit.error.status.seven")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.OPT_OUT)) {
//                                        return Mono.error(new BusinessException("SSO00008", Translator.toLocaleVi("sms.submit.error.status.eight")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.DUPLICATE_AD)) {
//                                        return Mono.error(new BusinessException("SSO00009", Translator.toLocaleVi("sms.submit.error.status.nine")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.CONTENT_LENGTH_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00010", Translator.toLocaleVi("sms.submit.error.status.ten")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.NETWORK_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00011", Translator.toLocaleVi("sms.submit.error.status.eleven")));
//                                    } else if (DataUtil.safeEqual(status, ConstValue.SendSmsErrorCode.UNKNOWN_SUBSCRIBER_ERROR)) {
//                                        return Mono.error(new BusinessException("SSO00012", Translator.toLocaleVi("sms.submit.error.status.twelve")));
//                                    } else {
//                                        return Mono.error(new BusinessException("SSO00013", Translator.toLocaleVi("sms.submit.error.status.thirteen")));
//                                    }
//                                }
}