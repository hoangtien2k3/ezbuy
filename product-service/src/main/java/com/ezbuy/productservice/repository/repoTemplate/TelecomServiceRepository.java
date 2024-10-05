package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.sme.productmodel.dto.TelecomServiceResponse;
import com.ezbuy.sme.productmodel.response.MegaMenuResponse;
import reactor.core.publisher.Flux;

public interface TelecomServiceRepository {
    Flux<MegaMenuResponse> getProductsForMegaMenu();
    Flux<TelecomServiceResponse> getTelecomServices();
}
