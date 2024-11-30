package com.ezbuy.orderservice.client;

import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.OptionSetValue;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SettingClient {

    Mono<List<TelecomDTO>> getTelecomData(List<String> ids, List<String> aliases, List<String> origins);

    /**
     * Ham lay danh sach dieu khoan trong DB
     *
     * @param code
     * @return
     */
    Mono<List<OptionSetValueDTO>> getConfDataPolicy(String code);

    /**
     * Ham lay danh sach tat ca dich vu
     *
     * @return
     */
    Mono<List<Telecom>> getAllTelecomService();

    Mono<List<OptionSetValue>> getAllActiveOptionSetValueByOptionSetCode(String code);

    /**
     * lay ten tinh, huyen, xa
     *
     * @param province
     * @param district
     * @param precinct
     * @return
     */
    Mono<AreaDTO> getAreaName(String province, String district, String precinct);
}
