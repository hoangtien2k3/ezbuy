package com.ezbuy.paymentservice.client;

import java.util.List;

import com.ezbuy.paymentservice.model.dto.OptionSetValue;
import reactor.core.publisher.Mono;

public interface SettingClient {

    Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode);
}
