package com.ezbuy.notificationsend.client;

import com.ezbuy.settingmodel.model.OptionSetValue;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface SettingClient {

    Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode);

    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);

}
