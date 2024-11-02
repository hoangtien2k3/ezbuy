package com.ezbuy.paymentservice.client;

import java.util.List;
import reactor.core.publisher.Mono;

public interface AuthClient {

    Mono<List<String>> getTrustedIdNoOrganization(String organizationId);
}
