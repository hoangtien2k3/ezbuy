package com.ezbuy.notimodel.dto.request;

import lombok.Builder;

@Builder
public record ReceiverDataDTO(
        String userId,
        String email
) {
}
