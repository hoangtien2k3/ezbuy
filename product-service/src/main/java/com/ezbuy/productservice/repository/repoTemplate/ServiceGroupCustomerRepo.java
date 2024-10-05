package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.sme.productmodel.dto.ServiceDTO;
import com.ezbuy.sme.productmodel.dto.ServiceGroupDTO;
import com.ezbuy.sme.productmodel.request.SearchServiceGroupRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceGroupCustomerRepo {
    Flux<ServiceGroupDTO> findServiceGroup(SearchServiceGroupRequest request);
    Mono<Long> countServiceGroup(SearchServiceGroupRequest request);
    Flux<ServiceDTO> getAllServiceGroupAndTelecomServiceActive();
}
