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
package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.constants.AuthConstants;
import com.ezbuy.authmodel.model.TenantIdentify;
import com.ezbuy.authservice.client.NotiServiceClient;
import com.ezbuy.authservice.repository.OrganizationRepo;
import com.ezbuy.authservice.repository.TenantIdentifyRepo;
import com.ezbuy.authservice.service.IdentifyService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentifyServiceImpl implements IdentifyService {

    private final TenantIdentifyRepo tenantIdentifyRepo;
    private final NotiServiceClient notiServiceClient;
    private final OrganizationRepo organizationRepo;

    @Value("${minio.private.bucket}")
    public String smePrivateBucket;

    @Override
    public Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies) {
        List<Mono<Boolean>> observers = new ArrayList<>();
        for (TenantIdentify identify : identifies) {
            var checkExisted = tenantIdentifyRepo.existsTrustedByIdNoAndType(
                    identify.getIdNo(), identify.getIdType(), AuthConstants.TenantType.ORGANIZATION);
            observers.add(checkExisted);
        }
        return Flux.concat(observers).collectList().flatMap(checkers -> {
            for (Boolean check : checkers) {
                if (Boolean.TRUE.equals(check)) {
                    return Mono.justOrEmpty(true);
                }
            }
            return Mono.justOrEmpty(false);
        });
    }
}
