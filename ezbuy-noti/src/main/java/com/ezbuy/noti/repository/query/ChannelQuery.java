package com.ezbuy.noti.repository.query;

public interface ChannelQuery {
    String FIND_CHANNEL_ID_BY_TYPE = "SELECT id FROM channel WHERE type = : channelType AND status = 1";
}
