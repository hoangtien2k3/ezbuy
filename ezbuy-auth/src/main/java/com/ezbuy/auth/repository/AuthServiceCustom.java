package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.dto.response.PermissionPolicyDetailDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthServiceCustom {
    Mono<Integer> countPermissionPolicyDetail(String filter, Integer state, String sort);

    Flux<PermissionPolicyDetailDto> getPermissionPolicyDetail(
            String filter, Integer state, Integer offset, Integer pageSize, String sort);
}
