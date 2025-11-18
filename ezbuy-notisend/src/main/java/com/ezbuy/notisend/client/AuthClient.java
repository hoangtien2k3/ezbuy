package com.ezbuy.notisend.client;

import com.ezbuy.notisend.model.dto.response.ContactResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {
    Mono<ContactResponse> getContacts(List<String> userIds);
}
