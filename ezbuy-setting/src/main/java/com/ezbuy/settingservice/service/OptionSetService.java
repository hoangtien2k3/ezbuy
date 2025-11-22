package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingservice.model.entity.OptionSet;
import com.ezbuy.settingservice.model.entity.OptionSetValue;
import com.ezbuy.settingservice.model.dto.request.CreateOptionSetRequest;
import com.ezbuy.settingservice.model.dto.response.SearchOptionSetResponse;
import com.ezbuy.core.model.response.DataResponse;
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
