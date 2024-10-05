package com.ezbuy.productservice.service.impl;

import com.ezbuy.productservice.repository.repoTemplate.TelecomServiceRepository;
import com.ezbuy.productservice.service.TelecomServiceService;
import com.ezbuy.sme.productmodel.dto.TelecomServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TelecomServiceServiceImpl implements TelecomServiceService {

    private final TelecomServiceRepository telecomServiceRepository;

    @Override
    public Flux<TelecomServiceResponse> getTelecomServices() {
        return telecomServiceRepository.getTelecomServices();
    }
}
