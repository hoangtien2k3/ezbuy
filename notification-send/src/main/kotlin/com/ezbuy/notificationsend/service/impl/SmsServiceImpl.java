package com.ezbuy.notificationsend.service.impl;

import com.ezbuy.notificationmodel.common.ConstValue;
import com.ezbuy.notificationmodel.dto.SmsResultDTO;
import com.ezbuy.notificationmodel.dto.request.SendMessageRequest;
import com.ezbuy.notificationmodel.model.SendSms;
import com.ezbuy.notificationsend.client.SettingClient;
import com.ezbuy.notificationsend.client.SmsBrandNameClient;
import com.ezbuy.notificationsend.service.SmsService;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final SettingClient settingClient;
    private final SmsBrandNameClient smsBrandNameClient;
    private final HttpCodeStatusMapper healthHttpCodeStatusMapper;
    private final StatusAggregator healthStatusAggregator;
    private final SpringReactiveOpaqueTokenIntrospector opaqueTokenIntrospector;

    @Value("${client.sms-brandname.username}")
    private String username;
    @Value("${client.sms-brandname.password}")
    private String password;

    @Override
    public Mono<String> sendSmsOnline(SendSms sms) {
        if (sms == null) {
            return Mono.empty();
        }

        // Nếu alias = 155 thì gửi offline
        if (ConstValue.SEND_SMS.DEFAULT_SMS_ALIAS.equals(sms.getAlias()) || ConstValue.SEND_SMS.DEFAULT_SMS_CP_CODE.equals(sms.getCpCode())) {
//            return Mono.fromRunnable(() -> sendSmsOffline(sms))
//                    .then(Mono.empty());
        }

        //lay trong cau hinh ra
        return settingClient.findOptionSetValueByCode(ConstValue.OptionSet.MODE_SEND_SMS_CALL_API, ConstValue.OptionSet.MODE_SEND_SMS_CALL_API)
                .flatMap(response -> {
                    String mode = response.getValue();
                    // Kiểm tra số có phải số nội mạng không
                    if (!DataUtil.isNullOrEmpty(mode) && !ConstValue.Common.STRING_ACTIVE_STATUS.equals(mode) && !isViettelIsdn(sms.getIsdn())) {
                        return settingClient.findOptionSetValueByCode(ConstValue.OptionSet.MODE_SEND_SMS_CONVERT_VN, ConstValue.OptionSet.MODE_SEND_SMS_CONVERT_VN)
                                .flatMap(x -> {
                                    String convertVN = x.getValue();
                                    SendMessageRequest sendMessageRequest = new SendMessageRequest();
                                    sendMessageRequest.setAliasName(sms.getAlias());
                                    sendMessageRequest.setContent(sms.getContent());
                                    sendMessageRequest.setMsisdn(List.of(sms.getIsdn()));
                                    if (ConstValue.SendSmsConvertVN.UNACCENTED.equals(DataUtil.safeToInt(convertVN))) {
                                        sendMessageRequest.setConvertVN(ConstValue.SendSmsConvertVN.UNACCENTED);
                                    } else {
                                        sendMessageRequest.setConvertVN(ConstValue.SendSmsConvertVN.ACCENTED);
                                    }

                                    return smsBrandNameClient.login(username, password)
                                            .flatMap(authRes -> {
                                                if (!DataUtil.isNullOrEmpty(authRes) && !DataUtil.isNullOrEmpty(authRes.getData())) {
                                                    String auth = authRes.getData().getToken();
                                                    return smsBrandNameClient.sendSMSBR(sendMessageRequest, auth)
                                                            .map(sendSmsOnline -> {
                                                                String code = sendSmsOnline.getCode();
                                                                if (!ConstValue.SEND_SMS.GECO_002.equals(code)) {
                                                                    String message = DataUtil.safeToString(sendSmsOnline.getMessage());
                                                                    String requestId = DataUtil.safeToString(sendSmsOnline.getRequestId());
                                                                    return Mono.error(new BusinessException("SSO00002", Translator.toLocaleVi("sms.send.online.call.error", code, message, requestId)));
                                                                } else {
                                                                    return Mono.just(sendSmsOnline.getMessage());
                                                                }
                                                            });
                                                }
                                            });
                                });
                    } else {
                        // Nếu là các alias khác thì gọi ws gửi SMS
                        return orderCpApiService.wsCpMtReactive(sms.getCpCode(), sms.getIsdn(), sms.getIsdn(), sms.getAlias(), sms.getContent())
                                .switchIfEmpty(Mono.error(new BusinessException("SSO00001", Translator.toLocaleVi("sms.send.online.fail.call", sms.getId().toString()))))
                                .flatMap(result -> {
                                    if (DataUtil.safeEqual(ConstValue.SEND_SMS.CCAPI_RESULT_ERROR, result.getResult())) {
                                        return Mono.error(new BusinessException("SSO00002", Translator.toLocaleVi("sms.send.online.call.error", sms.getId().toString(), result.getMessage())));
                                    }
                                    if (DataUtil.safeEqual(ConstValue.SEND_SMS.CCAPI_RESULT_AUTHEN, result.getResult())) {
                                        return Mono.error(new BusinessException("SSO00003", Translator.toLocaleVi("sms.send.online.call.error.authen")));
                                    }
                                    if (DataUtil.safeEqual(ConstValue.SEND_SMS.CCAPI_RESULT_ERROR_SYSTEM, result.getResult())) {
                                        return Mono.error(new BusinessException("SSO00004", Translator.toLocaleVi("sms.send.online.call.error.system", sms.getId().toString())));
                                    }
                                    return Mono.just("OK");
                                });
                    }
                });
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
}