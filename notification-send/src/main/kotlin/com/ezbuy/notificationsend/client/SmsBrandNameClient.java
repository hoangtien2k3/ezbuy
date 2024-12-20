package com.ezbuy.notificationsend.client;

import com.ezbuy.notificationmodel.dto.request.SendMessageRequest;
import com.ezbuy.notificationmodel.dto.response.SendMessageDTO;
import com.ezbuy.notificationmodel.dto.response.SmsBrandNameLoginResponse;
import reactor.core.publisher.Mono;

public interface SmsBrandNameClient {

    Mono<SmsBrandNameLoginResponse> login(String username, String password);

    Mono<SendMessageDTO> sendSMSBR(SendMessageRequest sendMessageReq, String auth);

}
