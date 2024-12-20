package com.ezbuy.notificationsend.client;

import com.ezbuy.settingmodel.model.OptionSetValue;
import reactor.core.publisher.Mono;

public interface SettingClient {

    Mono<OptionSetValue> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode);

}
