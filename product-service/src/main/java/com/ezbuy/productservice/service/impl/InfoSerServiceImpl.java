package com.ezbuy.productservice.service.impl;

import com.ezbuy.productservice.repository.SubscriberRepository;
import com.ezbuy.productservice.client.SettingClient;
import com.ezbuy.productservice.service.InfoSerService;
import com.ezbuy.sme.settingmodel.dto.TelecomDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class InfoSerServiceImpl implements InfoSerService {

    private final SubscriberRepository subcriberRepository;
    private final SettingClient settingClient;

    @Override
    public Mono<List<TelecomDTO>> getActiveTelecomService(List<String> idNos) {
        return subcriberRepository.findTelecomServicesByIdNo(idNos)
                .collectList().flatMap(originalIds -> {
                    if(originalIds == null || originalIds.size() ==0){
                        return Mono.just(new ArrayList<TelecomDTO>());
                    }
                    return settingClient.getTelecomServices(originalIds);
                })
                .switchIfEmpty(Mono.justOrEmpty(null));
    }

    @Override
    public Mono<List<String>> getActiveTelecomServiceIdByOrgId(String idNo) {
        return subcriberRepository.findTelecomAliasByIdNo(List.of(idNo))
                .collectList()
                .switchIfEmpty(Mono.justOrEmpty(new ArrayList<>()));
    }

    /**
     *
     * @param idNo
     * @return
     */
    @Override
    public Mono<List<String>> getActiveTelecomServiceAlilasByIdNo(String idNo) {
        return subcriberRepository.findTelecomAlilasByIdNo(List.of(idNo))
                .collectList()
                .switchIfEmpty(Mono.justOrEmpty(new ArrayList<>()));
    }
}
