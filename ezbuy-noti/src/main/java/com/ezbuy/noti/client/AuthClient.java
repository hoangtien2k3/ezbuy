package com.ezbuy.noti.client;

import com.ezbuy.noti.model.dto.response.ContactResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {

    Mono<ContactResponse> getContacts(List<String> userIds);

    Mono<List<String>> getAllUserId();

    Mono<String> getEmailsByUsername(String username);
}