package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.response.StatisticSubscriberResponse;
import com.ezbuy.productmodel.dto.response.TotalSubscriberResponse;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubscriberCustomRepository {

    Mono<TotalSubscriberResponse> findByTelecomServiceIdAndIdNo(Long telecomServiceId);

    Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<String> telecomServiceIds, Integer time);
}
