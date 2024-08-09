package com.ezbuy.notiservice.repository.query;

public interface ChannelQuery {
    String findChannelIdByType = "SELECT id FROM channel WHERE type = :channelType AND status = 1";
}
