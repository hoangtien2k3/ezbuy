package com.ezbuy.notification.service.impl;

import static com.ezbuy.framework.constants.CommonErrorCode.*;
import static com.ezbuy.framework.constants.Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN;
import static com.ezbuy.notification.common.ConstValue.Channel.*;
import static com.ezbuy.notification.common.ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC;
import static com.ezbuy.notification.common.ConstValue.ContentTypeConstant.HTML;
import static com.ezbuy.notification.common.ConstValue.ContentTypeConstant.TEXT;
import static com.ezbuy.notification.common.ConstValue.NotiServerity.CRITICAL;
import static com.ezbuy.notification.common.ConstValue.NotiServerity.NORMAL;
import static com.ezbuy.notification.common.ConstValue.NotificationConstant.THONG_BAO;
import static com.ezbuy.notification.common.ConstValue.NotificationConstant.TIN_TUC;
import static com.ezbuy.notification.common.ConstValue.TransmissionState.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.constants.Regex;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.*;
import com.ezbuy.notification.client.AuthClient;
import com.ezbuy.notification.dto.request.CreateNotificationDTO;
import com.ezbuy.notification.dto.request.NotiContentDTO;
import com.ezbuy.notification.dto.request.ReceiverDataDTO;
import com.ezbuy.notification.dto.response.CountNoticeResponseDTO;
import com.ezbuy.notification.dto.response.NotificationHeader;
import com.ezbuy.notification.model.Notification;
import com.ezbuy.notification.model.NotificationContent;
import com.ezbuy.notification.model.Transmission;
import com.ezbuy.notification.repository.*;
import com.ezbuy.notification.service.TransmissionService;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransmissionServiceImpl implements TransmissionService {

    private final TransmissionRepository transmissionRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final NotificationContentRepository notificationContentRepository;
    private final NotificationRepository notificationRepository;
    private final ChannelRepository channelRepository;
    private final AuthClient authClient;
//    private final R2dbcEntityTemplate template;

    @Value("${config.resendCount}")
    private Integer resendCount;

    @Override
    public Mono<DataResponse> getCountNoticeResponseDTO() {
        return SecurityUtils.getCurrentUser().flatMap(user -> transmissionRepository
                .getListCountNoticeDTO(user.getId())
                .collectList()
                .map(listCountNoticeDTO -> {
                    CountNoticeResponseDTO countNoticeResponseDTO = new CountNoticeResponseDTO(0, new ArrayList<>());
                    if (listCountNoticeDTO.isEmpty()) {
                        return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
                    }
                    listCountNoticeDTO.forEach(countNoticeDTO -> countNoticeResponseDTO.setTotal(
                            countNoticeResponseDTO.getTotal() + countNoticeDTO.getQuantity()));
                    countNoticeResponseDTO.setDetail(listCountNoticeDTO);
                    return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
                }));
    }

    @Override
    public Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(
            String state, String notificationContentId, String transmissionId) {
        return SecurityUtils.getCurrentUser().flatMap(user -> {
            if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId))
                    && DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                return Mono.error(new BusinessException(
                        BAD_REQUEST, Translator.toLocaleVi("params.notificationContentId.transmissionId.notnull")));
            }
            if (DataUtil.isNullOrEmpty(state)) {
                return Mono.error(new BusinessException(BAD_REQUEST, Translator.toLocaleVi("params.state.null")));
            }
            if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId))
                    && !DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                if (!DataUtil.isUUID(transmissionId)) {
                    return Mono.error(
                            new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.invalid.format")));
                }
                if (DataUtil.safeTrim(state).equals(READ)
                        || DataUtil.safeTrim(state).equals(UNREAD)
                        || DataUtil.safeTrim(state).equals(PENDING)
                        || DataUtil.safeTrim(state).equals(FAILED)
                        || DataUtil.safeTrim(state).equals(NEW)
                        || DataUtil.safeTrim(state).equals(SENT)) {
                    return transmissionRepository
                            .findByIdAndStatus(transmissionId, 1)
                            .flatMap(transmission -> {
                                transmission.setState(state);
                                transmission.setUpdateBy("system");
                                transmission.setUpdateAt(null);
                                return transmissionRepository
                                        .updateTransmissionStateById(transmission.getId(), transmission.getState())
                                        .flatMap(t -> Mono.just(
                                                new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)))
                                        .switchIfEmpty(Mono.just(
                                                new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
                            })
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    NOT_FOUND, Translator.toLocaleVi("transmission.findById.not.found"))));
                }
                return Mono.error(new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.state.invalid")));
            }
            if (!DataUtil.isUUID(notificationContentId)) {
                return Mono.error(
                        new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.invalid.format")));
            }
            if (DataUtil.safeTrim(state).equals(READ)
                    || DataUtil.safeTrim(state).equals(UNREAD)
                    || DataUtil.safeTrim(state).equals(PENDING)
                    || DataUtil.safeTrim(state).equals(FAILED)
                    || DataUtil.safeTrim(state).equals(NEW)
                    || DataUtil.safeTrim(state).equals(SENT)) {
                return transmissionRepository
                        .getListTransId(user.getId(), notificationContentId)
                        .collectList()
                        .flatMap(listId -> {
                            if (DataUtil.isNullOrEmpty(listId)) {
                                return Mono.error(new BusinessException(
                                        NOT_FOUND, Translator.toLocaleVi("transmission.not.found")));
                            }
                            return transmissionRepository
                                    .changeStateTransmissionByNotiIdAndReceiver(
                                            DataUtil.safeTrim(state),
                                            DataUtil.safeTrim(user.getId()),
                                            DataUtil.safeTrim(notificationContentId))
                                    .flatMap(data ->
                                            Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)))
                                    .switchIfEmpty(
                                            Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
                        });
            } else {
                return Mono.error(new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.state.invalid")));
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
                    return notificationContentRepository
                            .save(NotificationContent.builder()
                                    .id(DataUtil.safeTrim(notiContentId))
                                    .title(DataUtil.safeTrim(notiContentDTO.getTitle()))
                                    .subTitle(DataUtil.safeTrim(notiContentDTO.getSubTitle()))
                                    .imageUrl(DataUtil.safeTrim(notiContentDTO.getImageUrl()))
                                    .url(DataUtil.safeTrim(notiContentDTO.getUrl()))
                                    .createBy(tokenUser.getUsername())
                                    .updateBy(tokenUser.getUsername())
                                    .status(1)
                                    .templateMail(DataUtil.safeToString(createNotificationDTO.getTemplateMail()))
                                    .externalData(notiContentDTO.getExternalData())
                                    .build())
                            .flatMap(data -> notificationCategoryRepository
                                    .findCategoryIdByType(DataUtil.safeTrim(createNotificationDTO.getCategoryType()))
                                    .flatMap(categoryId -> notificationRepository
                                            .save(Notification.builder()
                                                    .id(DataUtil.safeTrim(notiId))
                                                    .contentType(
                                                            DataUtil.safeTrim(createNotificationDTO.getContentType()))
                                                    .createBy(DataUtil.safeTrim(tokenUser.getUsername()))
                                                    .updateBy(DataUtil.safeTrim(tokenUser.getUsername()))
                                                    .expectSendTime(createNotificationDTO.getExpectSendTime())
                                                    .categoryId(DataUtil.safeTrim(categoryId))
                                                    .notificationContentId(DataUtil.safeTrim(notiContentId))
                                                    .sender(DataUtil.safeTrim(createNotificationDTO.getSender()))
                                                    .severity(DataUtil.safeTrim(createNotificationDTO.getSeverity()))
                                                    .status(1)
                                                    .build())
                                            .switchIfEmpty(Mono.error(
                                                    new BusinessException(INTERNAL_SERVER_ERROR, "category.not.found")))
                                            .flatMap(data2 -> {
                                                if (DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
                                                    return Mono.error(new BusinessException(
                                                            NOT_FOUND, Translator.toLocaleVi("no.receiver")));
                                                }

                                                // check invalid UUID receiver
                                                boolean invalidReceiver =
                                                        createNotificationDTO.getReceiverList().stream()
                                                                .anyMatch(receiver -> (!DataUtil.isUUID(
                                                                                DataUtil.safeTrim(receiver.getUserId()))
                                                                        && DataUtil.isNullOrEmpty(
                                                                                receiver.getEmail())));
                                                if (invalidReceiver) {
                                                    return Mono.error(new BusinessException(
                                                            INVALID_PARAMS,
                                                            Translator.toLocaleVi("receiver.string.invalid")));
                                                }
                                                return channelRepository.findChannelIdByType(
                                                        DataUtil.safeTrim(createNotificationDTO.getChannelType()));
                                            })
                                            .switchIfEmpty(Mono.error(new BusinessException(
                                                    INTERNAL_SERVER_ERROR, "params.channelId.notExist")))
                                            .flatMap(channelId -> {
                                                List<Transmission> transmissionList =
                                                        createNotificationDTO.getReceiverList().stream()
                                                                .distinct()
                                                                .map(receiver -> {
                                                                    Transmission tr = Transmission.builder()
                                                                            .id(UUID.randomUUID().toString())
                                                                            .notificationId(notiId)
                                                                            .channelId(channelId)
                                                                            .status(1)
                                                                            .state(PENDING)
                                                                            .resendCount(0)
                                                                            .updateBy(tokenUser.getUsername())
                                                                            .createBy(tokenUser.getUsername())
                                                                            .build();
                                                                    if (DataUtil.isUUID(DataUtil.safeTrim(receiver.getUserId()))) {
                                                                        tr.setReceiver(DataUtil.safeTrim(receiver.getUserId()));
                                                                        tr.setEmail(DataUtil.safeTrim(receiver.getEmail()));
                                                                    } else if (ValidateUtils.validateRegex(receiver.getEmail(), Regex.EMAIL_REGEX)) {
                                                                        tr.setEmail(DataUtil.safeTrim(receiver.getEmail()));
                                                                    }
                                                                    return tr;
                                                                })
                                                                .collect(Collectors.toList());
                                                if (transmissionList.isEmpty()) {
                                                    return Mono.error(
                                                            new BusinessException(INVALID_PARAMS, "no.receiver"));
                                                }
                                                return transmissionRepository
                                                        .saveAll(transmissionList)
                                                        .collectList();
                                            })
                                            .switchIfEmpty(Mono.error(new BusinessException(
                                                    INTERNAL_SERVER_ERROR, "resource.server.error")))
                                            .flatMap(result -> Mono.just(new DataResponse<>(
                                                    null, Translator.toLocaleVi(SUCCESS), result)))));
                });
    }

    @Override
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime) {
        if (DataUtil.isNullOrEmpty(newestNotiTime)) {
            return Mono.error(
                    new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.newestNotiTime.notnull")));
        }
        if (DataUtil.isNullOrEmpty(DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))) {
            return Mono.error(new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.invalid.format")));
        }
        return SecurityUtils.getCurrentUser().flatMap(tokenUser -> transmissionRepository
                .getAllNotificationContentByCreateAtAfter(
                        tokenUser.getId(),
                        DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))
                .collectList()
                .flatMap(listNotiContent ->
                        Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), listNotiContent)))
                .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new ArrayList<>()))));
    }

