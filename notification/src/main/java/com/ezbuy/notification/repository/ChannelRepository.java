package com.ezbuy.notification.repository;

import com.ezbuy.notification.model.Channel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import static com.ezbuy.notification.repository.query.ChannelQuery.findChannelIdByType;

public interface ChannelRepository extends R2dbcRepository<Channel, String> {
    Mono<Channel> findById(String id);

    @Query(findChannelIdByType)
    Mono<String> findChannelIdByType(String channelType);
}
