package com.ezbuy.notiservice.service.impl;

import com.ezbuy.framework.constants.Regex;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.TokenUser;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.*;
import com.ezbuy.notimodel.common.ConstValue;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.request.NotiContentDTO;
import com.ezbuy.notimodel.dto.request.ReceiverDataDTO;
import com.ezbuy.notimodel.dto.response.CountNoticeDTO;
import com.ezbuy.notimodel.dto.response.CountNoticeResponseDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.Notification;
import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notimodel.model.Transmission;
import com.ezbuy.notiservice.repository.*;
import com.ezbuy.notiservice.client.AuthClient;
import com.ezbuy.notiservice.service.TransmissionService;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ezbuy.framework.constants.CommonErrorCode.*;
import static com.ezbuy.framework.constants.Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN;
import static com.ezbuy.notimodel.common.ConstValue.Channel.CHANNEL_REST;
import static com.ezbuy.notimodel.common.ConstValue.Channel.CHANNEL_SMS;
import static com.ezbuy.notimodel.common.ConstValue.Channel.CHANNEL_EMAIL;
import static com.ezbuy.notimodel.common.ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC;
import static com.ezbuy.notimodel.common.ConstValue.ContentTypeConstant.HTML;
import static com.ezbuy.notimodel.common.ConstValue.ContentTypeConstant.TEXT;
import static com.ezbuy.notimodel.common.ConstValue.NotificationConstant.ANNOUNCEMENT;
import static com.ezbuy.notimodel.common.ConstValue.TransmissionState.*;
import static com.ezbuy.notimodel.common.ConstValue.NotiServerity.*;
import static com.ezbuy.notimodel.common.ConstValue.NotificationConstant.*;

@Service
@RequiredArgsConstructor
public class TransmissionServiceImpl implements TransmissionService {

    private final TransmissionRepository transmissionRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final NotificationContentRepository notificationContentRepository;
    private final NotificationRepository notificationRepository;
    private final ChannelRepository channelRepository;
    private final AuthClient authClient;
    private final R2dbcEntityTemplate template;

    @Value("${config.resendCount}")
    private Integer resendCount;

//    @Override
//    public Mono<DataResponse> getCountNoticeResponseDTO() {
//        return SecurityUtils.getCurrentUser()
//                .flatMap(user -> transmissionRepository.getListCountNoticeDTO(user.getId()).collectList()
//                        .map(listCountNoticeDTO -> {
//                            CountNoticeResponseDTO countNoticeResponseDTO = new CountNoticeResponseDTO(0, new ArrayList<>());
//                            if (listCountNoticeDTO.isEmpty()) {
//                                return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
//                            }
//                            listCountNoticeDTO.forEach(
//                                    countNoticeDTO -> countNoticeResponseDTO.setTotal(countNoticeResponseDTO.getTotal() + countNoticeDTO.getQuantity())
//                            );
//                            countNoticeResponseDTO.setDetail(listCountNoticeDTO);
//                            return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
//                        })
//                );
//    }

