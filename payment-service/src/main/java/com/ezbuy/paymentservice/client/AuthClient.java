package com.ezbuy.paymentservice.client;

import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {

    Mono<List<String>> getTrustedIdNoOrganization(String organizationId);
}
