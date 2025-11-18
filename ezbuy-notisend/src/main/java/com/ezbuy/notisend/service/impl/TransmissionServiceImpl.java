package com.ezbuy.notisend.service.impl;

import com.ezbuy.notisend.client.AuthClient;
import com.ezbuy.notisend.common.ConstValue;
import com.ezbuy.notisend.model.dto.ContactInfoDTO;
import com.ezbuy.notisend.model.dto.EmailResultDTO;
import com.ezbuy.notisend.model.dto.TransmissionNotiDTO;
import com.ezbuy.notisend.repository.TransmissionRepository;
import com.ezbuy.notisend.service.MailService;
import com.ezbuy.notisend.service.TransmissionService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Regex;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.AppUtils;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.Translator;
import com.ezbuy.core.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransmissionServiceImpl implements TransmissionService {

    @Value("${config.resendCount}")
    private Integer resendCount;

    @Value("${config.limit}")
    private Integer limit;

    private final TransmissionRepository transmissionRepository;
    private final AuthClient authClient;
    private final MailService mailService;

    @Override
    @Transactional
    public Mono<DataResponse<Object>> sendNotification() {
        return transmissionRepository.getTransmissionsToSendMail(resendCount, limit).collectList()
                .flatMap(transmissionNotis -> {
                    if (DataUtil.isNullOrEmpty(transmissionNotis)) {
                        log.info("transmissionList is empty");
                        return Mono.just(new DataResponse<>(null, Translator.toLocaleVi(CommonErrorCode.SUCCESS)));
                    }
                    List<String> successIds = new ArrayList<>();
                    List<TransmissionNotiDTO> emailTransmissions = new ArrayList<>();
                    for (TransmissionNotiDTO transmissionNoti : transmissionNotis) {
                        // add channel type = REST for success
                        if (DataUtil.safeEqual(transmissionNoti.getType(), ConstValue.Channel.CHANNEL_REST)) {
                            successIds.add(transmissionNoti.getId());
                        } else {
                            emailTransmissions.add(transmissionNoti);
                        }
                    }
                    if (emailTransmissions.stream().allMatch(x -> DataUtil.isNullOrEmpty(x.getEmail()))) {
                        log.info("Email Transmission empty");
                    }
                    Mono<Boolean> updateResultVar;
                    if (!DataUtil.isNullOrEmpty(successIds)) {
                        updateResultVar = AppUtils.insertData(transmissionRepository.updateTransmissionRestSuccessState(successIds));
                    } else {
                        updateResultVar = Mono.just(true);
                    }
                    return updateResultVar
                            .flatMap(updateResult -> {
                                successIds.clear();
                                Mono<List<EmailResultDTO>> emailResultMono;
                                if (!DataUtil.isNullOrEmpty(emailTransmissions)) {
                                    // check receiver = user_id to get email of user
                                    List<TransmissionNotiDTO> transmissionHaveEmailList = emailTransmissions.stream()
                                            .filter(transmissionNoti ->
                                                    !DataUtil.isNullOrEmpty(transmissionNoti.getEmail())
                                                            && ValidateUtils.validateRegex(DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX))
                                            .toList();
                                    emailTransmissions.removeAll(emailTransmissions.stream()
                                            .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getEmail()) && ValidateUtils.validateRegex(DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX))
                                            .toList());
                                    List<String> receiverIds = emailTransmissions.stream()
                                            .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getReceiver()) && (
                                                    DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionNoti.getEmail())) || !ValidateUtils.validateRegex(DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX)))
                                            .map(TransmissionNotiDTO::getReceiver)
                                            .collect(Collectors.toList());
                                    Mono<Map<String, String>> emailsMono;
                                    if (!DataUtil.isNullOrEmpty(receiverIds)) {
                                        List<List<String>> listOfMax200List = new ArrayList<>();
                                        //auth service xu ly toi da 200 receiver 1 lan
                                        for (int i = 0; i < receiverIds.size(); i += 200) {
                                            int end = Math.min(receiverIds.size(), i + 200);
                                            List<String> subList = receiverIds.subList(i, end);
                                            listOfMax200List.add(subList);
                                        }
                                        emailsMono = combineMap(listOfMax200List);
                                    } else {
                                        emailsMono = Mono.just(new HashMap<>());
                                    }
                                    emailResultMono = emailsMono
                                            .flatMap(contactInfoMap -> {
                                                List<TransmissionNotiDTO> emailNotis = new ArrayList<>();
                                                // check info to send email
                                                for (TransmissionNotiDTO transmissionNoti : emailTransmissions) {
                                                    String email;
                                                    if (DataUtil.isNullOrEmpty(transmissionNoti.getReceiver())) {
                                                        email = transmissionNoti.getEmail();
                                                    } else {
                                                        email = contactInfoMap.get(transmissionNoti.getReceiver());
                                                    }
                                                    if (!DataUtil.isNullOrEmpty(email)
                                                            && !DataUtil.isNullOrEmpty(transmissionNoti.getSender())) {
                                                        transmissionNoti.setEmail(email);
                                                        emailNotis.add(transmissionNoti);
                                                    }
                                                }
                                                emailNotis.addAll(transmissionHaveEmailList);
                                                // send mail
                                                return mailService.sendMailByTransmission(emailNotis);
                                            });

                                } else {
                                    emailResultMono = Mono.just(new ArrayList<>());
                                }
                                // update info
                                return emailResultMono
                                        .flatMap(emailResults -> {
                                            List<String> failedIds = new ArrayList<>();
                                            for (EmailResultDTO emailResult : emailResults) {
                                                if (Boolean.TRUE.equals(emailResult.getIsSuccess())) {
                                                    successIds.add(emailResult.getTransmissionId());
                                                } else {
                                                    failedIds.add(emailResult.getTransmissionId());
                                                }
                                            }
                                            Mono<Boolean> successUpdateMono;
                                            if (!DataUtil.isNullOrEmpty(successIds)) {
                                                successUpdateMono = transmissionRepository.updateTransmissionEmailSuccessState(successIds);
                                            } else {
                                                successUpdateMono = Mono.just(true);
                                            }
                                            Mono<Boolean> failedUpdateMono;
                                            if (!DataUtil.isNullOrEmpty(failedIds)) {
                                                failedUpdateMono = transmissionRepository.updateTransmissionFailedState(failedIds);
                                            } else {
                                                failedUpdateMono = Mono.just(true);
                                            }
                                            // update success and failed
                                            log.info("End call API");
                                            return Mono.zip(successUpdateMono, failedUpdateMono)
                                                    .map(data -> (new DataResponse<>(null, Translator.toLocaleVi(CommonErrorCode.SUCCESS))));
                                        });
                            });

                });
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
                            List<ContactInfoDTO> data = responseOpt.getData();
                            if (!DataUtil.isNullOrEmpty(data)) {
                                for (ContactInfoDTO contactInfoDTO : data) {
                                    map.put(DataUtil.safeToString(contactInfoDTO.getId()),
                                            DataUtil.safeToString(contactInfoDTO.getEmail()));
                                }
                            }
                            log.info("Get {} email", map.size());
                            return map;
                        }))
                .collect(Collectors.toList());
        return Mono.zip(emailMonoList, response -> Arrays.stream(response)
                        .map(object -> (Map<String, String>) object)
                        .collect(Collectors.toList()))
                .flatMap(response -> {
                    Map<String, String> result = new HashMap<>();
                    for (Map<String, String> element : response) {
                        result.putAll(element);
                    }
                    return Mono.just(result);
                });
    }
}
