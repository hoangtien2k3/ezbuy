package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.CustType;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustTypeService {
    Mono<List<CustType>> getAllCustTypeActive();
}
