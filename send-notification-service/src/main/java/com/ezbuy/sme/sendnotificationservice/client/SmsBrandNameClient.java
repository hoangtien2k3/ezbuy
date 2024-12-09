package com.ezbuy.sme.sendnotificationservice.client;

import com.ezbuy.notimodel.dto.request.SendMessageRequest;
import com.ezbuy.notimodel.dto.response.SendMessageDTO;
import com.ezbuy.notimodel.dto.response.SmsBrandNameLoginResponse;
import reactor.core.publisher.Mono;

public interface SmsBrandNameClient {

    Mono<SmsBrandNameLoginResponse> login(String username, String password);

    Mono<SendMessageDTO> sendMessage(SendMessageRequest sendMessageReq);

}