    @Override
    public Mono<DataResponse> getCountNoticeResponseDTO() {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> transmissionRepository.getListCountNoticeDTO(user.getId())
                        .collect(Collectors.toMap(
                                CountNoticeDTO::getType,
                                CountNoticeDTO::getQuantity,
                                Integer::sum
                        ))
                        .map(totalMap -> {
                            int total = totalMap.values().stream().mapToInt(Integer::intValue).sum();
                            List<CountNoticeDTO> details = totalMap.entrySet().stream()
                                    .map(entry -> new CountNoticeDTO(entry.getKey(), entry.getValue()))
                                    .collect(Collectors.toList());
                            CountNoticeResponseDTO countNoticeResponseDTO = new CountNoticeResponseDTO(total, details);
                            return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
                        })
                        .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new CountNoticeResponseDTO(0, new ArrayList<>())))) // Xử lý khi không có dữ liệu
                );
    }

    @Override
    public Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId) {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> {
                    if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) && DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                        return Mono.error(new BusinessException(BAD_REQUEST, "params.notificationContentId.transmissionId.notnull"));
                    }
                    if (DataUtil.isNullOrEmpty(state)) {
                        return Mono.error(new BusinessException(BAD_REQUEST, "params.state.null"));
                    }
                    if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) && !DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                        if (!DataUtil.isUUID(transmissionId)) {
                            return Mono.error(new BusinessException(INVALID_PARAMS, "params.invalid.format"));
                        }
                        if (DataUtil.safeTrim(state).equals(READ) || DataUtil.safeTrim(state).equals(UNREAD) || DataUtil.safeTrim(state).equals(PENDING)
                                || DataUtil.safeTrim(state).equals(FAILED) || DataUtil.safeTrim(state).equals(NEW) || DataUtil.safeTrim(state).equals(SENT)) {
                            return transmissionRepository.findByIdAndStatus(transmissionId, ConstValue.Status.ACTIVE)
                                    .flatMap(
                                            transmission -> {
                                                transmission.setState(state);
                                                transmission.setUpdateBy("system");
                                                transmission.setUpdateAt(null);
                                                return transmissionRepository.updateTransmissionStateById(transmission.getId(), transmission.getState()).flatMap(
                                                        t -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null))
                                                ).switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
                                            }
                                    )
                                    .switchIfEmpty(Mono.error(new BusinessException(NOT_FOUND, "transmission.findById.not.found")));
                        }
                        return Mono.error(new BusinessException(INVALID_PARAMS, "params.state.invalid"));
                    }
                    if (!DataUtil.isUUID(notificationContentId)) {
                        return Mono.error(new BusinessException(INVALID_PARAMS, "params.invalid.format"));
                    }
                    if (DataUtil.safeTrim(state).equals(READ) || DataUtil.safeTrim(state).equals(UNREAD) || DataUtil.safeTrim(state).equals(PENDING)
                            || DataUtil.safeTrim(state).equals(FAILED) || DataUtil.safeTrim(state).equals(NEW) || DataUtil.safeTrim(state).equals(SENT)) {
                        return transmissionRepository.getListTransId(user.getId(), notificationContentId).collectList()
                                .flatMap(listId -> {
                                    if (DataUtil.isNullOrEmpty(listId)) {
                                        return Mono.error(new BusinessException(NOT_FOUND, "transmission.not.found"));
                                    }
                                    return transmissionRepository.changeStateTransmissionByNotiIdAndReceiver(DataUtil.safeTrim(state), DataUtil.safeTrim(user.getId()), DataUtil.safeTrim(notificationContentId))
                                            .then(Mono.defer(() -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null))))
                                            .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
                                });
                    } else {
                        return Mono.error(new BusinessException(INVALID_PARAMS, "params.state.invalid"));
                    }
                });
    }

    @Transactional
    @Override
    public Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO) {
        return validateCreateNotificationDTO(createNotificationDTO)
                .flatMap(validatedDTO -> SecurityUtils.getCurrentUser())
                .flatMap(tokenUser -> {
                    String notiContentId = UUID.randomUUID().toString();
                    String notiId = UUID.randomUUID().toString();
                    NotiContentDTO notiContentDTO = createNotificationDTO.getNotiContentDTO();

                    NotificationContent notificationContent = buildNotificationContent(notiContentId, notiContentDTO, createNotificationDTO.getTemplateMail(), tokenUser);
                    return notificationContentRepository.save(notificationContent)
                            .flatMap(savedContent -> handleNotificationCreation(createNotificationDTO, tokenUser, notiContentId, notiId))
                            .flatMap(notification -> handleTransmissionCreation(createNotificationDTO, tokenUser, notiId))
                            .flatMap(transmissions -> Mono.just(DataResponse.success(transmissions)));
                });
    }

    private NotificationContent buildNotificationContent(String notiContentId, NotiContentDTO notiContentDTO,  String templateMail, TokenUser tokenUser) {
        return NotificationContent.builder()
                .id(notiContentId.trim())
                .title(DataUtil.safeTrim(notiContentDTO.getTitle()))
                .subTitle(DataUtil.safeTrim(notiContentDTO.getSubTitle()))
                .imageUrl(DataUtil.safeTrim(notiContentDTO.getImageUrl()))
                .url(DataUtil.safeTrim(notiContentDTO.getUrl()))
                .createBy(tokenUser.getUsername())
                .updateBy(tokenUser.getUsername())
                .status(ConstValue.Status.ACTIVE)
                .templateMail(DataUtil.safeToString(templateMail))
                .externalData(notiContentDTO.getExternalData())
                .build();
    }

    private Mono<Notification> handleNotificationCreation(CreateNotificationDTO createNotificationDTO, TokenUser tokenUser, String notiContentId, String notiId) {
        return notificationCategoryRepository.findCategoryIdByType(DataUtil.safeTrim(createNotificationDTO.getCategoryType()))
                .switchIfEmpty(Mono.error(new BusinessException(INTERNAL_SERVER_ERROR, "category.not.found")))
                .flatMap(categoryId -> {
                    Notification notification = buildNotification(createNotificationDTO, tokenUser, notiContentId, notiId, categoryId);
                    return notificationRepository.save(notification);
                });
    }

    private Notification buildNotification(CreateNotificationDTO createNotificationDTO, TokenUser tokenUser, String notiContentId, String notiId, String categoryId) {
        return Notification.builder()
                .id(notiId.trim())
                .contentType(DataUtil.safeTrim(createNotificationDTO.getContentType()))
                .createBy(tokenUser.getUsername())
                .updateBy(tokenUser.getUsername())
                .expectSendTime(createNotificationDTO.getExpectSendTime())
                .categoryId(categoryId)
                .notificationContentId(notiContentId)
                .sender(DataUtil.safeTrim(createNotificationDTO.getSender()))
                .severity(DataUtil.safeTrim(createNotificationDTO.getSeverity()))
                .status(ConstValue.Status.ACTIVE)
                .build();
    }

    private Mono<List<Transmission>> handleTransmissionCreation(CreateNotificationDTO createNotificationDTO, TokenUser tokenUser, String notiId) {
        if (DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
            return Mono.error(new BusinessException(NOT_FOUND, Translator.toLocaleVi("no.receiver")));
        }
        boolean invalidReceiver = createNotificationDTO.getReceiverList().stream()
                .anyMatch(receiver -> (!DataUtil.isUUID(DataUtil.safeTrim(receiver.getUserId())) && DataUtil.isNullOrEmpty(receiver.getEmail())));
        if (invalidReceiver) {
            return Mono.error(new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("receiver.string.invalid")));
        }
        return channelRepository.findChannelIdByType(DataUtil.safeTrim(createNotificationDTO.getChannelType()))
                .switchIfEmpty(Mono.error(new BusinessException(INTERNAL_SERVER_ERROR, "params.channelId.notExist")))
                .flatMap(channelId -> {
                    List<Transmission> transmissionList = createTransmissionList(createNotificationDTO, tokenUser, notiId, channelId);
                    if (DataUtil.isNullOrEmpty(transmissionList)) {
                        return Mono.error(new BusinessException(INVALID_PARAMS, "no.receiver"));
                    }
                    return transmissionRepository.saveAll(transmissionList).collectList();
                });
    }

    private List<Transmission> createTransmissionList(CreateNotificationDTO createNotificationDTO, TokenUser tokenUser, String notiId, String channelId) {
        return createNotificationDTO.getReceiverList().stream().distinct()
                .map(receiver -> buildTransmission(receiver, tokenUser, notiId, channelId)).collect(Collectors.toList());
    }

    private Transmission buildTransmission(ReceiverDataDTO receiver, TokenUser tokenUser, String notiId, String channelId) {
        Transmission transmission = Transmission.builder()
                .id(UUID.randomUUID().toString())
                .notificationId(notiId)
                .channelId(channelId)
                .status(ConstValue.Status.ACTIVE)
                .state(PENDING)
                .resendCount(0)
                .updateBy(tokenUser.getUsername())
                .createBy(tokenUser.getUsername())
                .build();

        if (DataUtil.isUUID(receiver.getUserId().trim())) {
            transmission.setReceiver(receiver.getUserId().trim());
            transmission.setEmail(DataUtil.safeTrim(receiver.getEmail()));
        } else if (ValidateUtils.validateRegex(receiver.getEmail(), Regex.EMAIL_REGEX)) {
            transmission.setEmail(DataUtil.safeTrim(receiver.getEmail()));
        }

        return transmission;
    }

    @Override
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime) {
        if (DataUtil.isNullOrEmpty(newestNotiTime)) {
            return Mono.error(new BusinessException(INVALID_PARAMS, "params.newestNotiTime.notnull"));
        }
        if (DataUtil.isNullOrEmpty(DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))) {
            return Mono.error(new BusinessException(INVALID_PARAMS, "params.invalid.format"));
        }
        return SecurityUtils.getCurrentUser().flatMap(
                tokenUser -> transmissionRepository.getAllNotificationContentByCreateAtAfter(tokenUser.getId(), DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))
                        .collectList()
                        .flatMap(
                                listNotiContent -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), listNotiContent))
                        ).switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new ArrayList<>())))
        );
    }

    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String categoryType, Integer pageIndex, Integer pageSize, String sort) {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> {
                    if (pageIndex < 1) {
                        return Mono.error(new BusinessException(INVALID_PARAMS, "params.pageIndex.invalid"));
                    }
                    if (pageSize < 1) {
                        return Mono.error(new BusinessException(INVALID_PARAMS, "params.pageSize.invalid"));
                    }
                    StringBuilder sortingString = new StringBuilder();
                    sortingString.append(SortingUtils.parseSorting(sort, NotificationHeader.class));
                    if (DataUtil.isNullOrEmpty(sortingString)) {
                        sortingString.append("");
                    }
                    StringBuilder query = new StringBuilder();
                    query.append(" SELECT nc.*,tr.state  \n" +
                            "FROM notification_content nc \n" +
                            "INNER JOIN notification n \n" +
                            "ON n.notification_content_id = nc.id \n" +
                            "INNER JOIN notification_category nca \n" +
                            "ON n.category_id = nca.id \n" +
                            "INNER JOIN transmission tr \n" +
                            "ON tr.notification_id = n.id \n" +
                            "INNER JOIN channel c \n" +
                            "ON tr.channel_id = c.id \n" +
                            "where tr.receiver = (:receiver)   \n" +
                            "AND tr.status =1 \n" +
                            "AND tr.state IN ('NEW','UNREAD','READ') \n" +
                            "AND nc.status =1 \n" +
                            "AND n.status =1 \n" +
                            "AND nca.status =1 \n" +
                            "AND c.status =1 \n" +
                            "AND c.type = 'REST' \n" +
                            "AND nca.type = (:categoryType)  \n" +
                            "ORDER BY ");
                    query.append(sortingString);
                    query.append("LIMIT :pageSize  \n" +
                            "OFFSET :index;");
                    BigDecimal index = (new BigDecimal(pageIndex - 1)).multiply(new BigDecimal(pageSize));
                    return template.getDatabaseClient().sql(String.valueOf(query))
                            .bind("receiver", user.getId())
                            .bind("categoryType", DataUtil.safeTrim(categoryType))
                            .bind("pageSize", pageSize)
                            .bind("index", index)
                            .map(c -> this.build((Row) c))
                            .all()
                            .collectList()
                            .flatMap(notificationContent -> Mono.just(new DataResponse<>(null, SUCCESS, notificationContent)))
                            .switchIfEmpty(Mono.just(new DataResponse<>(null, SUCCESS, new ArrayList<>())));
                });
    }

    public Mono<DataResponse<Object>> validateCreateNotificationDTO(CreateNotificationDTO createNotificationDTO) {
        if (DataUtil.isNullOrEmpty(createNotificationDTO.getSeverity())) {
            createNotificationDTO.setSeverity(NORMAL);
        }
        // NORMAL or CRITICAL
        if (!DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(NORMAL) && !DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(CRITICAL)) {
            return Mono.error(new BusinessException(INVALID_PARAMS, INVALID_FORMAT_SPEC, "Severity"));
        }
        // ANNOUNCEMENT or NEWS
        if (!DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(ANNOUNCEMENT) && !DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(NEWS)) {
            return Mono.error(new BusinessException(INVALID_PARAMS, INVALID_FORMAT_SPEC, "CategoryType"));
        }
        // text/plain or html/plain
        if (!DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(TEXT) && !DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(HTML)) {
            return Mono.error(new BusinessException(INVALID_PARAMS, INVALID_FORMAT_SPEC, "ContentType"));
        }
        // SMS or EMAIL or REST
        if (!DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(CHANNEL_SMS) && !DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(CHANNEL_EMAIL) && !createNotificationDTO.getChannelType().equals(CHANNEL_REST)) {
            return Mono.error(new BusinessException(INVALID_PARAMS, INVALID_FORMAT_SPEC, "ChannelType"));
        }
        // validate: NotiContentDTO
        notiContentDTOIsValid(createNotificationDTO.getNotiContentDTO());
        if (DataUtil.isNullOrEmpty(createNotificationDTO.getSendAll())) {
            createNotificationDTO.setSendAll(false);
        }
        if (DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
            createNotificationDTO.setReceiverList(new ArrayList<>());
        } else {
            createNotificationDTO.getReceiverList().forEach(this::isReceiverDataDTOValid);
        }
        if (Boolean.TRUE.equals(createNotificationDTO.getSendAll())) {
            return authClient.getAllUserId().flatMap(list -> {
                        if (DataUtil.isNullOrEmpty(list)) {
                            return Mono.error(new BusinessException(NOT_FOUND, "no.receiver"));
                        }
                        List<ReceiverDataDTO> receiverDataDTOList = createNotificationDTO.getReceiverList();
                        receiverDataDTOList.addAll(list.stream().map(x ->
                                new ReceiverDataDTO(x, null)
                        ).toList());
                        createNotificationDTO.setReceiverList(receiverDataDTOList);
                        return Mono.just(new DataResponse<>(null, SUCCESS, createNotificationDTO));
                    }
            );
        }
        if (!DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
            createNotificationDTO.getReceiverList().forEach(this::isReceiverDataDTOValid);
            return Mono.just(new DataResponse<>(null, SUCCESS, createNotificationDTO));
        }
        return Mono.error(new BusinessException(NOT_FOUND, "no.receiver"));
    }

    private void notiContentDTOIsValid(NotiContentDTO notiContentDTO) {
        if (DataUtil.isNullOrEmpty(notiContentDTO)) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.object.null", NotiContentDTO.builder().build().getClass().getSimpleName()));
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getTitle())) {
            throw new BusinessException(INVALID_PARAMS, "params.title.null");
        } else if (notiContentDTO.getTitle().length() > 500) {
            throw new BusinessException(INVALID_PARAMS, "params.title.outOfLength");
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getSubTitle())) {
            throw new BusinessException(INVALID_PARAMS, "params.subTitle.null");
        } else if (notiContentDTO.getSubTitle().length() > 5000) {
            throw new BusinessException(INVALID_PARAMS, "params.subTitle.outOfLength");
        }
        if (DataUtil.safeTrim(notiContentDTO.getImageUrl()).length() > 500 && !DataUtil.isNullOrEmpty(notiContentDTO.getImageUrl())) {
            throw new BusinessException(INVALID_PARAMS, "params.imageUrl.outOfLength");
        }
        if (DataUtil.safeTrim(notiContentDTO.getUrl()).length() > 300 && !DataUtil.isNullOrEmpty(notiContentDTO.getUrl())) {
            throw new BusinessException(INVALID_PARAMS, "params.url.outOfLength");
        }
    }

    private void isReceiverDataDTOValid(ReceiverDataDTO receiverDataDTO) {
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && DataUtil.isNullOrEmpty(receiverDataDTO.getEmail())) {
            throw new BusinessException(INVALID_PARAMS, "params.receiverDataDTO.notAllNull");
        }
        if (!DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && !DataUtil.isUUID(receiverDataDTO.getUserId()) && (DataUtil.isNullOrEmpty(receiverDataDTO.getEmail()) || !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX))) {
            throw new BusinessException(INVALID_PARAMS, "receiver.string.invalid");
        }
        if (DataUtil.safeTrim(receiverDataDTO.getEmail()).length() > 200) {
            throw new BusinessException(INVALID_PARAMS, "params.email.outOfLength");
        }
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX)) {
            throw new BusinessException(INVALID_PARAMS, "receiver.email.invalid");
        }
    }

    private NotificationHeader build(Row row) {
        return NotificationHeader.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .title(DataUtil.safeToString(row.get("title")))
                .subTitle(DataUtil.safeToString(row.get("sub_title")))
                .imageUrl(DataUtil.safeToString(row.get("image_url")))
                .url(DataUtil.safeToString(row.get("url")))
                .status(DataUtil.safeToInt(row.get("status")))
                .createAt((LocalDateTime) row.get("create_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .updateAt((LocalDateTime) row.get("update_at"))
                .updateBy(DataUtil.safeToString(row.get("update_by")))
                .state(DataUtil.safeToString(row.get("state")))
                .build();
    }
}
