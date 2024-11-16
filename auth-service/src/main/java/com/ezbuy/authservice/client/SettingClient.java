package com.ezbuy.authservice.client;

import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.dto.request.GetServiceConfigRequest;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.reactify.model.response.DataResponse;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface SettingClient {

    Mono<DataResponse<List<AreaDTO>>> getAreas(String parentCode);

    Mono<Optional<DataResponse>> getAdminRole(@RequestParam String originId);

    Mono<Optional<DataResponse>> getAdminRoleByAlias(@RequestParam String originId);

    Mono<Optional<DataResponse>> getConfig(List<String> telecomServiceIds, List<String> originalIds, String syncType);

    Mono<DataResponse<String>> findByCode(String code);

    /**
     * Ham lay thong tin cau hinh option set value theo option set code
     * @param optionSetCode
     * @param optionSetValueCode
     * @return
     */
    Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode);

    //lay ra danh sach tien to theo list Alias
    Mono<Optional<DataResponse>> getLstAcronymByAliases(@RequestParam String code, @RequestParam List<String> aliases);

    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);
}
