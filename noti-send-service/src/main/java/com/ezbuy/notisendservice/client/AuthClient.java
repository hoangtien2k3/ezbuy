package com.ezbuy.notisendservice.client;

import com.ezbuy.notimodel.dto.response.ContactResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {
    Mono<ContactResponse> getContacts(List<String> userIds);
}
