package com.ezbuy.settingservice.service.impl;

import com.ezbuy.framework.annotations.LocalCache;
import com.ezbuy.settingmodel.model.CustType;
import com.ezbuy.settingservice.repository.CustTypeRepository;
import com.ezbuy.settingservice.service.CustTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustTypeServiceImpl extends BaseServiceHandler implements CustTypeService {
    private final CustTypeRepository custTypeRepository;

    @Override
    @LocalCache(durationInMinute = 720)
    public Mono<List<CustType>> getAllCustTypeActive() {
        return custTypeRepository.getAllCustTypeActive().collectList();
    }
}
