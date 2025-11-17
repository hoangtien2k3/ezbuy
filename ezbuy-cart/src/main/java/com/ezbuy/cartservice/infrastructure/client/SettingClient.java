package com.ezbuy.cartservice.infrastructure.client;

import java.util.List;

import com.ezbuy.cartservice.application.dto.CartTelecomDTO;
import reactor.core.publisher.Mono;

public interface SettingClient {
    Mono<List<CartTelecomDTO>> getTelecomService();
}
