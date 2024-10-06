package com.viettel.sme.cartservice.client;

import com.ezbuy.cartmodel.dto.CartTelecomDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SettingClient {
    Mono<List<CartTelecomDTO>> getTelecomService();
}
