package com.ezbuy.customermodel.dto;

import java.time.LocalDateTime;

public record ShopUserDTO(
        Integer customerId,
        Integer statusView,
        Integer statusAccount,
        LocalDateTime lastLogin,
        String roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer loyalty_points
) {
}
