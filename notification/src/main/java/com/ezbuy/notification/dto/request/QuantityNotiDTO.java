package com.ezbuy.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityNotiDTO {
    private Integer quantityNewNoti;
    private Integer quantityUnreadNotices;
    private Integer quantityUnreadNews;
}
