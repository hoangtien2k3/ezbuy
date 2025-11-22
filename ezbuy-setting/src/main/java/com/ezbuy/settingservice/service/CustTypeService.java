package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.entity.CustType;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CustTypeService {
    Mono<List<CustType>> getAllCustTypeActive();
}
