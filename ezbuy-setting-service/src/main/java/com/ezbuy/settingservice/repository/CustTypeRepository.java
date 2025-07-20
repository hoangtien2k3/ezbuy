package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.CustType;
import com.ezbuy.settingmodel.model.Telecom;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface CustTypeRepository extends R2dbcRepository<Telecom, String> {
    @Query(
            "select cust_type, name, status, group_type, tax, plan, description from cust_type where status = 1 ORDER BY name")
    Flux<CustType> getAllCustTypeActive();
}
