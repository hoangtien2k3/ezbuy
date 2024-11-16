package com.ezbuy.searchservice.client;

import java.util.List;
import reactor.core.publisher.Mono;

public interface ElasticsearchClient {

    Mono<String> search(String keyword, List<String> type, Integer from, Integer size);
}
