package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.CustType;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CustTypeService {
    Mono<List<CustType>> getAllCustTypeActive();
}
