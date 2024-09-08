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
package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.request.PageTelecomRequest;
import com.ezbuy.settingmodel.request.TelecomSearchingRequest;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TelecomRepositoryTemplate {

    Flux<TelecomDTO> getAll(List<String> ids, List<String> aliases, List<String> origins);

    Flux<String> getServiceTypes();

    Flux<Telecom> queryTelecomServices(TelecomSearchingRequest request);

    Mono<Long> countTelecomServices(TelecomSearchingRequest request);

    Flux<TelecomDTO> getAllByRequest(PageTelecomRequest request);

    Mono<Long> getTotalByRequest(PageTelecomRequest request);
}
