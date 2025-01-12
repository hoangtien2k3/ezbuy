package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.TelecomServiceResponse;
import com.ezbuy.productmodel.dto.response.MegaMenuResponse;
import reactor.core.publisher.Flux;

public interface TelecomServiceRepository {
    Flux<MegaMenuResponse> getProductsForMegaMenu();

    Flux<TelecomServiceResponse> getTelecomServices();
}
