package com.ezbuy.notiservice.service.impl;

import com.ezbuy.notimodel.common.ConstValue;
import com.ezbuy.notimodel.dto.UserTransmissionDTO;
import com.ezbuy.notimodel.dto.UserTransmissionPageDTO;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.request.GetTransmissionByEmailAndFromToRequest;
import com.ezbuy.notimodel.dto.request.NotiContentDTO;
import com.ezbuy.notimodel.dto.request.ReceiverDataDTO;
import com.ezbuy.notimodel.dto.response.CountNoticeResponseDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.Notification;
import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notimodel.model.Transmission;
import com.ezbuy.notiservice.client.AuthClient;
import com.ezbuy.notiservice.repoTemplate.TransmissionRepoTemplate;
import com.ezbuy.notiservice.repository.*;
import com.ezbuy.notiservice.service.TransmissionService;
import com.reactify.constants.Regex;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.*;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
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

import static com.reactify.constants.CommonConstant.FORMAT_DATE_DMY_HYPHEN;
import static com.reactify.constants.Constants.DateTimePattern.LOCAL_DATE_TIME_PATTERN;

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
    private final TransmissionRepoTemplate transmissionRepoTemplate;

    private final String SUCCESS = "success";

    @Override
    public Mono<DataResponse<CountNoticeResponseDTO>> getCountNoticeResponseDTO() {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> transmissionRepository.getListCountNoticeDTO(user.getId()).collectList()
                        .map(listCountNoticeDTO -> {
                            CountNoticeResponseDTO countNoticeResponseDTO = new CountNoticeResponseDTO(0, new ArrayList<>());
                            if (listCountNoticeDTO.isEmpty()) {
                                return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
                            }
                            listCountNoticeDTO.forEach(countNoticeDTO ->
                                    countNoticeResponseDTO.setTotal(countNoticeResponseDTO.getTotal() + countNoticeDTO.getQuantity())
                            );
                            countNoticeResponseDTO.setDetail(listCountNoticeDTO);
                            return new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), countNoticeResponseDTO);
                        })
                );
    }

    @Override
    public Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId) {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> {
                    if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) && DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                        return Mono.error(new BusinessException("CTSBIAR00001", Translator.toLocaleVi("params.notificationContentId.transmissionId.notnull")));
                    }
                    if (DataUtil.isNullOrEmpty(state)) {
                        return Mono.error(new BusinessException("CTSBIAR00002", Translator.toLocaleVi("params.state.null")));
                    }
                    if (DataUtil.isNullOrEmpty(DataUtil.safeTrim(notificationContentId)) && !DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionId))) {
                        if (!DataUtil.isUUID(transmissionId)) {
                            return Mono.error(new BusinessException("CTSBIAR00003", Translator.toLocaleVi("params.invalid.format")));
                        }
                        if (DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.READ) || DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.UNREAD) || DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.PENDING)
                                || DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.FAILED) || DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.NEW) || DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.SENT)) {
                            return transmissionRepository.findByIdAndStatus(transmissionId, 1)
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
                                    .switchIfEmpty(Mono.error(new BusinessException("CTSBIAR00004", Translator.toLocaleVi("transmission.findById.not.found"))));
                        }
                        return Mono.error(new BusinessException("CTSBIAR00005", Translator.toLocaleVi("params.state.invalid")));
                    }
                    if (!DataUtil.isUUID(notificationContentId)) {
                        return Mono.error(new BusinessException("CTSBIAR00006", Translator.toLocaleVi("params.invalid.format")));
                    }
                    if (DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.READ) ||
                            DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.UNREAD) ||
                            DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.PENDING) ||
                            DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.FAILED) ||
                            DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.NEW) ||
                            DataUtil.safeTrim(state).equals(ConstValue.TransmissionState.SENT)) {
                        return transmissionRepository.getListTransId(user.getId(), notificationContentId).collectList()
                                .flatMap(listId -> {
                                    if (DataUtil.isNullOrEmpty(listId)) {
                                        return Mono.error(new BusinessException("CTSBIAR00007", Translator.toLocaleVi("transmission.not.found")));
                                    }
                                    return transmissionRepository.changeStateTransmissionByNotiIdAndReceiver(DataUtil.safeTrim(state), DataUtil.safeTrim(user.getId()), DataUtil.safeTrim(notificationContentId))
                                            .then(Mono.defer(() -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null))))
                                            .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), null)));
                                });
                    } else {
                        return Mono.error(new BusinessException("CTSBIAR00008", Translator.toLocaleVi("params.state.invalid")));
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
                    return notificationContentRepository.save(NotificationContent.builder()
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
                            .build()
                    ).flatMap(data -> notificationCategoryRepository.findCategoryIdByType(DataUtil.safeTrim(createNotificationDTO.getCategoryType()))
                            .flatMap(categoryId -> notificationRepository.save(
                                            Notification.builder()
                                                    .id(DataUtil.safeTrim(notiId))
                                                    .contentType(DataUtil.safeTrim(createNotificationDTO.getContentType()))
                                                    .createBy(DataUtil.safeTrim(tokenUser.getUsername()))
                                                    .updateBy(DataUtil.safeTrim(tokenUser.getUsername()))
                                                    .expectSendTime(createNotificationDTO.getExpectSendTime())
                                                    .categoryId(DataUtil.safeTrim(categoryId))
                                                    .notificationContentId(DataUtil.safeTrim(notiContentId))
                                                    .sender(DataUtil.safeTrim(createNotificationDTO.getSender()))
                                                    .severity(DataUtil.safeTrim(createNotificationDTO.getSeverity()))
                                                    .status(1)
                                                    .build())
                                    .switchIfEmpty(Mono.error(new BusinessException("IT00001", "category.not.found")))
                                    .flatMap(data2 -> {
                                                if (DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
                                                    return Mono.error(new BusinessException("IT00002", Translator.toLocaleVi("no.receiver")));
                                                }

                                                // check invalid UUID receiver
                                                boolean invalidReceiver = createNotificationDTO.getReceiverList().stream()
                                                        .anyMatch(receiver -> (!DataUtil.isUUID(DataUtil.safeTrim(receiver.getUserId())) && DataUtil.isNullOrEmpty(receiver.getEmail())));
                                                if (invalidReceiver) {
                                                    return Mono.error(new BusinessException("IT00003", Translator.toLocaleVi("receiver.string.invalid")));
                                                }
                                                return channelRepository.findChannelIdByType(DataUtil.safeTrim(createNotificationDTO.getChannelType()));
                                            }
                                    ).switchIfEmpty(Mono.error(new BusinessException("IT00004", "params.channelId.notExist")))
                                    .flatMap(channelId -> {
                                        List<Transmission> transmissionList = createNotificationDTO.getReceiverList().stream().distinct()
                                                .map(receiver -> {
                                                            Transmission tr = Transmission.builder().id(UUID.randomUUID().toString())
                                                                    .notificationId(notiId)
                                                                    .channelId(channelId)
                                                                    .status(1)
                                                                    .state(ConstValue.TransmissionState.PENDING)
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
                                                        }
                                                ).collect(Collectors.toList());
                                        if (transmissionList.isEmpty()) {
                                            return Mono.error(new BusinessException("IT00005", "no.receiver"));
                                        }
                                        return transmissionRepository.saveAll(transmissionList).collectList();
                                    })
                                    .switchIfEmpty(Mono.error(new BusinessException("IT00006", "resource.server.error")))
                                    .flatMap(result ->
                                            Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), result))))
                    );
                });
    }

    @Override
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime) {
        if (DataUtil.isNullOrEmpty(newestNotiTime)) {
            return Mono.error(new BusinessException("GNNWO00001", Translator.toLocaleVi("params.newestNotiTime.notnull")));
        }
        if (DataUtil.isNullOrEmpty(DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))) {
            return Mono.error(new BusinessException("GNNWO00002", Translator.toLocaleVi("params.invalid.format")));
        }
        return SecurityUtils.getCurrentUser().flatMap(
                tokenUser -> transmissionRepository.getAllNotificationContentByCreateAtAfter(tokenUser.getId(), DataUtil.convertStringToLocalDateTime(newestNotiTime, LOCAL_DATE_TIME_PATTERN))
                        .collectList()
                        .flatMap(
                                listNotiContent -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), listNotiContent))
                        ).switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new ArrayList<>())))
        );
    }

    @Override
    public Mono<DataResponse<UserTransmissionPageDTO>> getTransmissionByEmailAndFromTo(GetTransmissionByEmailAndFromToRequest request) {
        LocalDateTime to = null;
        LocalDateTime from = null;
        if (!DataUtil.isNullOrEmpty(request.getFrom())) {
            from = DataUtil.convertDateStrToLocalDateTime(request.getFrom(), FORMAT_DATE_DMY_HYPHEN);
        }
        if (!DataUtil.isNullOrEmpty(request.getTo())) {
            to = DataUtil.convertDateStrToLocalDateTime(request.getTo(), FORMAT_DATE_DMY_HYPHEN);
        }
        if (DataUtil.isNullOrEmpty(request.getUsername()) && DataUtil.isNullOrEmpty(request.getEmail())) {
            //neu khong truyen username + email
            //lay 30 ngay gan nhat
            if (to == null) {
                to = LocalDateTime.now();
            }
            if (DataUtil.isNullOrEmpty(request.getFrom())) {
                from = to.minusDays(30);
            }
        }

        Integer offset = (request.getPageIndex() - 1) * request.getPageSize();
        String sort = request.getSort();
        String sortQuery = (sort.contains("-")) ? " order by " + sort.substring(1) + " desc " : " order by " + sort.substring(1);

        if (!DataUtil.isNullOrEmpty(request.getUsername())) {
            LocalDateTime finalFrom = from;
            LocalDateTime finalTo = to;
            return authClient.getEmailsByUsername(request.getUsername()).flatMap(email -> {
                if (!DataUtil.isNullOrEmpty(request.getEmail()) && !email.equals(request.getEmail())) {
                    return Mono.just(this.mapPaginationResults(new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L));
                } else {
                    return Mono.zip(transmissionRepoTemplate.searchUserTransmission(email, request.getTemplateMail(), finalFrom, finalTo, offset, request.getLimit(), sortQuery).collectList(),
                            transmissionRepoTemplate.countUserTransmission(email, request.getTemplateMail(), finalFrom, finalTo))
                            .map(tuple2 -> this.mapPaginationResults(tuple2.getT1(), request.getPageIndex(), request.getLimit(), tuple2.getT2()))
                            .switchIfEmpty(Mono.just(this.mapPaginationResults(new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
                }
            }).switchIfEmpty(Mono.just(this.mapPaginationResults(new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
        } else {
            return Mono.zip(transmissionRepoTemplate.searchUserTransmission(request.getEmail(), request.getTemplateMail(), from, to, offset, request.getLimit(), sortQuery).collectList(),
                            transmissionRepoTemplate.countUserTransmission(request.getEmail(), request.getTemplateMail(), from, to))
                    .map(tuple2 -> this.mapPaginationResults(tuple2.getT1(), request.getPageIndex(), request.getLimit(), tuple2.getT2()))
                    .switchIfEmpty(Mono.just(this.mapPaginationResults(new ArrayList<>(), request.getPageIndex(), request.getLimit(), 0L)));
        }
    }

    private DataResponse<UserTransmissionPageDTO> mapPaginationResults(List<UserTransmissionDTO> userTransmissionDTOs, Integer pageIndex, Integer limit, Long totalRecords) {
        var pagination = UserTransmissionPageDTO.Pagination.builder()
                .pageSize(limit)
                .pageIndex(pageIndex)
                .totalRecords(totalRecords).build();
        var pageResponse = UserTransmissionPageDTO.builder()
                .pagination(pagination)
                .results(userTransmissionDTOs)
                .build();
        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null, pageResponse);
    }

    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String categoryType, Integer pageIndex, Integer pageSize, String sort) {
        return SecurityUtils.getCurrentUser()
                .flatMap(user -> {
                    if (pageIndex < 1) {
                        return Mono.error(new BusinessException("GNCLBCT00001", Translator.toLocaleVi("params.pageIndex.invalid")));
                    }
                    if (pageSize < 1) {
                        return Mono.error(new BusinessException("GNCLBCT00002", Translator.toLocaleVi("params.pageSize.invalid")));
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
                    query.append(" LIMIT :pageSize  \n" +
                            "             OFFSET :index;");
                    BigDecimal index = (new BigDecimal(pageIndex - 1)).multiply(new BigDecimal(pageSize));
                    return template.getDatabaseClient().sql(String.valueOf(query))
                            .bind("receiver", user.getId())
                            .bind("categoryType", DataUtil.safeTrim(categoryType))
                            .bind("pageSize", pageSize)
                            .bind("index", index)
                            .map((x, y) -> notificationHeader(x))
                            .all()
                            .collectList()
                            .flatMap(notificationContent -> Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), notificationContent)))
                            .switchIfEmpty(Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), new ArrayList<>())));

                });

    }

    public Mono<DataResponse<Object>> validateCreateNotificationDTO(CreateNotificationDTO createNotificationDTO) {

        if (DataUtil.isNullOrEmpty(createNotificationDTO.getSeverity())) {
            createNotificationDTO.setSeverity(ConstValue.NotiServerity.NORMAL);
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(ConstValue.NotiServerity.NORMAL) && !DataUtil.safeTrim(createNotificationDTO.getSeverity()).equals(ConstValue.NotiServerity.CRITICAL)) {
            return Mono.error(new BusinessException("VCND00001", Translator.toLocaleVi(ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC, "severity")));
        }

        if (!DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(ConstValue.NotificationConstant.ANNOUNCEMENT) && !DataUtil.safeTrim(createNotificationDTO.getCategoryType()).equals(ConstValue.NotificationConstant.NEWS)) {
            return Mono.error(new BusinessException("VCND00002", Translator.toLocaleVi(ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC, "CategoryType")));
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(ConstValue.ContentTypeConstant.TEXT) && !DataUtil.safeTrim(createNotificationDTO.getContentType()).equals(ConstValue.ContentTypeConstant.HTML)) {
            return Mono.error(new BusinessException("VCND00003", Translator.toLocaleVi(ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC, "ContentType")));
        }
        if (!DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(ConstValue.Channel.CHANNEL_SMS) && !DataUtil.safeTrim(createNotificationDTO.getChannelType()).equals(ConstValue.Channel.CHANNEL_EMAIL) && !createNotificationDTO.getChannelType().equals(ConstValue.Channel.CHANNEL_REST)) {
            return Mono.error(new BusinessException("VCND00004", Translator.toLocaleVi(ConstValue.CommonMessageNoti.INVALID_FORMAT_SPEC, "ChannelType")));
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
            return authClient.getAllUserId().flatMap(
                    list -> {
                        if (DataUtil.isNullOrEmpty(list)) {
                            return Mono.error(new BusinessException("VCND00005", Translator.toLocaleVi("no.receiver")));
                        }
                        List<ReceiverDataDTO> receiverDataDTOList = createNotificationDTO.getReceiverList();
                        receiverDataDTOList.addAll(list.stream()
                                .map(x -> new ReceiverDataDTO(x, null))
                                .toList());
                        createNotificationDTO.setReceiverList(receiverDataDTOList);
                        return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), createNotificationDTO));
                    }
            );
        }
        if (!DataUtil.isNullOrEmpty(createNotificationDTO.getReceiverList())) {
            createNotificationDTO.getReceiverList().forEach(this::isReceiverDataDTOValid);
            return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS), createNotificationDTO));
        }
        return Mono.error(new BusinessException("VCND00006", Translator.toLocaleVi("no.receiver")));
    }

    private void notiContentDTOIsValid(NotiContentDTO notiContentDTO) {

        if (DataUtil.isNullOrEmpty(notiContentDTO)) {
            throw new BusinessException("NCDIV00001", Translator.toLocaleVi("params.object.null", NotiContentDTO.builder().build().getClass().getSimpleName()));
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getTitle())) {
            throw new BusinessException("NCDIV00002", Translator.toLocaleVi("params.title.null"));
        } else if (notiContentDTO.getTitle().length() > 500) {
            throw new BusinessException("NCDIV00003", Translator.toLocaleVi("params.title.outOfLength"));
        }
        if (DataUtil.isNullOrEmpty(notiContentDTO.getSubTitle())) {
            throw new BusinessException("NCDIV00004", Translator.toLocaleVi("params.subTitle.null"));
        } else if (notiContentDTO.getSubTitle().length() > 5000) {
            throw new BusinessException("NCDIV00005", Translator.toLocaleVi("params.subTitle.outOfLength"));
        }
        if (DataUtil.safeTrim(notiContentDTO.getImageUrl()).length() > 500 && !DataUtil.isNullOrEmpty(notiContentDTO.getImageUrl())) {
            throw new BusinessException("NCDIV00006", Translator.toLocaleVi("params.imageUrl.outOfLength"));
        }
        if (DataUtil.safeTrim(notiContentDTO.getUrl()).length() > 300 && !DataUtil.isNullOrEmpty(notiContentDTO.getUrl())) {
            throw new BusinessException("NCDIV00007", Translator.toLocaleVi("params.url.outOfLength"));
        }

    }

    private void isReceiverDataDTOValid(ReceiverDataDTO receiverDataDTO) {
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && DataUtil.isNullOrEmpty(receiverDataDTO.getEmail())) {
            throw new BusinessException("IRDDV00001", Translator.toLocaleVi("params.receiverDataDTO.notAllNull"));
        }
        if (!DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && !DataUtil.isUUID(receiverDataDTO.getUserId()) && (DataUtil.isNullOrEmpty(receiverDataDTO.getEmail()) || !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX))) {
            throw new BusinessException("IRDDV00002", Translator.toLocaleVi("receiver.string.invalid"));
        }
        if (DataUtil.safeTrim(receiverDataDTO.getEmail()).length() > 200) {
            throw new BusinessException("IRDDV00003", Translator.toLocaleVi("params.email.outOfLength"));
        }
        if (DataUtil.isNullOrEmpty(receiverDataDTO.getUserId()) && !ValidateUtils.validateRegex(receiverDataDTO.getEmail(), Regex.EMAIL_REGEX)) {
            throw new BusinessException("IRDDV00004", Translator.toLocaleVi("receiver.email.invalid"));
        }
    }

    private NotificationHeader notificationHeader(Row row) {
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

    private CreateNotificationDTO createNotificationDTO(String subTitle, String title, String template, ReceiverDataDTO data, String externalData, String url) {
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
        createNotificationDTO.setSender(ConstValue.RoleName.SYSTEM);
        createNotificationDTO.setSeverity(ConstValue.NotificationConstant.SEVERITY);
        createNotificationDTO.setTemplateMail(template);
        createNotificationDTO.setNotiContentDTO(NotiContentDTO.builder().title(title).subTitle(subTitle).externalData(externalData).url(url).build());
        createNotificationDTO.setContentType(ConstValue.NotificationConstant.CONTENT_TYPE);
        createNotificationDTO.setCategoryType(ConstValue.NotificationConstant.ANNOUNCEMENT);
        createNotificationDTO.setChannelType(ConstValue.NotificationConstant.CHANNEL_TYPE);
        List<ReceiverDataDTO> list = new ArrayList<>();
        list.add(data);
        createNotificationDTO.setReceiverList(list);
        return createNotificationDTO;
    }
}
