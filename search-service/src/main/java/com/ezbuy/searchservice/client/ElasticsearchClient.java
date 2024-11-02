package com.ezbuy.searchservice.client;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ElasticsearchClient {

    Mono<String> search(String keyword, List<String> type, Integer from, Integer size);

}
