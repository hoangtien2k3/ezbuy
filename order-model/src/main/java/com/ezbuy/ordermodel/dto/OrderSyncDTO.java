package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderSyncDTO {

    private String id;

    private String orderCode;

    private Integer state;

    private String description;

    private LocalDateTime createAt;

    private String updateBy;

    private String type;

    private String orderId;
}
