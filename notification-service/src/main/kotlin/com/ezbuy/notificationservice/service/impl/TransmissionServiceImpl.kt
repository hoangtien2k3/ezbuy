package com.ezbuy.notificationservice.service.impl

import com.ezbuy.notificationservice.client.AuthClient
import com.ezbuy.notificationservice.repository.*
import com.ezbuy.notificationservice.service.TransmissionService
import com.ezbuy.notificationmodel.common.ConstValue
import com.ezbuy.notificationmodel.common.ConstValue.Channel.*
import com.ezbuy.notificationmodel.common.ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC
import com.ezbuy.notificationmodel.common.ConstValue.ContentTypeConstant.HTML
import com.ezbuy.notificationmodel.common.ConstValue.ContentTypeConstant.TEXT
import com.ezbuy.notificationmodel.common.ConstValue.NotiServerity.CRITICAL
import com.ezbuy.notificationmodel.common.ConstValue.NotiServerity.NORMAL
import com.ezbuy.notificationmodel.common.ConstValue.NotificationConstant.ANNOUNCEMENT
import com.ezbuy.notificationmodel.common.ConstValue.NotificationConstant.NEWS
import com.ezbuy.notificationmodel.common.ConstValue.TransmissionState.*
import com.ezbuy.notificationmodel.dto.request.CreateNotificationDTO
import com.ezbuy.notificationmodel.dto.request.NotiContentDTO
import com.ezbuy.notificationmodel.dto.request.ReceiverDataDTO
import com.ezbuy.notificationmodel.dto.response.CountNoticeDTO
import com.ezbuy.notificationmodel.dto.response.CountNoticeResponseDTO
import com.ezbuy.notificationmodel.dto.response.NotificationHeader
import com.ezbuy.notificationmodel.model.NotificationContent
import com.ezbuy.notificationmodel.model.Transmission
import com.ezbuy.notificationmodel.model.Notification
import com.reactify.constants.CommonErrorCode.*
import com.reactify.constants.Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN
import com.reactify.exception.BusinessException
import com.reactify.model.TokenUser
import com.reactify.model.response.DataResponse
import com.reactify.constants.Regex
import com.reactify.util.*
import io.r2dbc.spi.Row
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class TransmissionServiceImpl(
    private val transmissionRepository: TransmissionRepository,
    private val notificationCategoryRepository: NotificationCategoryRepository,
    private val notificationContentRepository: NotificationContentRepository,
    private val notificationRepository: NotificationRepository,
    private val channelRepository: ChannelRepository,
    private val authClient: AuthClient,
    private val template: R2dbcEntityTemplate,
    @Value("\${config.resendCount}")
    private val resendCount: Int
) : TransmissionService {

    override fun getCountNoticeResponseDTO(): Mono<DataResponse<CountNoticeResponseDTO>> {
        return SecurityUtils.getCurrentUser().flatMap { user ->
            transmissionRepository.getListCountNoticeDTO(user.id)
                .collect(Collectors.toMap({ it.type }, { it.quantity }, Int::plus))
                .map { totalMap ->
                    val total = totalMap.values.sum()
                    val details = totalMap.map { CountNoticeDTO(it.key, it.value) }
                    val countNoticeResponseDTO = CountNoticeResponseDTO(total, details)
                    DataResponse(
                        null,
                        Translator.toLocaleVi(SUCCESS),
                        countNoticeResponseDTO
                    )
                }
                .switchIfEmpty(
                    Mono.just(
                        DataResponse(
                            null,
                            Translator.toLocaleVi(SUCCESS),
                            CountNoticeResponseDTO(0, emptyList())
                        )
                    )
                )
        }
    }

    override fun changeTransmissionStateByIdAndReceiver(
        state: String,
        notificationContentId: String,
        transmissionId: String
    ): Mono<DataResponse<Any>> {
        return SecurityUtils.getCurrentUser().flatMap { user ->
            when {
                DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) &&
                        DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId)) -> {
                    Mono.error(
                        BusinessException(
                            BAD_REQUEST,
                            "params.notificationContentId.transmissionId.notnull"
                        )
                    )
                }

                DataUtil.isNullOrEmpty(state) -> {
                    Mono.error(
                        BusinessException(
                            BAD_REQUEST,
                            "params.state.null"
                        )
                    )
                }

                DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) &&
                        !DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId)) -> {
                    if (!DataUtil.isUUID(transmissionId)) {
                        return@flatMap Mono.error(
                            BusinessException(
                                INVALID_PARAMS,
                                "params.invalid.format"
                            )
                        )
                    }
                    if (DataUtil.safeTrim(state) in listOf(READ, UNREAD, PENDING, FAILED, NEW, SENT)) {
                        return@flatMap transmissionRepository
                            .findByIdAndStatus(transmissionId, ConstValue.Status.ACTIVE)
                            .flatMap { transmission ->
                                transmission.apply {
                                    this.state = state
                                    this.updateBy = "system"
                                    this.updateAt = null
                                }
                                transmissionRepository
                                    .updateTransmissionStateById(transmission.id, transmission.state)
                                    .flatMap {
                                        Mono.just(
                                            DataResponse<Any>(
                                                null,
                                                Translator.toLocaleVi(
                                                    SUCCESS
                                                ),
                                                null
                                            )
                                        )
                                    }.switchIfEmpty(
                                        Mono.just(
                                            DataResponse<Any>(
                                                null,
                                                Translator.toLocaleVi(
                                                    SUCCESS
                                                ),
                                                null
                                            )
                                        )
                                    )
                            }.switchIfEmpty(
                                Mono.error(
                                    BusinessException(
                                        NOT_FOUND,
                                        "transmission.findById.not.found"
                                    )
                                )
                            )
                    } else {
                        Mono.error(
                            BusinessException(
                                INVALID_PARAMS,
                                "params.state.invalid"
                            )
                        )
                    }
                }

                !DataUtil.isUUID(notificationContentId) -> {
                    Mono.error(
                        BusinessException(
                            INVALID_PARAMS,
                            "params.invalid.format"
                        )
                    )
                }

                DataUtil.safeTrim(state) in listOf(READ, UNREAD, PENDING, FAILED, NEW, SENT) -> {
                    return@flatMap transmissionRepository
                        .getListTransId(user.id, notificationContentId)
                        .collectList()
                        .flatMap { listId ->
                            if (DataUtil.isNullOrEmpty(listId)) {
                                Mono.error(
                                    BusinessException(
                                        NOT_FOUND,
                                        "transmission.not.found"
                                    )
                                )
                            } else {
                                transmissionRepository
                                    .changeStateTransmissionByNotiIdAndReceiver(
                                        DataUtil.safeTrim(state),
                                        DataUtil.safeTrim(user.id),
                                        DataUtil.safeTrim(notificationContentId)
                                    ).then(
                                        Mono.defer {
                                            Mono.just(
                                                DataResponse<Any>(
                                                    null,
                                                    Translator.toLocaleVi(
                                                        SUCCESS
                                                    ),
                                                    null
                                                )
                                            )
                                        }
                                    ).switchIfEmpty(
                                        Mono.just(
                                            DataResponse<Any>(
                                                null,
                                                Translator.toLocaleVi(
                                                    SUCCESS
                                                ),
                                                null
                                            )
                                        )
                                    )
                            }
                        }
                }

                else -> {
                    Mono.error(
                        BusinessException(
                            INVALID_PARAMS,
                            "params.state.invalid"
                        )
                    )
                }
            }
        }
    }

    @Transactional
    override fun insertTransmission(createNotificationDTO: CreateNotificationDTO): Mono<DataResponse<Any>> {
        return validateCreateNotificationDTO(createNotificationDTO)
            .flatMap { validatedDTO ->
                SecurityUtils.getCurrentUser()
            }.flatMap { tokenUser ->
                val notiContentId = UUID.randomUUID().toString()
                val notiId = UUID.randomUUID().toString()
                val notiContentDTO = createNotificationDTO.notiContentDTO

                val notificationContent = buildNotificationContent(
                    notiContentId,
                    notiContentDTO,
                    createNotificationDTO.templateMail,
                    tokenUser
                )
                notificationContentRepository.save(notificationContent)
                    .flatMap {
                        handleNotificationCreation(createNotificationDTO, tokenUser, notiContentId, notiId)
                    }.flatMap {
                        handleTransmissionCreation(createNotificationDTO, tokenUser, notiId)
                    }.map { transmissions -> DataResponse.success(transmissions) }
            }
    }

    private fun buildNotificationContent(
        notiContentId: String, notiContentDTO: NotiContentDTO, templateMail: String?, tokenUser: TokenUser
    ): NotificationContent {
        return NotificationContent.builder()
            .id(notiContentId.trim())
            .title(DataUtil.safeTrim(notiContentDTO.title))
            .subTitle(DataUtil.safeTrim(notiContentDTO.subTitle))
            .imageUrl(DataUtil.safeTrim(notiContentDTO.imageUrl))
            .url(DataUtil.safeTrim(notiContentDTO.url))
            .createBy(tokenUser.username)
            .updateBy(tokenUser.username)
            .status(ConstValue.Status.ACTIVE)
            .templateMail(DataUtil.safeToString(templateMail))
            .externalData(notiContentDTO.externalData)
            .build()
    }

    private fun handleNotificationCreation(
        createNotificationDTO: CreateNotificationDTO, tokenUser: TokenUser, notiContentId: String, notiId: String
    ): Mono<Notification> {
        return notificationCategoryRepository
            .findCategoryIdByType(DataUtil.safeTrim(createNotificationDTO.categoryType))
            .switchIfEmpty(Mono.error(
                BusinessException(
                    INTERNAL_SERVER_ERROR,
                    "category.not.found"
                )
            ))
            .flatMap { categoryId ->
                val notification =
                    buildNotification(createNotificationDTO, tokenUser, notiContentId, notiId, categoryId)
                notificationRepository.save(notification)
            }
    }

    private fun buildNotification(
        createNotificationDTO: CreateNotificationDTO,
        tokenUser: TokenUser,
        notiContentId: String,
        notiId: String,
        categoryId: String
    ): Notification {
        return Notification.builder()
            .id(notiId.trim())
            .contentType(DataUtil.safeTrim(createNotificationDTO.contentType))
            .createBy(tokenUser.username)
            .updateBy(tokenUser.username)
            .expectSendTime(createNotificationDTO.expectSendTime)
            .categoryId(categoryId)
            .notificationContentId(notiContentId)
            .sender(DataUtil.safeTrim(createNotificationDTO.sender))
            .severity(DataUtil.safeTrim(createNotificationDTO.severity))
            .status(ConstValue.Status.ACTIVE)
            .build()
    }

    private fun handleTransmissionCreation(
        createNotificationDTO: CreateNotificationDTO, tokenUser: TokenUser, notiId: String
    ): Mono<List<Transmission>> {
        if (DataUtil.isNullOrEmpty(createNotificationDTO.receiverList)) {
            return Mono.error(
                BusinessException(
                    NOT_FOUND,
                    Translator.toLocaleVi("no.receiver")
                )
            )
        }

        val invalidReceiver = createNotificationDTO.receiverList.stream()
            .anyMatch { receiver ->
                !DataUtil.isUUID(DataUtil.safeTrim(receiver.userId)) &&
                        DataUtil.isNullOrEmpty(receiver.email)
            }

        if (invalidReceiver) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    Translator.toLocaleVi("receiver.string.invalid")
                )
            )
        }

        return channelRepository
            .findChannelIdByType(DataUtil.safeTrim(createNotificationDTO.channelType))
            .switchIfEmpty(Mono.error(
                BusinessException(
                    INTERNAL_SERVER_ERROR,
                    "params.channelId.notExist"
                )
            ))
            .flatMap { channelId ->
                val transmissionList = createTransmissionList(createNotificationDTO, tokenUser, notiId, channelId)
                if (DataUtil.isNullOrEmpty(transmissionList)) {
                    return@flatMap Mono.error(
                        BusinessException(
                            INVALID_PARAMS,
                            "no.receiver"
                        )
                    )
                }
                transmissionRepository.saveAll(transmissionList).collectList()
            }
    }

    private fun createTransmissionList(
        createNotificationDTO: CreateNotificationDTO, tokenUser: TokenUser, notiId: String, channelId: String
    ): List<Transmission> {
        return createNotificationDTO.receiverList.distinct()
            .map { receiver -> buildTransmission(receiver, tokenUser, notiId, channelId) }
    }

    private fun buildTransmission(
        receiver: ReceiverDataDTO, tokenUser: TokenUser, notiId: String, channelId: String
    ): Transmission {
        val transmission = Transmission.builder()
            .id(UUID.randomUUID().toString())
            .notificationId(notiId)
            .channelId(channelId)
            .status(ConstValue.Status.ACTIVE)
            .state(PENDING)
            .resendCount(0)
            .updateBy(tokenUser.username)
            .createBy(tokenUser.username)
            .build()
        if (DataUtil.isUUID(receiver.userId.trim())) {
            transmission.receiver = receiver.userId.trim()
            transmission.email = DataUtil.safeTrim(receiver.email)
        } else if (ValidateUtils.validateRegex(receiver.email, Regex.EMAIL_REGEX)) {
            transmission.email = DataUtil.safeTrim(receiver.email)
        }
        return transmission
    }

    override fun getNewNotiWhenOnline(newestNotiTime: String): Mono<DataResponse<List<NotificationContent>>> {
        if (DataUtil.isNullOrEmpty(newestNotiTime)) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    "params.newestNotiTime.notnull"
                )
            )
        }
        if (DataUtil.isNullOrEmpty(DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    "params.invalid.format"
                )
            )
        }
        return SecurityUtils.getCurrentUser().flatMap { tokenUser ->
            transmissionRepository.getAllNotificationContentByCreateAtAfter(
                tokenUser.id,
                DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN)
            )
                .collectList()
                .flatMap { listNotiContent ->
                    Mono.just(
                        DataResponse(
                            null,
                            Translator.toLocaleVi(SUCCESS),
                            listNotiContent
                        )
                    )
                }
                .switchIfEmpty(Mono.just(
                    DataResponse(
                        null,
                        Translator.toLocaleVi(SUCCESS),
                        emptyList()
                    )
                ))
        }
    }

    override fun getNotificationContentListByCategoryType(
        categoryType: String,
        pageIndex: Int?,
        pageSize: Int?,
        sort: String?
    ): Mono<DataResponse<List<NotificationHeader>>> {
        if (pageIndex?.let { it < 1 } == true) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    "params.pageIndex.invalid"
                )
            )
        }
        if (pageSize?.let { it < 1 } == true) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    "params.pageSize.invalid"
                )
            )
        }
        return SecurityUtils.getCurrentUser().flatMap { user ->
            var sortingString = SortingUtils.parseSorting(sort, NotificationHeader::class.java)
            if (DataUtil.isNullOrEmpty(sortingString)) {
                sortingString = ""
            }
            val query = """
          SELECT nc.*, tr.state
          FROM notification_content nc
          INNER JOIN notification n ON n.notification_content_id = nc.id
          INNER JOIN notification_category nca ON n.category_id = nca.id
          INNER JOIN transmission tr ON tr.notification_id = n.id
          INNER JOIN channel c ON tr.channel_id = c.id
          WHERE tr.receiver = :receiver
          AND tr.status = 1
          AND tr.state IN ('NEW', 'UNREAD', 'READ')
          AND nc.status = 1
          AND n.status = 1
          AND nca.status = 1
          AND c.status = 1
          AND c.type = 'REST'
          AND nca.type = :categoryType
          ORDER BY %s
          LIMIT :pageSize OFFSET :index
        """.trimIndent().format(sortingString)

            val index = ((pageIndex ?: 1) - 1) * (pageSize ?: 10)
            template.databaseClient.sql(query)
                .bind("receiver", user.id)
                .bind("categoryType", DataUtil.safeTrim(categoryType))
                .bind("pageSize", pageSize ?: 10)
                .bind("index", index)
                .map { row -> this.build(row as Row) }
                .all()
                .collectList()
                .flatMap { notificationContent ->
                    Mono.just(
                        DataResponse(
                            null,
                            SUCCESS,
                            notificationContent
                        )
                    )
                }
                .switchIfEmpty(Mono.just(
                    DataResponse(
                        null,
                        SUCCESS,
                        emptyList()
                    )
                ))
        }
    }

    fun validateCreateNotificationDTO(createNotificationDTO: CreateNotificationDTO): Mono<DataResponse<Any>> {
        if (DataUtil.isNullOrEmpty(createNotificationDTO.severity)) {
            createNotificationDTO.severity = NORMAL
        }
        // NORMAL or CRITICAL
        if (DataUtil.safeTrim(createNotificationDTO.severity) != NORMAL && DataUtil.safeTrim(createNotificationDTO.severity) != CRITICAL) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    INVALID_FORMAT_SPEC,
                    "Severity"
                )
            )
        }
        // ANNOUNCEMENT or NEWS
        if (DataUtil.safeTrim(createNotificationDTO.categoryType) != ANNOUNCEMENT && DataUtil.safeTrim(
                createNotificationDTO.categoryType
            ) != NEWS
        ) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    INVALID_FORMAT_SPEC,
                    "CategoryType"
                )
            )
        }
        // text/plain or html/plain
        if (DataUtil.safeTrim(createNotificationDTO.contentType) != TEXT && DataUtil.safeTrim(createNotificationDTO.contentType) != HTML) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    INVALID_FORMAT_SPEC,
                    "ContentType"
                )
            )
        }
        // SMS or EMAIL or REST
        if (DataUtil.safeTrim(createNotificationDTO.channelType) != CHANNEL_SMS &&
            DataUtil.safeTrim(createNotificationDTO.channelType) != CHANNEL_EMAIL &&
            createNotificationDTO.channelType != CHANNEL_REST
        ) {
            return Mono.error(
                BusinessException(
                    INVALID_PARAMS,
                    INVALID_FORMAT_SPEC,
                    "ChannelType"
                )
            )
        }
        // validate: NotiContentDTO
        notiContentDTOIsValid(createNotificationDTO.notiContentDTO)
        if (DataUtil.isNullOrEmpty(createNotificationDTO.sendAll)) {
            createNotificationDTO.sendAll = false
        }
        if (DataUtil.isNullOrEmpty(createNotificationDTO.receiverList)) {
            createNotificationDTO.receiverList = mutableListOf()
        } else {
            createNotificationDTO.receiverList.forEach { this.isReceiverDataDTOValid(it) }
        }
        return if (createNotificationDTO.sendAll == true) {
            authClient.getAllUserId().flatMap { list ->
                if (DataUtil.isNullOrEmpty(list)) {
                    return@flatMap Mono.error(
                        BusinessException(
                            NOT_FOUND,
                            "no.receiver"
                        )
                    )
                }
                val receiverDataDTOList = createNotificationDTO.receiverList
                receiverDataDTOList.addAll(list.map { ReceiverDataDTO(it, null) })
                createNotificationDTO.receiverList = receiverDataDTOList
                Mono.just(
                    DataResponse(
                        null,
                        SUCCESS,
                        createNotificationDTO
                    )
                )
            }
        } else {
            if (!DataUtil.isNullOrEmpty(createNotificationDTO.receiverList)) {
                createNotificationDTO.receiverList.forEach { this.isReceiverDataDTOValid(it) }
                Mono.just(
                    DataResponse(
                        null,
                        SUCCESS,
                        createNotificationDTO
                    )
                )
            } else {
                Mono.error(
                    BusinessException(
                        NOT_FOUND,
                        "no.receiver"
                    )
                )
            }
        }
    }

    private fun notiContentDTOIsValid(notiContentDTO: NotiContentDTO) {
        if (DataUtil.isNullOrEmpty(notiContentDTO)) {
            throw BusinessException(
                INVALID_PARAMS,
                Translator.toLocaleVi(
                    "params.object.null",
                    NotiContentDTO.builder().build().javaClass.simpleName
                )
            )
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.title)) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.title.null"
            )
        } else if (notiContentDTO.title.length > 500) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.title.outOfLength"
            )
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.subTitle)) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.subTitle.null"
            )
        } else if (notiContentDTO.subTitle.length > 5000) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.subTitle.outOfLength"
            )
        }
        if (DataUtil.safeTrim(notiContentDTO.imageUrl).length > 500 && !DataUtil.isNullOrEmpty(notiContentDTO.imageUrl)) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.imageUrl.outOfLength"
            )
        }
        if (DataUtil.safeTrim(notiContentDTO.url).length > 300 && !DataUtil.isNullOrEmpty(notiContentDTO.url)) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.url.outOfLength"
            )
        }
    }

    private fun isReceiverDataDTOValid(receiverDataDTO: ReceiverDataDTO) {
        if (DataUtil.isNullOrEmpty(receiverDataDTO.userId) && DataUtil.isNullOrEmpty(receiverDataDTO.email)) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.receiverDataDTO.notAllNull"
            )
        }
        if (!DataUtil.isNullOrEmpty(receiverDataDTO.userId) &&
            !DataUtil.isUUID(receiverDataDTO.userId) &&
            (DataUtil.isNullOrEmpty(receiverDataDTO.email) ||
                    !ValidateUtils.validateRegex(receiverDataDTO.email, Regex.EMAIL_REGEX))
        ) {
            throw BusinessException(
                INVALID_PARAMS,
                "receiver.string.invalid"
            )
        }
        if (DataUtil.safeTrim(receiverDataDTO.email).length > 200) {
            throw BusinessException(
                INVALID_PARAMS,
                "params.email.outOfLength"
            )
        }
        if (DataUtil.isNullOrEmpty(receiverDataDTO.userId) &&
            !ValidateUtils.validateRegex(receiverDataDTO.email, Regex.EMAIL_REGEX)
        ) {
            throw BusinessException(
                INVALID_PARAMS,
                "receiver.email.invalid"
            )
        }
    }

    private fun build(row: Row): NotificationHeader {
        return NotificationHeader.builder()
            .id(DataUtil.safeToString(row.get("id")))
            .title(DataUtil.safeToString(row.get("title")))
            .subTitle(DataUtil.safeToString(row.get("sub_title")))
            .imageUrl(DataUtil.safeToString(row.get("image_url")))
            .url(DataUtil.safeToString(row.get("url")))
            .status(DataUtil.safeToInt(row.get("status")))
            .createAt(row.get("create_at") as LocalDateTime)
            .createBy(DataUtil.safeToString(row.get("create_by")))
            .updateAt(row.get("update_at") as LocalDateTime)
            .updateBy(DataUtil.safeToString(row.get("update_by")))
            .state(DataUtil.safeToString(row.get("state")))
            .build()
    }
}
