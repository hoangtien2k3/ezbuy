package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.model.CustType;
import com.ezbuy.settingservice.repository.CustTypeRepository;
import com.ezbuy.settingservice.service.CustTypeService;
import java.util.List;

import io.hoangtien2k3.reactify.annotations.LocalCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
