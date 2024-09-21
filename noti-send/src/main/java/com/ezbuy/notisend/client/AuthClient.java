package com.ezbuy.notisend.client;

import com.ezbuy.notimodel.dto.response.ContactResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface AuthClient {
    Mono<ContactResponse> getContacts(List<String> userIds);
}
