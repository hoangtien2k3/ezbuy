package com.ezbuy.notificationmodel.dto.request;

import lombok.Builder;

@Builder
public record ReceiverDataDTO(String userId, String email) {}
