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

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetValueRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetValueResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface OptionSetValueService {
    Mono<List<OptionSetValueDTO>> getAllActiveDataPolicyConfigByOptionSetCode(String optionSetCode);

    Mono<List<OptionSetValue>> getAllActiveOptionSetValueByOptionSetCode(String optionSetCode);

    /**
     * Tao moi doi tuong option_set_value
     *
     * @param request
     * @return
     */
    Mono<DataResponse<OptionSetValue>> createOptionSetValue(CreateOptionSetValueRequest request);

    /**
     * Cap nhat doi tuong option_set_value theo id
     *
     * @param request
     * @return
     */
    Mono<DataResponse<OptionSetValue>> editOptionSetValue(String id, CreateOptionSetValueRequest request);

    /**
     * Lay ra tat ca option_set_value theo option_set_id
     *
     * @param request
     * @return
     */
    Mono<SearchOptionSetValueResponse> findOptionSetValueByOptionSetId(SearchOptionSetValueRequest request);

    /**
     * Lay ra list tien to theo lst Alias
     *
     * @param code
     * @param serviceAliases
     * @return
     */
    Mono<List<OptionSetValue>> getLstAcronymByAliases(String code, List<String> serviceAliases);
}
