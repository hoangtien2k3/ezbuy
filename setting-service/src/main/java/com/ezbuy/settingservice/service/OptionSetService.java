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
package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingmodel.model.OptionSet;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface OptionSetService {
    /**
     * lay danh sach option_set_value theo optionSetCode
     *
     * @param optionSetCode
     * @return
     */
    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);

    /**
     * lay ra option_set_value theo optionSetCode va optionSetValueCode
     *
     * @param optionSetCode
     * @param optionValueCode
     * @return
     */
    Mono<OptionSetValue> findByOptionSetCodeAndOptionValueCode(String optionSetCode, String optionValueCode);

    /**
     * Tao moi doi tuong option_set
     *
     * @param request
     * @return
     */
    Mono<DataResponse<OptionSet>> createOptionSet(CreateOptionSetRequest request);

    /**
     * Cap nhat doi tuong option_set theo id
     *
     * @param request
     * @return
     */
    Mono<DataResponse<OptionSet>> editOptionSet(String id, CreateOptionSetRequest request);

    /**
     * Lay ra tat ca option_set theo tieu chi tim kiem truyen vao
     *
     * @param request
     * @return
     */
    Mono<SearchOptionSetResponse> findOptionSet(SearchOptionSetRequest request);
}
