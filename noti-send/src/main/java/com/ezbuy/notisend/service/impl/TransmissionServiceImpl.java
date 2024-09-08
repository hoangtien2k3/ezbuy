/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.notisend.service.impl;

import com.ezbuy.notimodel.dto.EmailResultDTO;
import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import com.ezbuy.notisend.client.AuthClient;
import com.ezbuy.notisend.repository.TransmissionRepository;
import com.ezbuy.notisend.service.MailService;
import com.ezbuy.notisend.service.TransmissionService;
import io.hoangtien2k3.commons.constants.Regex;
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.AppUtils;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.ValidateUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransmissionServiceImpl implements TransmissionService {
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
        return transmissionRepository
                .getTransmissionsToSendMail(resendCount, limit)
                .collectList()
                .flatMap(transmissionNotis -> {
                    if (DataUtil.isNullOrEmpty(transmissionNotis)) {
                        log.info("transmissionList is empty");
                        return Mono.just(new DataResponse<>(null, "success"));
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
                        log.info("Email transmission empty");
                    }

                    Mono<Boolean> updateResultVar = DataUtil.isNullOrEmpty(successIds)
                            ? Mono.just(true)
                            : AppUtils.insertData(
                                    transmissionRepository.updateTransmissionRestSuccessState(successIds));

                    return updateResultVar.flatMap(updateResult -> {
                        successIds.clear();

                        Mono<List<EmailResultDTO>> emailResultMono;
                        if (!DataUtil.isNullOrEmpty(emailTransmissions)) {
                            List<TransmissionNotiDTO> transmissionHaveEmailList = emailTransmissions.stream()
                                    .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getEmail())
                                            && ValidateUtils.validateRegex(
                                                    DataUtil.safeTrim(transmissionNoti.getEmail()), Regex.EMAIL_REGEX))
                                    .collect(Collectors.toList());

                            List<String> receiverIds = emailTransmissions.stream()
                                    .filter(transmissionNoti -> !DataUtil.isNullOrEmpty(transmissionNoti.getReceiver())
                                            && (DataUtil.isNullOrEmpty(DataUtil.safeTrim(transmissionNoti.getEmail()))
                                                    || !ValidateUtils.validateRegex(
                                                            DataUtil.safeTrim(transmissionNoti.getEmail()),
                                                            Regex.EMAIL_REGEX)))
                                    .map(TransmissionNotiDTO::getReceiver)
                                    .collect(Collectors.toList());

                            emailResultMono = handleEmails(receiverIds, transmissionHaveEmailList, emailTransmissions);

                        } else {
                            emailResultMono = Mono.just(new ArrayList<>());
                        }

                        return emailResultMono.flatMap(
                                emailResults -> updateTransmissionStatus(emailResults, successIds));
                    });
                });
    }

    private Mono<List<EmailResultDTO>> handleEmails(
            List<String> receiverIds,
            List<TransmissionNotiDTO> transmissionHaveEmailList,
            List<TransmissionNotiDTO> emailTransmissions) {
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

    private Mono<DataResponse<Object>> updateTransmissionStatus(
            List<EmailResultDTO> emailResults, List<String> successIds) {
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

        return Mono.zip(successUpdateMono, failedUpdateMono).map(data -> new DataResponse<>(null, "success"));
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
                .map(receiverIdList -> authClient.getContacts(receiverIdList).map(responseOpt -> {
                    Map<String, String> map = new HashMap<>();
                    if (DataUtil.isNullOrEmpty(responseOpt)) {
                        log.info("Email list not found");
                        return map;
                    }
                    responseOpt
                            .getData()
                            .forEach(contactInfoDTO -> map.put(
                                    DataUtil.safeToString(contactInfoDTO.getId()),
                                    DataUtil.safeToString(contactInfoDTO.getEmail())));
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
