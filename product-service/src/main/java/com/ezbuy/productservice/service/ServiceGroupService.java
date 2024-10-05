package com.ezbuy.productservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.dto.ServiceGroupDTO;
import com.ezbuy.sme.productmodel.model.ServiceGroup;
import com.ezbuy.sme.productmodel.request.CreateServiceGroupRequest;
import com.ezbuy.sme.productmodel.request.SearchServiceGroupRequest;
import com.ezbuy.sme.productmodel.response.SearchServiceGroupResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServiceGroupService {
    Mono<DataResponse<ServiceGroup>> createServiceGroup(CreateServiceGroupRequest request);
    Mono<DataResponse<ServiceGroup>> editServiceGroup(String groupId, CreateServiceGroupRequest request);
    Mono<SearchServiceGroupResponse> findServiceGroup(SearchServiceGroupRequest request);
    Mono<List<ServiceGroup>> getAllServiceGroupActive();
}
