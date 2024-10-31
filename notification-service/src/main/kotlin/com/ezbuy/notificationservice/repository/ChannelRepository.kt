package com.ezbuy.notificationservice.repository

import com.ezbuy.notificationservice.repository.query.ChannelQuery
import com.ezbuy.notimodel.model.Channel
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.Query
import reactor.core.publisher.Mono

interface ChannelRepository : R2dbcRepository<Channel, String> {

    override fun findById(id: String): Mono<Channel>

    @Query(ChannelQuery.findChannelIdByType)
    fun findChannelIdByType(channelType: String): Mono<String>
}
