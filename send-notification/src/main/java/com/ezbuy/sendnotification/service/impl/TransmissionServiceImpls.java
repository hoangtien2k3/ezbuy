package com.ezbuy.sendnotification.service.impl;

import com.ezbuy.framework.constants.Regex;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.AppUtils;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.Translator;
import com.ezbuy.framework.utils.ValidateUtils;
import com.ezbuy.sendnotification.client.AuthClient;
import com.ezbuy.sendnotification.model.noti.EmailResultDTO;
import com.ezbuy.sendnotification.model.noti.TransmissionNotiDTO;
import com.ezbuy.sendnotification.repository.TransmissionRepository;
import com.ezbuy.sendnotification.service.MailService;
import com.ezbuy.sendnotification.service.TransmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.ezbuy.framework.constants.MessageConstant.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransmissionServiceImpls implements TransmissionService {
    private final TransmissionRepository transmissionRepository;
    private final AuthClient authClient;
    private final MailService mailService;

    @Value("${config.resendCount}")
    private Integer resendCount;
    @Value("${config.limit}")
    private Integer limit;

    @Override
    @Transactional
    public Mono<DataResponse<Object>> sendNotification() {
        return transmissionRepository.getTransmissionsToSendMail(resendCount, limit)
                .collectList()
                .flatMap(transmissionNotis -> {
                    if (DataUtil.isNullOrEmpty(transmissionNotis)) {
                        log.info("transmissionList is empty");
                        return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(SUCCESS)));
                    }

                    List<String> successIds = new ArrayList<>();
                    List<TransmissionNotiDTO> emailTransmissions = transmissionNotis.stream()
                            .filter(transmissionNoti -> {
                                if (DataUtil.safeEqual(transmissionNoti.getType(), "REST")) {
                                    successIds.add(transmissionNoti.getId());
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toList());

                    if (emailTransmissions.stream().allMatch(x -> DataUtil.isNullOrEmpty(x.getEmail()))) {
                        log.info("Email Transmission empty");
                    }

                    Mono<Boolean> updateResultVar = DataUtil.isNullOrEmpty(successIds)
                            ? Mono.just(true)
                            : AppUtils.insertData(transmissionRepository.updateTransmissionRestSuccessState(successIds));

                    return updateResultVar.flatMap(updateResult -> {
                        successIds.clear();

                        Mono<List<EmailResultDTO>> emailResultMono;
                        if (!DataUtil.isNullOrEmpty(emailTransmissions)) {
                            List<TransmissionNotiDTO> transmissionHaveEmailList = emailTransmissions.stream()
                                    .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getEmail()) &&
                                            ValidateUtils.validateRegex(DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX))
                                    .collect(Collectors.toList());

                            List<String> receiverIds = emailTransmissions.stream()
                                    .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getReceiver()) &&
                                            (DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionNoti.getEmail())) ||
                                                    !ValidateUtils.validateRegex(DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX)))
                                    .map(TransmissionNotiDTO::getReceiver)
                                    .collect(Collectors.toList());

                            emailResultMono = handleEmails(receiverIds, transmissionHaveEmailList, emailTransmissions);

                        } else {
                            emailResultMono = Mono.just(new ArrayList<>());
                        }

                        return emailResultMono.flatMap(emailResults -> updateTransmissionStatus(emailResults, successIds));
                    });
                });
    }

    private Mono<List<EmailResultDTO>> handleEmails(List<String> receiverIds, List<TransmissionNotiDTO> transmissionHaveEmailList, List<TransmissionNotiDTO> emailTransmissions) {
        return (DataUtil.isNullOrEmpty(receiverIds)
                ? Mono.just(new HashMap<String, String>())
                : combineMap(partitionList(receiverIds, 200)))
                .flatMap(contactInfoMap -> {
                    List<TransmissionNotiDTO> emailNotis = new ArrayList<>();
                    for (TransmissionNotiDTO transmissionNoti : emailTransmissions) {
                        String email = DataUtil.isNullOrEmpty(transmissionNoti.getReceiver())
                                ? transmissionNoti.getEmail()
                                : contactInfoMap.get(transmissionNoti.getReceiver());

                        if (!DataUtil.isNullOrEmpty(email) && !DataUtil.isNullOrEmpty(transmissionNoti.getSender())) {
                            transmissionNoti.setEmail(email);
                            emailNotis.add(transmissionNoti);
                        }
                    }
                    emailNotis.addAll(transmissionHaveEmailList);
                    return mailService.sendMailByTransmission(emailNotis);
                });
    }


    private Mono<DataResponse<Object>> updateTransmissionStatus(List<EmailResultDTO> emailResults, List<String> successIds) {
        List<String> failedIds = emailResults.stream()
                .filter(emailResult -> Boolean.FALSE.equals(emailResult.getIsSuccess()))
                .map(EmailResultDTO::getTransmissionId)
                .collect(Collectors.toList());

        successIds.addAll(emailResults.stream()
                .filter(emailResult -> Boolean.TRUE.equals(emailResult.getIsSuccess()))
                .map(EmailResultDTO::getTransmissionId)
                .toList());

        Mono<Boolean> successUpdateMono = DataUtil.isNullOrEmpty(successIds)
                ? Mono.just(true)
                : transmissionRepository.updateTransmissionEmailSuccessState(successIds);

        Mono<Boolean> failedUpdateMono = DataUtil.isNullOrEmpty(failedIds)
                ? Mono.just(true)
                : transmissionRepository.updateTransmissionFailedState(failedIds);

        return Mono.zip(successUpdateMono, failedUpdateMono)
                .map(data -> new DataResponse<>(null, Translator.toLocaleVi(SUCCESS)));
    }

    private List<List<String>> partitionList(List<String> list, int size) {
        List<List<String>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(list.size(), i + size)));
        }
        return partitions;
    }

    private Mono<Map<String, String>> combineMap(List<List<String>> listOfMax200List) {
        List<Mono<Map<String, String>>> emailMonoList = listOfMax200List.stream()
                .map(receiverIdList -> authClient.getContacts(receiverIdList)
                        .map(responseOpt -> {
                            Map<String, String> map = new HashMap<>();
                            if (DataUtil.isNullOrEmpty(responseOpt)) {
                                log.info("Email list not found");
                                return map;
                            }
                            responseOpt.getData().forEach(contactInfoDTO ->
                                    map.put(DataUtil.safeToString(contactInfoDTO.getId()), DataUtil.safeToString(contactInfoDTO.getEmail())));
                            log.info("Get {} email", map.size());
                            return map;
                        }))
                .collect(Collectors.toList());

        return Mono.zip(emailMonoList, results -> Arrays.stream(results)
                .map(object -> (Map<String, String>) object)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}

