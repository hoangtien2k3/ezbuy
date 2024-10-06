package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.ServiceDTO;
import com.ezbuy.productmodel.dto.ServiceGroupDTO;
import com.ezbuy.productmodel.request.SearchServiceGroupRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceGroupCustomerRepo {
    Flux<ServiceGroupDTO> findServiceGroup(SearchServiceGroupRequest request);
    Mono<Long> countServiceGroup(SearchServiceGroupRequest request);
    Flux<ServiceDTO> getAllServiceGroupAndTelecomServiceActive();
}