//    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(
//            String categoryType, Integer pageIndex, Integer pageSize, String sort) {
//        return SecurityUtils.getCurrentUser().flatMap(user -> {
//            if (pageIndex < 1) {
//                return Mono.error(
//                        new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.pageIndex.invalid")));
//            }
//            if (pageSize < 1) {
//                return Mono.error(
//                        new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.pageSize.invalid")));
//            }
//            StringBuilder sortingString = new StringBuilder();
//            sortingString.append(SortingUtils.parseSorting(sort, NotificationHeader.class));
//            if (DataUtil.isNullOrEmpty(sortingString)) {
//                sortingString.append("");
//            }
//            StringBuilder query = new StringBuilder();
//            query.append(" SELECT nc.*,tr.state  \n"
//                    + " FROM notification_content nc \n"
//                    + " INNER JOIN notification n \n"
//                    + " ON n.notification_content_id = nc.id \n"
//                    + " INNER JOIN notification_category nca \n"
//                    + " ON n.category_id = nca.id \n"
//                    + " INNER JOIN transmission tr \n"
//                    + " ON tr.notification_id = n.id \n"
//                    + " INNER JOIN channel c \n"
//                    + " ON tr.channel_id = c.id \n"
//                    + " where tr.receiver = (:receiver)   \n"
//                    + " AND tr.status =1 \n"
//                    + " AND tr.state IN ('NEW','UNREAD','READ') \n"
//                    + " AND nc.status =1 \n"
//                    + " AND n.status =1 \n"
//                    + " AND nca.status =1 \n"
//                    + " AND c.status =1 \n"
//                    + " AND c.type = 'REST' \n"
//                    + " AND nca.type = (:categoryType)  \n"
//                    + " ORDER BY ");
//            query.append(sortingString);
//            query.append(" LIMIT :pageSize  \n" + " OFFSET :index;");
//            BigDecimal index = (new BigDecimal(pageIndex - 1)).multiply(new BigDecimal(pageSize));
//            return template.getDatabaseClient()
//                    .sql(String.valueOf(query))
//                    .bind("receiver", user.getId())
//                    .bind("categoryType", DataUtil.safeTrim(categoryType))
//                    .bind("pageSize", pageSize)
//                    .bind("index", index)
//                    .map(row -> build((Row) row))
//                    .all()
//                    .collectList()
//                    .flatMap(notificationContent ->
//                            Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), notificationContent)))
//                    .switchIfEmpty(
//                            Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new ArrayList<>())));
//        });
//    }

    public Mono<DataResponse<Object>> validateCreateNotificationDTO(CreateNotificationDTO createNotificationDTO) {
        if (DataUtil.isNullOrEmpty(createNotificationDTO.getSeverity())) {
            createNotificationDTO.setSeverity(NORMAL);
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(NORMAL)
                && !DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(CRITICAL)) {
            return Mono.error(
                    new BusinessException(INVALID_PARAMS, Translator.toLocaleVi(INVALID_FORMAT_SPEC, "severity")));
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(THONG_BAO)
                && !DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(TIN_TUC)) {
            return Mono.error(
                    new BusinessException(INVALID_PARAMS, Translator.toLocaleVi(INVALID_FORMAT_SPEC, "CategoryType")));
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(TEXT)
                && !DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(HTML)) {
            return Mono.error(
                    new BusinessException(INVALID_PARAMS, Translator.toLocaleVi(INVALID_FORMAT_SPEC, "ContentType")));
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(CHANNEL_SMS)
                && !DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(CHANNEL_EMAIL)
                && !createNotificationDTO.getChannelType().equals(CHANNEL_REST)) {
            return Mono.error(
                    new BusinessException(INVALID_PARAMS, Translator.toLocaleVi(INVALID_FORMAT_SPEC, "ChannelType")));
        }
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
                    return Mono.error(
                            new BusinessException(CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("no.receiver")));
                }
                List<ReceiverDataDTO> receiverDataDTOList = createNotificationDTO.getReceiverList();
                receiverDataDTOList.addAll(
                        list.stream().map(x -> new ReceiverDataDTO(x, null)).toList());
                createNotificationDTO.setReceiverList(receiverDataDTOList);
                return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), createNotificationDTO));
            });
        }
        if (!DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
            createNotificationDTO.getReceiverList().forEach(this::isReceiverDataDTOValid);
            return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), createNotificationDTO));
        }
        return Mono.error(new BusinessException(NOT_FOUND, Translator.toLocaleVi("no.receiver")));
    }

    private void notiContentDTOIsValid(NotiContentDTO notiContentDTO) {
        if (DataUtil.isNullOrEmpty(notiContentDTO)) {
            throw new BusinessException(
                    INVALID_PARAMS,
                    Translator.toLocaleVi(
                            "params.object.null",
                            NotiContentDTO.builder().build().getClass().getSimpleName()));
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getTitle())) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.title.null"));
        } else if (notiContentDTO.getTitle().length() > 500) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.title.outOfLength"));
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getSubTitle())) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.subTitle.null"));
        } else if (notiContentDTO.getSubTitle().length() > 5000) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.subTitle.outOfLength"));
        }
        if (DataUtil.safeTrim(notiContentDTO.getImageUrl()).length() > 500
                && !DataUtil.isNullOrEmpty(notiContentDTO.getImageUrl())) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.imageUrl.outOfLength"));
        }
        if (DataUtil.safeTrim(notiContentDTO.getUrl()).length() > 300
                && !DataUtil.isNullOrEmpty(notiContentDTO.getUrl())) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.url.outOfLength"));
        }
    }

    private void isReceiverDataDTOValid(ReceiverDataDTO receiverDataDTO) {
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && DataUtil.isNullOrEmpty(receiverDataDTO.getEmail())) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.receiverDataDTO.notAllNull"));
        }
        if (!DataUtil.isNullOrEmpty(receiverDataDTO.getUserId())
                && !DataUtil.isUUID(receiverDataDTO.getUserId())
                && (DataUtil.isNullOrEmpty(receiverDataDTO.getEmail())
                        || !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX))) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("receiver.string.invalid"));
        }
        if (DataUtil.safeTrim(receiverDataDTO.getEmail()).length() > 200) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("params.email.outOfLength"));
        }
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId())
                && !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX)) {
            throw new BusinessException(INVALID_PARAMS, Translator.toLocaleVi("receiver.email.invalid"));
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
