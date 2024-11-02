package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.response.StatisticSubscriberResponse;
import java.util.List;
import reactor.core.publisher.Flux;

public interface SubscriberCustomRepo {

    Flux<StatisticSubscriberResponse> getStatisticSubscriber(String idNo, List<Long> telecomServiceIds);
}
