package com.ezbuy.paymentservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "request_banking")
public class RequestBanking {
    private String id;
    private Integer state;
    private Long totalFee;
    private String vtTransactionId;
    private String orderId;
    private String orderType;
    private Integer orderState;
    private Integer status;
    private String merchantCode;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private Integer updateState;
}
