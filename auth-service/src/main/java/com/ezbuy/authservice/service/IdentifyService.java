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

import com.ezbuy.authmodel.model.TenantIdentify;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Service interface for handling identification of trusted organizations.
 */
public interface IdentifyService {

    /**
     * Checks if the given list of tenant identifies contains any trusted
     * organization identifies.
     *
     * @param identifies
     *            the list of tenant identifies to check
     * @return a Mono emitting a Boolean indicating whether any trusted organization
     *         identifies exist
     */
    Mono<Boolean> existedTrustedOrgIdentify(List<TenantIdentify> identifies);
}
