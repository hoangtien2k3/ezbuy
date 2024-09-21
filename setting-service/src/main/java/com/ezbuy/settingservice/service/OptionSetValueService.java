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
