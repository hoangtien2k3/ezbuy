package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.Channel;
import com.ezbuy.notiservice.repository.query.ChannelQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ChannelRepository extends R2dbcRepository<Channel, String> {

    @Query(ChannelQuery.FIND_CHANNEL_ID_BY_TYPE)
    Mono<String> findChannelIdByType(String channelType);
}
