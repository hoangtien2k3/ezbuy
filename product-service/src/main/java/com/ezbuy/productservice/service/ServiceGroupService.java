package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.model.ServiceGroup;
import com.ezbuy.productmodel.request.CreateServiceGroupRequest;
import com.ezbuy.productmodel.request.SearchServiceGroupRequest;
import com.ezbuy.productmodel.response.SearchServiceGroupResponse;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServiceGroupService {
    Mono<DataResponse<ServiceGroup>> createServiceGroup(CreateServiceGroupRequest request);
    Mono<DataResponse<ServiceGroup>> editServiceGroup(String groupId, CreateServiceGroupRequest request);
    Mono<SearchServiceGroupResponse> findServiceGroup(SearchServiceGroupRequest request);
    Mono<List<ServiceGroup>> getAllServiceGroupActive();
}
