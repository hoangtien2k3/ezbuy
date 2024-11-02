package com.ezbuy.notificationsend.service.impl

import com.ezbuy.notificationsend.constants.CommonConstants
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.ACCOUNT_ACTIVE
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.CUSTOMER_ACTIVE_SUCCESS
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.CUSTOMER_REGISTER_SUCCESS
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.EMPLOYEE_REGISTER_SUCCESS
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.FORGOT_PASSWORD
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.NOTI_VERIFY_ACCOUNT
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.SIGN_UP
import com.ezbuy.notificationsend.constants.CommonConstants.TemplateMail.SIGN_UP_PASSWORD
import com.ezbuy.notificationsend.service.MailService
import com.ezbuy.notificationmodel.dto.EmailResultDTO
import com.ezbuy.notificationmodel.dto.TransmissionNotiDTO
import com.ezbuy.reactify.DataUtil
import com.ezbuy.reactify.constants.Constants.TemplateMail.VERIFY_ACCOUNT_SUCESS
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Service
class MailServiceImpl(
    private val templateEngine: SpringTemplateEngine,
    private val javaMailSender: JavaMailSender,
    @Value("{spring.mail.username}") private val sender: String
) : MailService {

    private val logger = LoggerFactory.getLogger(MailServiceImpl::class.java)

    override fun sendMailByTransmission(transmissionNotis: List<TransmissionNotiDTO>): Mono<List<EmailResultDTO>> {
        val results = mutableListOf<EmailResultDTO>()
        if (transmissionNotis.isEmpty()) {
            return Mono.just(results)
        }
        logger.info("send ${transmissionNotis.size} email")

        val resultDTOFlux = Flux.fromIterable(transmissionNotis)
            .flatMap { transmissionNoti ->
                val content = renderContent(transmissionNoti)
                sendEmail(sender, transmissionNoti.email, transmissionNoti.title, content, true)
                    .map { isOk -> EmailResultDTO(transmissionNoti.id, isOk) }
            }
        return resultDTOFlux.collectList()
    }

    private fun renderContent(transmissionNoti: TransmissionNotiDTO): String {
        val templateMail = DataUtil.safeToString(transmissionNoti.templateMail)
        return when (templateMail) {
            SIGN_UP, FORGOT_PASSWORD, SIGN_UP_PASSWORD -> renderOtpMailContent(transmissionNoti)
            CUSTOMER_ACTIVE_SUCCESS -> mailRegisterAccountSuccessContent(transmissionNoti)
            CUSTOMER_REGISTER_SUCCESS -> mailAccountCustomerInfo(transmissionNoti)
            EMPLOYEE_REGISTER_SUCCESS -> mailRegisterUserSuccessContent(transmissionNoti)
            ACCOUNT_ACTIVE -> mailActiveAccountSuccessContent(transmissionNoti)
            VERIFY_ACCOUNT_SUCESS -> mailVerifyAccountSuccessContent(transmissionNoti)
            NOTI_VERIFY_ACCOUNT -> mailNotiVerifyAccountContent(transmissionNoti)
            else -> ""
        }
    }

    private fun renderOtpMailContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("USERNAME", transmissionNoti.externalData)
                setVariable("SUBTITLE", transmissionNoti.subTitle)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                "mail/TransmissionOtpMailSignUp.html"
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: $ex")
            ""
        }
    }

    private fun mailRegisterAccountSuccessContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("USERNAME", transmissionNoti.subTitle)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                CommonConstants.TEMP_ACTIVE_ACCOUNT_CUSTOMER
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }

    private fun mailAccountCustomerInfo(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context()

            val userInfo = transmissionNoti.subTitle.split("-")
            context.setVariable("USERNAME", userInfo[0])
            context.setVariable("PASSWORD", userInfo[1])

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                CommonConstants.TEMP_ACCOUNT_CUSTOMER_INFO
            )
            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }

    private fun mailActiveAccountSuccessContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("USERNAME", transmissionNoti.externalData)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                "mail/register_account_success.html"
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }

    private fun mailRegisterUserSuccessContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("USERNAME", transmissionNoti.externalData)
                setVariable("PASSWORD", transmissionNoti.subTitle)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                "mail/register_account_success.html"
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }

    private fun sendEmail(sender: String, receiver: String, subject: String, content: String, isHtml: Boolean): Mono<Boolean> {
        return Mono.fromCallable {
            if (DataUtil.isNullOrEmpty(content)) {
                logger.error("Content null can not be send to {}", receiver)
                return@fromCallable false
            }
            val mimeMessage = javaMailSender.createMimeMessage()
            val message = MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name())
            message.setTo(receiver)
            message.setFrom(sender)
            message.setSubject(subject)
            message.setText(content, isHtml)
            javaMailSender.send(mimeMessage)
            logger.info("Sent email to User '{}'", receiver)
            true
        }.onErrorResume { throwable ->
            logger.info("throwable: ", throwable)
            Mono.just(false)
        }
    }

    private fun mailVerifyAccountSuccessContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("SUBTITLE", transmissionNoti.subTitle)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                CommonConstants.TEMP_VERIFY_ACCOUNT_SUCCESS
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }

    private fun mailNotiVerifyAccountContent(transmissionNoti: TransmissionNotiDTO): String {
        return try {
            val context = Context().apply {
                setVariable("SUBTITLE", transmissionNoti.subTitle)
            }

            val templateMail = DataUtil.safeToString(
                CommonConstants.TEMPLATE_MAIL_MAP[DataUtil.safeToString(transmissionNoti.templateMail)],
                CommonConstants.TEMP_NOTI_VERIFY_ACCOUNT
            )

            templateEngine.process(templateMail, context)
        } catch (ex: Exception) {
            logger.error("renderOtpMailContent error: ", ex)
            ""
        }
    }
}
