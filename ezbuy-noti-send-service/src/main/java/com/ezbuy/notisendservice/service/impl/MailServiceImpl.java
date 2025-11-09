package com.ezbuy.notisendservice.service.impl;

import com.ezbuy.notimodel.dto.EmailResultDTO;
import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import com.ezbuy.notisendservice.constants.CommonConstants;
import com.ezbuy.notisendservice.service.MailService;
import com.ezbuy.core.util.DataUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.ezbuy.notisendservice.constants.CommonConstants.*;
import static com.ezbuy.notisendservice.constants.CommonConstants.TemplateMail.*;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis) {
        List<EmailResultDTO> results = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(transmissionNotis)) {
            return Mono.just(results);
        }
        logger.info("send {} email", transmissionNotis.size());
        Flux<EmailResultDTO> resultDTOFlux = Flux.fromArray(transmissionNotis.toArray())
                .flatMap(obj -> {
                    TransmissionNotiDTO transmissionNoti = (TransmissionNotiDTO) obj;
                    String content = renderContent(transmissionNoti);
                    return sendEmail(sender, transmissionNoti.getEmail(), transmissionNoti.getTitle(), content, true)
                            .map(isOk -> new EmailResultDTO(transmissionNoti.getId(), isOk));
                });
        return resultDTOFlux.collectList();
    }

    private String renderContent(TransmissionNotiDTO transmissionNoti) {
        String templateMail = DataUtil.safeToString(transmissionNoti.getTemplateMail());
        return switch (templateMail) {
            case CommonConstants.TemplateMail.SIGN_UP, FORGOT_PASSWORD -> renderOtpMailContent(transmissionNoti);
            case SIGN_UP_PASSWORD -> renderSignUpPasswordMailContent(transmissionNoti);
            case CUSTOMER_ACTIVE_SUCCESS -> mailRegisterAccountSuccessContent(transmissionNoti);
            case CUSTOMER_REGISTER_SUCCESS -> mailAccountCustomerInfo(transmissionNoti);
            case ACCOUNT_ACTIVE -> mailActiveAccountSuccessContent(transmissionNoti);
            case VERIFY_ACCOUNT_SUCESS -> mailVerifyAccountSuccessContent(transmissionNoti);
            case NOTI_VERIFY_ACCOUNT -> mailNotiVerifyAccountContent(transmissionNoti);
            case CREATE_ACCOUNT_SUCCESS -> mailNotiCreateAccountSuccessContent(transmissionNoti);
            case CHANGE_PASSWORD_SUCCESS -> mailNotiChangePasswordSuccessContent(transmissionNoti);
            case APPROVE_ORDER_FAIL -> mailNotificationReceiveApproveResultFailContent(transmissionNoti);
            case NEED_APPROVE -> mailNoticeNeedApprove(transmissionNoti);
            case VERIFY_IDENTIFY_REJECTED -> mailVerifyIdentifyRejected(transmissionNoti);
            case VERIFY_IDENTIFY_SUCCESS -> mailVerifyIdentifySuccess(transmissionNoti);
            default -> "";
        };
    }

    private String renderOtpMailContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("TIME_OTP", transmissionNoti.getExternalData());
            context.setVariable("SUBTITLE", transmissionNoti.getSubTitle());

            String templateMail = DataUtil.safeToString(
                    CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())),
                    "mail/TransmissionOtpMailSignUp.html"
            );
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String renderSignUpPasswordMailContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("USERNAME", transmissionNoti.getExternalData());
            context.setVariable("SUBTITLE", transmissionNoti.getSubTitle());

            String templateMail = DataUtil.safeToString(
                    CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())),
                    "mail/TransmissionOtpMailSignUp.html"
            );
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailRegisterAccountSuccessContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("USERNAME", transmissionNoti.getSubTitle());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_ACTIVE_ACCOUNT_CUSTOMER);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailAccountCustomerInfo(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();

            String[] userInfo = transmissionNoti.getSubTitle().split("-");
            context.setVariable("USERNAME", userInfo[0]);
            context.setVariable("PASSWORD", userInfo[1]);

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())),TEMP_ACCOUNT_CUSTOMER_INFO);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailActiveAccountSuccessContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();

            context.setVariable("USERNAME", transmissionNoti.getExternalData());

            String templateMail = DataUtil.safeToString(
                    CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())),
                    "mail/register_account_success.html"
            );

            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private Mono<Boolean> sendEmail(String sender, String receiver, String subject, String content, boolean isHtml) {
        return Mono.fromCallable(() -> {
                    if (DataUtil.isNullOrEmpty(content)) {
                        logger.error("Content null can not be send to {}", receiver);
                        return false;
                    }
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
                    message.setTo(receiver);
                    message.setFrom(sender);
                    message.setSubject(subject);
                    message.setText(content, isHtml);
                    javaMailSender.send(mimeMessage);
                    logger.info("Sent email to User '{}'", receiver);
                    return true;
                }).onErrorResume(throwable -> {
                    logger.info("throwable: ", throwable);
                    return Mono.just(false);
                })
                .map(isSuccess -> isSuccess);

    }

    private String mailVerifyAccountSuccessContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("SUBTITLE", transmissionNoti.getSubTitle());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_VERIFY_ACCOUNT_SUCESS);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailNotiVerifyAccountContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("SUBTITLE", transmissionNoti.getSubTitle());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_NOTI_VERIFY_ACCOUNT);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailNotiCreateAccountSuccessContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("USERNAME", transmissionNoti.getExternalData());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_CREATE_ACCOUNT_SUCCESS);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailNotiChangePasswordSuccessContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable("USERNAME", transmissionNoti.getExternalData());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_CHANGE_PASSWORD_SUCCESS);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("renderOtpMailContent error: ", ex);
            return "";
        }
    }

    private String mailNotificationReceiveApproveResultFailContent(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable(VariableContextMail.CANCEL_REASON, transmissionNoti.getExternalData());

            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), TEMP_APPROVE_ORDER_FAIL);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("mailNotificationReceiveApproveResultFailContent error: ", ex);
            return "";
        }
    }

    private String mailNoticeNeedApprove(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), NOTI_NEED_APPROVE);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("TransmissionMailNoticeNeedApprove error: ", ex);
            return "";
        }
    }

    private String mailVerifyIdentifyRejected(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            context.setVariable(VariableContextMail.CANCEL_REASON, transmissionNoti.getExternalData());
            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), NOTI_VERIFY_IDENTIFY_REJECTED);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("TransmissionMailVerifyIdentifyRejected error: ", ex);
            return "";
        }
    }

    private String mailVerifyIdentifySuccess(TransmissionNotiDTO transmissionNoti) {
        try {
            Context context = new Context();
            String templateMail = DataUtil.safeToString(CommonConstants.TEMPLATE_MAIL_MAP.get(DataUtil.safeToString(transmissionNoti.getTemplateMail())), NOTI_VERIFY_IDENTIFY_SUCCESS);
            return templateEngine.process(templateMail, context);
        } catch (Exception ex) {
            logger.error("TransmissionMailVerifyIdentifySuccess error: ", ex);
            return "";
        }
    }
}
