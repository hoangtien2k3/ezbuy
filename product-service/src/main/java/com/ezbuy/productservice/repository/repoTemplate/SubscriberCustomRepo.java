package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.sme.productmodel.response.StatisticSubscriberResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface SubscriberCustomRepo {

    Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<Long> telecomServiceIds);
}
