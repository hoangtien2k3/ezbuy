package com.ezbuy.sendnotification.client;

import com.ezbuy.sendnotification.model.noti.ContactResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {
    Mono<ContactResponse> getContacts(List<String> userIds);
}
