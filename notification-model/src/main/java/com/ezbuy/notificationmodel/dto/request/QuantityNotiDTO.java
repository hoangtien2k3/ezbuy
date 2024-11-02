package com.ezbuy.notificationmodel.dto.request;

import lombok.Builder;

@Builder
public record QuantityNotiDTO(Integer quantityNewNoti, Integer quantityUnreadNotices, Integer quantityUnreadNews) {}
