package com.ezbuy.cartservice.client;

import com.ezbuy.cartmodel.dto.CartTelecomDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SettingClient {
    Mono<List<CartTelecomDTO>> getTelecomService();
}
