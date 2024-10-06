package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.response.StatisticSubscriberResponse;
import com.ezbuy.productmodel.response.TotalSubscriberResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SubscriberCustomRepository {

    Mono<TotalSubscriberResponse> findByTelecomServiceIdAndIdNo(Long telecomServiceId);

    Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<String> telecomServiceIds, Integer time);

}
