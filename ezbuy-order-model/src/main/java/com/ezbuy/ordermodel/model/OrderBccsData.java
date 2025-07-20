package com.ezbuy.ordermodel.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "order_bccs_data")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderBccsData {
    private String orderId;
    private String data;
    private String orderType;
    private Integer status;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
}
