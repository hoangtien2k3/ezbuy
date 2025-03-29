package com.ezbuy.notificationservice.repository.query

interface ChannelQuery {
    companion object {
        const val FIND_CHANNEL_ID_BY_TYPE = "SELECT id FROM channel WHERE type = :channelType AND status = 1"
    }
}
