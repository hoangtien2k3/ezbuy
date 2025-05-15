package com.ezbuy.notificationsend.service.impl

import com.ezbuy.notificationsend.client.AuthClient
import com.ezbuy.notificationsend.repository.TransmissionRepository
import com.ezbuy.notificationsend.service.MailService
import com.ezbuy.notificationsend.service.TransmissionService
import com.ezbuy.notificationmodel.dto.EmailResultDTO
import com.ezbuy.notificationmodel.dto.TransmissionNotiDTO
import com.reactify.util.AppUtils
import com.reactify.util.DataUtil
import com.reactify.util.ValidateUtils
import com.reactify.model.response.DataResponse
import com.reactify.constants.Regex
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class TransmissionServiceImpl(
    private val transmissionRepository: TransmissionRepository,
    private val authClient: AuthClient,
    private val mailService: MailService
) : TransmissionService {

    private val log = LoggerFactory.getLogger(TransmissionServiceImpl::class.java)

    @Value("\${config.resendCount}")
    private var resendCount: Int = 0

    @Value("\${config.limit}")
    private var limit: Int = 0

    @Transactional
    override fun sendNotification(): Mono<DataResponse<Any>> {
        return transmissionRepository
            .getTransmissionsToSendMail(resendCount, limit)
            .collectList()
            .flatMap { transmissionNotis ->
                if (DataUtil.isNullOrEmpty(transmissionNotis)) {
                    log.info("\t\t transmission list is empty")
                    return@flatMap Mono.just(
                        DataResponse(null, "success")
                    )
                }

                val successIds = mutableListOf<String>()
                val emailTransmissions = transmissionNotis.filter { transmissionNoti ->
                    if (DataUtil.safeEqual(transmissionNoti.type, "REST")) {
                        successIds.add(transmissionNoti.id)
                        false
                    } else {
                        true
                    }
                }
                if (emailTransmissions.all { DataUtil.isNullOrEmpty(it.email) }) {
                    log.info("Email transmission empty")
                }
                val updateResultVar = if (DataUtil.isNullOrEmpty(successIds)) {
                    Mono.just(true)
                } else {
                    AppUtils.insertData(transmissionRepository.updateTransmissionRestSuccessState(successIds))
                }

                updateResultVar.flatMap {
                    successIds.clear()
                    val emailResultMono = if (emailTransmissions.isNotEmpty()) {
                        val transmissionHaveEmailList = emailTransmissions.filter {
                            !DataUtil.isNullOrEmpty(it.email) && ValidateUtils.validateRegex(
                                DataUtil.safeTrim(it.email), Regex.EMAIL_REGEX
                            )
                        }
                        val receiverIds = emailTransmissions.filter {
                            !DataUtil.isNullOrEmpty(it.receiver) && (DataUtil.isNullOrEmpty(
                                DataUtil.safeTrim(it.email))
                                    || !ValidateUtils.validateRegex(
                                DataUtil.safeTrim(it.email),
                                Regex.EMAIL_REGEX
                            ))
                        }.map { it.receiver }
                        handleEmails(receiverIds, transmissionHaveEmailList, emailTransmissions)
                    } else {
                        Mono.just(emptyList())
                    }
                    emailResultMono.flatMap { emailResults -> updateTransmissionStatus(emailResults, successIds) }
                }
            }
    }

    private fun handleEmails(
        receiverIds: List<String>,
        transmissionHaveEmailList: List<TransmissionNotiDTO>,
        emailTransmissions: List<TransmissionNotiDTO>
    ): Mono<List<EmailResultDTO>> {
        return if (DataUtil.isNullOrEmpty(receiverIds)) {
            Mono.just(HashMap())
        } else {
            combineMap(partitionList(receiverIds, 200))
        }.flatMap { contactInfoMap ->
            val emailNotis = mutableListOf<TransmissionNotiDTO>()
            for (transmissionNoti in emailTransmissions) {
                val email = if (DataUtil.isNullOrEmpty(transmissionNoti.receiver)) {
                    transmissionNoti.email
                } else {
                    contactInfoMap[transmissionNoti.receiver]
                }
                if (!DataUtil.isNullOrEmpty(email) && !DataUtil.isNullOrEmpty(transmissionNoti.sender)) {
                    transmissionNoti.email = email
                    emailNotis.add(transmissionNoti)
                }
            }
            emailNotis.addAll(transmissionHaveEmailList)
            mailService.sendMailByTransmission(emailNotis)
        }
    }

    private fun updateTransmissionStatus(
        emailResults: List<EmailResultDTO>,
        successIds: MutableList<String>
    ): Mono<DataResponse<Any>> {
        val failedIds = emailResults.filter { !it.isSuccess }.map { it.transmissionId }
        successIds.addAll(emailResults.filter { it.isSuccess }.map { it.transmissionId })
        val successUpdateMono = if (DataUtil.isNullOrEmpty(successIds)) {
            Mono.just(true)
        } else {
            transmissionRepository.updateTransmissionEmailSuccessState(successIds)
        }
        val failedUpdateMono = if (DataUtil.isNullOrEmpty(failedIds)) {
            Mono.just(true)
        } else {
            transmissionRepository.updateTransmissionFailedState(failedIds)
        }
        return Mono.zip(successUpdateMono, failedUpdateMono).map {
            DataResponse(
                null,
                "success"
            )
        }
    }

    private fun partitionList(list: List<String>, size: Int): List<List<String>> {
        return list.chunked(size)
    }

    private fun combineMap(listOfMax200List: List<List<String>>): Mono<Map<String, String>> {
        val emailMonoList = listOfMax200List.map { receiverIdList ->
            authClient.getContacts(receiverIdList).map { responseOpt ->
                val map = mutableMapOf<String, String>()
                if (DataUtil.isNullOrEmpty(responseOpt)) {
                    log.info("Email list not found")
                    return@map map
                }
                responseOpt.data.forEach { contactInfoDTO ->
                    map[DataUtil.safeToString(contactInfoDTO.id)] = DataUtil.safeToString(contactInfoDTO.email)
                }
                log.info("Get ${map.size} email")
                map
            }
        }
        return Mono.zip(emailMonoList) { results ->
            results.flatMap { (it as Map<String, String>).entries }.associate { it.toPair() }
        }
    }
}
