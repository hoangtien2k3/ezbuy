package com.ezbuy.ordermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItem {

    private String id;

    private String orderId;

    private String orderCode;

    private String productId;

    private String telecomServiceId;

    private String name;

    private Double price;

    private Double originPrice;

    private Integer quantity;

    private String currency;

    private String description;

    private String reviewContent;

    private String rating;

    private String duration;

    private Integer status;

    private String telecomServiceName;

    private String telecomServiceAlias; // bo sung alias theo PYCXXX/LuongToanTrinhScontract

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private String subscriberId;

    private Integer isBundle;

    private String accountId;

    private String productCode;

    private Integer state;
}
