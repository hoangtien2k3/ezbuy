/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.UserProfileDTO;
import com.ezbuy.authmodel.dto.request.QueryUserRequest;
import com.ezbuy.authmodel.dto.request.UpdateUserRequest;
import com.ezbuy.authmodel.dto.response.*;
import com.ezbuy.authmodel.model.UserProfile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Optional<UserProfile>> getUserProfile();

    Mono<UserProfile> update(UpdateUserRequest u);

    Flux<UserContact> getUserContacts(List<UUID> userIds);

    Mono<Optional<UserProfile>> getUserById(String id);

    // Mono<List<CredentialResponse>> getCredentialsList(String idNo);

    // Mono<DataResponse> sighHash(SignHashRequest request);

    // Mono<DataResponse> getStatusSignHash();

    Mono<QueryUserResponse> queryUserProfile(QueryUserRequest request);

    Mono<Resource> exportUser(QueryUserRequest request);

    Mono<UserProfileDTO> getUserProfile(String id);

    // Flux<TaxBranchResponse> getTaxBranches();

    // Flux<IdTypeResponse> getIdTypes();

    // Mono<Account> findUserByIdNo(String idNo, String idType);
}
