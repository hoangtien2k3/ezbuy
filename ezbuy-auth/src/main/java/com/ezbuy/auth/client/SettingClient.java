package com.ezbuy.auth.client;

import com.ezbuy.core.model.response.DataResponse;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface SettingClient {

    Mono<Optional<DataResponse<String>>> getAdminRole(@RequestParam String originId);

    Mono<DataResponse<String>> findByCode(String code);

    Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode);
}
