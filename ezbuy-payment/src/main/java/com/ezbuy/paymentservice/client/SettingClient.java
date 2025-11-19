package com.ezbuy.paymentservice.client;

import com.ezbuy.settingmodel.model.OptionSetValue;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SettingClient {

    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);
}
