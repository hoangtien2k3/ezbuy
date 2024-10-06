package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.TelecomDTO;
import com.viettel.sme.settingmodel.model.OptionSetValue;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SettingClient {

    Mono<List<TelecomDTO>> getTelecomData(List<String> ids,
                                          List<String> aliases,
                                          List<String> origins);

    /**
     * lay danh sach value cau hinh
     * @param optionSetCode ma option set
     * @return
     */
    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);
}
