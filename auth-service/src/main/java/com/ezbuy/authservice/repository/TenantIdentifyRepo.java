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
package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.TenantIdentify;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TenantIdentifyRepo extends R2dbcRepository<TenantIdentify, String> {

    @Query(
            value = "select exists(\n" + "    select id\n" + "    from sme_user.tenant_identify\n"
                    + "    where status =1 and trust_status =1 and id_type =:idType and id_no =:idNo and type =:type\n"
                    + ")")
    Mono<Boolean> existsTrustedByIdNoAndType(String idNo, String idType, String type);
}
